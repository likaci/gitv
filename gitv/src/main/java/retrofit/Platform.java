package retrofit;

import android.os.Build.VERSION;
import android.os.Process;
import com.google.gson.Gson;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import retrofit.RestAdapter.Log;
import retrofit.android.AndroidApacheClient;
import retrofit.android.AndroidLog;
import retrofit.android.MainThreadExecutor;
import retrofit.appengine.UrlFetchClient;
import retrofit.client.Client;
import retrofit.client.Client.Provider;
import retrofit.client.OkClient;
import retrofit.client.UrlConnectionClient;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

abstract class Platform {
    static final boolean HAS_RX_JAVA = hasRxJavaOnClasspath();
    private static final Platform PLATFORM = findPlatform();

    private static class Android extends Platform {
        private Android() {
        }

        Converter defaultConverter() {
            return new GsonConverter(new Gson());
        }

        Provider defaultClient() {
            Client client;
            if (Platform.hasOkHttpOnClasspath()) {
                client = OkClientInstantiator.instantiate();
            } else if (VERSION.SDK_INT < 9) {
                client = new AndroidApacheClient();
            } else {
                client = new UrlConnectionClient();
            }
            return new Provider() {
                public Client get() {
                    return client;
                }
            };
        }

        Executor defaultHttpExecutor() {
            return Executors.newCachedThreadPool(new ThreadFactory() {
                public Thread newThread(final Runnable r) {
                    return new Thread(new Runnable() {
                        public void run() {
                            Process.setThreadPriority(10);
                            r.run();
                        }
                    }, "Retrofit-Idle");
                }
            });
        }

        Executor defaultCallbackExecutor() {
            return new MainThreadExecutor();
        }

        Log defaultLog() {
            return new AndroidLog("Retrofit");
        }
    }

    private static class Base extends Platform {
        private Base() {
        }

        Converter defaultConverter() {
            return new GsonConverter(new Gson());
        }

        Provider defaultClient() {
            Client client;
            if (Platform.hasOkHttpOnClasspath()) {
                client = OkClientInstantiator.instantiate();
            } else {
                client = new UrlConnectionClient();
            }
            return new Provider() {
                public Client get() {
                    return client;
                }
            };
        }

        Executor defaultHttpExecutor() {
            return Executors.newCachedThreadPool(new ThreadFactory() {
                public Thread newThread(final Runnable r) {
                    return new Thread(new Runnable() {
                        public void run() {
                            Thread.currentThread().setPriority(1);
                            r.run();
                        }
                    }, "Retrofit-Idle");
                }
            });
        }

        Executor defaultCallbackExecutor() {
            return new SynchronousExecutor();
        }

        Log defaultLog() {
            return new Log() {
                public void log(String message) {
                    System.out.println(message);
                }
            };
        }
    }

    private static class AppEngine extends Base {
        private AppEngine() {
            super();
        }

        Provider defaultClient() {
            final UrlFetchClient client = new UrlFetchClient();
            return new Provider() {
                public Client get() {
                    return client;
                }
            };
        }
    }

    private static class OkClientInstantiator {
        private OkClientInstantiator() {
        }

        static Client instantiate() {
            return new OkClient();
        }
    }

    abstract Executor defaultCallbackExecutor();

    abstract Provider defaultClient();

    abstract Converter defaultConverter();

    abstract Executor defaultHttpExecutor();

    abstract Log defaultLog();

    Platform() {
    }

    static Platform get() {
        return PLATFORM;
    }

    private static Platform findPlatform() {
        try {
            Class.forName("android.os.Build");
            if (VERSION.SDK_INT != 0) {
                return new Android();
            }
        } catch (ClassNotFoundException e) {
        }
        if (System.getProperty("com.google.appengine.runtime.version") != null) {
            return new AppEngine();
        }
        return new Base();
    }

    private static boolean hasOkHttpOnClasspath() {
        boolean okUrlFactory = false;
        try {
            Class.forName("com.squareup.okhttp.OkUrlFactory");
            okUrlFactory = true;
        } catch (ClassNotFoundException e) {
        }
        boolean okHttpClient = false;
        try {
            Class.forName("com.squareup.okhttp.OkHttpClient");
            okHttpClient = true;
        } catch (ClassNotFoundException e2) {
        }
        if (okHttpClient == okUrlFactory) {
            return okHttpClient;
        }
        throw new RuntimeException("Retrofit detected an unsupported OkHttp on the classpath.\nTo use OkHttp with this version of Retrofit, you'll need:\n1. com.squareup.okhttp:okhttp:1.6.0 (or newer)\n2. com.squareup.okhttp:okhttp-urlconnection:1.6.0 (or newer)\nNote that OkHttp 2.0.0+ is supported!");
    }

    private static boolean hasRxJavaOnClasspath() {
        try {
            Class.forName("rx.Observable");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
