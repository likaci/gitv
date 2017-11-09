package org.greenrobot.eventbus;

import android.os.Looper;
import android.util.Log;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

public class EventBus {
    private static final EventBusBuilder DEFAULT_BUILDER = new EventBusBuilder();
    public static String TAG = "EventBus";
    static volatile EventBus defaultInstance;
    private static final Map<Class<?>, List<Class<?>>> eventTypesCache = new HashMap();
    private final AsyncPoster asyncPoster;
    private final BackgroundPoster backgroundPoster;
    private final ThreadLocal<PostingThreadState> currentPostingThreadState;
    private final boolean eventInheritance;
    private final ExecutorService executorService;
    private final int indexCount;
    private final boolean logNoSubscriberMessages;
    private final boolean logSubscriberExceptions;
    private final HandlerPoster mainThreadPoster;
    private final boolean sendNoSubscriberEvent;
    private final boolean sendSubscriberExceptionEvent;
    private final Map<Class<?>, Object> stickyEvents;
    private final SubscriberMethodFinder subscriberMethodFinder;
    private final Map<Class<?>, CopyOnWriteArrayList<Subscription>> subscriptionsByEventType;
    private final boolean throwSubscriberException;
    private final Map<Object, List<Class<?>>> typesBySubscriber;

    interface PostCallback {
        void onPostCompleted(List<SubscriberExceptionEvent> list);
    }

    static final class PostingThreadState {
        boolean canceled;
        Object event;
        final List<Object> eventQueue = new ArrayList();
        boolean isMainThread;
        boolean isPosting;
        Subscription subscription;

        PostingThreadState() {
        }
    }

