package retrofit.android;

import retrofit.RestAdapter.Log;

public class AndroidLog implements Log {
    private static final int LOG_CHUNK_SIZE = 4000;
    private final String tag;

    public AndroidLog(String tag) {
        this.tag = tag;
    }

    public final void log(String message) {
        int len = message.length();
        for (int i = 0; i < len; i += 4000) {
            logChunk(message.substring(i, Math.min(len, i + 4000)));
        }
    }

    public void logChunk(String chunk) {
        android.util.Log.d(getTag(), chunk);
    }

    public String getTag() {
        return this.tag;
    }
}