    public static EventBus getDefault() {
        if (defaultInstance == null) {
            synchronized (EventBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new EventBus();
                }
            }
        }
        return defaultInstance;
    }

    public static EventBusBuilder builder() {
        return new EventBusBuilder();
    }

    public static void clearCaches() {
        SubscriberMethodFinder.clearCaches();
        eventTypesCache.clear();
    }

    public EventBus() {
        this(DEFAULT_BUILDER);
    }

    EventBus(EventBusBuilder builder) {
        this.currentPostingThreadState = new ThreadLocal<PostingThreadState>() {
            protected PostingThreadState initialValue() {
                return new PostingThreadState();
            }
        };
        this.subscriptionsByEventType = new HashMap();
        this.typesBySubscriber = new HashMap();
        this.stickyEvents = new ConcurrentHashMap();
        this.mainThreadPoster = new HandlerPoster(this, Looper.getMainLooper(), 10);
        this.backgroundPoster = new BackgroundPoster(this);
        this.asyncPoster = new AsyncPoster(this);
        this.indexCount = builder.subscriberInfoIndexes != null ? builder.subscriberInfoIndexes.size() : 0;
        this.subscriberMethodFinder = new SubscriberMethodFinder(builder.subscriberInfoIndexes, builder.strictMethodVerification, builder.ignoreGeneratedIndex);
        this.logSubscriberExceptions = builder.logSubscriberExceptions;
        this.logNoSubscriberMessages = builder.logNoSubscriberMessages;
        this.sendSubscriberExceptionEvent = builder.sendSubscriberExceptionEvent;
        this.sendNoSubscriberEvent = builder.sendNoSubscriberEvent;
        this.throwSubscriberException = builder.throwSubscriberException;
        this.eventInheritance = builder.eventInheritance;
        this.executorService = builder.executorService;
    }

    public void register(Object subscriber) {
        List<SubscriberMethod> subscriberMethods = this.subscriberMethodFinder.findSubscriberMethods(subscriber.getClass());
        synchronized (this) {
            for (SubscriberMethod subscriberMethod : subscriberMethods) {
                subscribe(subscriber, subscriberMethod);
            }
        }
    }

    private void subscribe(Object subscriber, SubscriberMethod subscriberMethod) {
        Class<?> eventType = subscriberMethod.eventType;
        Subscription newSubscription = new Subscription(subscriber, subscriberMethod);
        CopyOnWriteArrayList<Subscription> subscriptions = (CopyOnWriteArrayList) this.subscriptionsByEventType.get(eventType);
        if (subscriptions == null) {
            subscriptions = new CopyOnWriteArrayList();
            this.subscriptionsByEventType.put(eventType, subscriptions);
        } else if (subscriptions.contains(newSubscription)) {
            throw new EventBusException("Subscriber " + subscriber.getClass() + " already registered to event " + eventType);
        }
        int size = subscriptions.size();
        int i = 0;
        while (i <= size) {
            if (i == size || subscriberMethod.priority > ((Subscription) subscriptions.get(i)).subscriberMethod.priority) {
                subscriptions.add(i, newSubscription);
                break;
            }
            i++;
        }
        List<Class<?>> subscribedEvents = (List) this.typesBySubscriber.get(subscriber);
        if (subscribedEvents == null) {
            subscribedEvents = new ArrayList();
            this.typesBySubscriber.put(subscriber, subscribedEvents);
        }
        subscribedEvents.add(eventType);
        if (!subscriberMethod.sticky) {
            return;
        }
        if (this.eventInheritance) {
            for (Entry<Class<?>, Object> entry : this.stickyEvents.entrySet()) {
                if (eventType.isAssignableFrom((Class) entry.getKey())) {
                    checkPostStickyEventToSubscription(newSubscription, entry.getValue());
                }
            }
            return;
        }
        checkPostStickyEventToSubscription(newSubscription, this.stickyEvents.get(eventType));
    }

    private void checkPostStickyEventToSubscription(Subscription newSubscription, Object stickyEvent) {
        if (stickyEvent != null) {
            postToSubscription(newSubscription, stickyEvent, Looper.getMainLooper() == Looper.myLooper());
        }
    }

    public synchronized boolean isRegistered(Object subscriber) {
        return this.typesBySubscriber.containsKey(subscriber);
    }

    private void unsubscribeByEventType(Object subscriber, Class<?> eventType) {
        List<Subscription> subscriptions = (List) this.subscriptionsByEventType.get(eventType);
        if (subscriptions != null) {
            int size = subscriptions.size();
            int i = 0;
            while (i < size) {
                Subscription subscription = (Subscription) subscriptions.get(i);
                if (subscription.subscriber == subscriber) {
                    subscription.active = false;
                    subscriptions.remove(i);
                    i--;
                    size--;
                }
                i++;
            }
        }
    }

    public synchronized void unregister(Object subscriber) {
        List<Class<?>> subscribedTypes = (List) this.typesBySubscriber.get(subscriber);
        if (subscribedTypes != null) {
            for (Class<?> eventType : subscribedTypes) {
                unsubscribeByEventType(subscriber, eventType);
            }
            this.typesBySubscriber.remove(subscriber);
        } else {
            Log.w(TAG, "Subscriber to unregister was not registered before: " + subscriber.getClass());
        }
    }

    public void post(Object event) {
        PostingThreadState postingState = (PostingThreadState) this.currentPostingThreadState.get();
        List<Object> eventQueue = postingState.eventQueue;
        eventQueue.add(event);
        if (!postingState.isPosting) {
            boolean z;
            if (Looper.getMainLooper() == Looper.myLooper()) {
                z = true;
            } else {
                z = false;
            }
            postingState.isMainThread = z;
            postingState.isPosting = true;
            if (postingState.canceled) {
                throw new EventBusException("Internal error. Abort state was not reset");
            }
            while (!eventQueue.isEmpty()) {
                try {
                    postSingleEvent(eventQueue.remove(0), postingState);
                } finally {
                    postingState.isPosting = false;
                    postingState.isMainThread = false;
                }
            }
        }
    }

    public void cancelEventDelivery(Object event) {
        PostingThreadState postingState = (PostingThreadState) this.currentPostingThreadState.get();
        if (!postingState.isPosting) {
            throw new EventBusException("This method may only be called from inside event handling methods on the posting thread");
        } else if (event == null) {
            throw new EventBusException("Event may not be null");
        } else if (postingState.event != event) {
            throw new EventBusException("Only the currently handled event may be aborted");
        } else if (postingState.subscription.subscriberMethod.threadMode != ThreadMode.POSTING) {
            throw new EventBusException(" event handlers may only abort the incoming event");
        } else {
            postingState.canceled = true;
        }
    }

    public void postSticky(Object event) {
        synchronized (this.stickyEvents) {
            this.stickyEvents.put(event.getClass(), event);
        }
        post(event);
    }

    public <T> T getStickyEvent(Class<T> eventType) {
        T cast;
        synchronized (this.stickyEvents) {
            cast = eventType.cast(this.stickyEvents.get(eventType));
        }
        return cast;
    }

    public <T> T removeStickyEvent(Class<T> eventType) {
        T cast;
        synchronized (this.stickyEvents) {
            cast = eventType.cast(this.stickyEvents.remove(eventType));
        }
        return cast;
    }

    public boolean removeStickyEvent(Object event) {
        boolean z;
        synchronized (this.stickyEvents) {
            Class<?> eventType = event.getClass();
            if (event.equals(this.stickyEvents.get(eventType))) {
                this.stickyEvents.remove(eventType);
                z = true;
            } else {
                z = false;
            }
        }
        return z;
    }

    public void removeAllStickyEvents() {
        synchronized (this.stickyEvents) {
            this.stickyEvents.clear();
        }
    }

    public boolean hasSubscriberForEvent(Class<?> eventClass) {
        List<Class<?>> eventTypes = lookupAllEventTypes(eventClass);
        if (eventTypes != null) {
            int countTypes = eventTypes.size();
            for (int h = 0; h < countTypes; h++) {
                CopyOnWriteArrayList<Subscription> subscriptions;
                Class<?> clazz = (Class) eventTypes.get(h);
                synchronized (this) {
                    subscriptions = (CopyOnWriteArrayList) this.subscriptionsByEventType.get(clazz);
                }
                if (subscriptions != null && !subscriptions.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void postSingleEvent(Object event, PostingThreadState postingState) throws Error {
        Class<?> eventClass = event.getClass();
        boolean subscriptionFound = false;
        if (this.eventInheritance) {
            List<Class<?>> eventTypes = lookupAllEventTypes(eventClass);
            for (int h = 0; h < eventTypes.size(); h++) {
                subscriptionFound |= postSingleEventForEventType(event, postingState, (Class) eventTypes.get(h));
            }
        } else {
            subscriptionFound = postSingleEventForEventType(event, postingState, eventClass);
        }
        if (!subscriptionFound) {
            if (this.logNoSubscriberMessages) {
                Log.d(TAG, "No subscribers registered for event " + eventClass);
            }
            if (this.sendNoSubscriberEvent && eventClass != NoSubscriberEvent.class && eventClass != SubscriberExceptionEvent.class) {
                post(new NoSubscriberEvent(this, event));
            }
        }
    }

    private boolean postSingleEventForEventType(Object event, PostingThreadState postingState, Class<?> eventClass) {
        synchronized (this) {
            CopyOnWriteArrayList<Subscription> subscriptions = (CopyOnWriteArrayList) this.subscriptionsByEventType.get(eventClass);
        }
        if (subscriptions == null || subscriptions.isEmpty()) {
            return false;
        }
        Iterator it = subscriptions.iterator();
        loop0:
        while (it.hasNext()) {
            Subscription subscription = (Subscription) it.next();
            postingState.event = event;
            postingState.subscription = subscription;
            boolean z = false;
            try {
                postToSubscription(subscription, event, postingState.isMainThread);
                z = postingState.canceled;
                continue;
            } finally {
                postingState.event = null;
                postingState.subscription = null;
                postingState.canceled = false;
            }
            if (z) {
                break loop0;
            }
        }
        return true;
    }

    private void postToSubscription(Subscription subscription, Object event, boolean isMainThread) {
        switch (subscription.subscriberMethod.threadMode) {
            case POSTING:
                invokeSubscriber(subscription, event);
                return;
            case MAIN:
                if (isMainThread) {
                    invokeSubscriber(subscription, event);
                    return;
                } else {
                    this.mainThreadPoster.enqueue(subscription, event);
                    return;
                }
            case BACKGROUND:
                if (isMainThread) {
                    this.backgroundPoster.enqueue(subscription, event);
                    return;
                } else {
                    invokeSubscriber(subscription, event);
                    return;
                }
            case ASYNC:
                this.asyncPoster.enqueue(subscription, event);
                return;
            default:
                throw new IllegalStateException("Unknown thread mode: " + subscription.subscriberMethod.threadMode);
        }
    }

    private static List<Class<?>> lookupAllEventTypes(Class<?> eventClass) {
        List<Class<?>> eventTypes;
        synchronized (eventTypesCache) {
            eventTypes = (List) eventTypesCache.get(eventClass);
            if (eventTypes == null) {
                eventTypes = new ArrayList();
                for (Class<?> clazz = eventClass; clazz != null; clazz = clazz.getSuperclass()) {
                    eventTypes.add(clazz);
                    addInterfaces(eventTypes, clazz.getInterfaces());
                }
                eventTypesCache.put(eventClass, eventTypes);
            }
        }
        return eventTypes;
    }

    static void addInterfaces(List<Class<?>> eventTypes, Class<?>[] interfaces) {
        for (Class<?> interfaceClass : interfaces) {
            if (!eventTypes.contains(interfaceClass)) {
                eventTypes.add(interfaceClass);
                addInterfaces(eventTypes, interfaceClass.getInterfaces());
            }
        }
    }

    void invokeSubscriber(PendingPost pendingPost) {
        Object event = pendingPost.event;
        Subscription subscription = pendingPost.subscription;
        PendingPost.releasePendingPost(pendingPost);
        if (subscription.active) {
            invokeSubscriber(subscription, event);
        }
    }

    void invokeSubscriber(Subscription subscription, Object event) {
        try {
            subscription.subscriberMethod.method.invoke(subscription.subscriber, new Object[]{event});
        } catch (InvocationTargetException e) {
            handleSubscriberException(subscription, event, e.getCause());
        } catch (IllegalAccessException e2) {
            throw new IllegalStateException("Unexpected exception", e2);
        }
    }

    private void handleSubscriberException(Subscription subscription, Object event, Throwable cause) {
        if (event instanceof SubscriberExceptionEvent) {
            if (this.logSubscriberExceptions) {
                Log.e(TAG, "SubscriberExceptionEvent subscriber " + subscription.subscriber.getClass() + " threw an exception", cause);
                SubscriberExceptionEvent exEvent = (SubscriberExceptionEvent) event;
                Log.e(TAG, "Initial event " + exEvent.causingEvent + " caused exception in " + exEvent.causingSubscriber, exEvent.throwable);
            }
        } else if (this.throwSubscriberException) {
            throw new EventBusException("Invoking subscriber failed", cause);
        } else {
            if (this.logSubscriberExceptions) {
                Log.e(TAG, "Could not dispatch event: " + event.getClass() + " to subscribing class " + subscription.subscriber.getClass(), cause);
            }
            if (this.sendSubscriberExceptionEvent) {
                post(new SubscriberExceptionEvent(this, cause, event, subscription.subscriber));
            }
        }
    }

    ExecutorService getExecutorService() {
        return this.executorService;
    }

    public String toString() {
        return "EventBus[indexCount=" + this.indexCount + ", eventInheritance=" + this.eventInheritance + AlbumEnterFactory.SIGN_STR;
    }
}
