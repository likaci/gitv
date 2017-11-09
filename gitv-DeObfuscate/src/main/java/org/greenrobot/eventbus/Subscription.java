package org.greenrobot.eventbus;

final class Subscription {
    volatile boolean active = true;
    final Object subscriber;
    final SubscriberMethod subscriberMethod;

    Subscription(Object subscriber, SubscriberMethod subscriberMethod) {
        this.subscriber = subscriber;
        this.subscriberMethod = subscriberMethod;
    }

    public boolean equals(Object other) {
        if (!(other instanceof Subscription)) {
            return false;
        }
        Subscription otherSubscription = (Subscription) other;
        if (this.subscriber == otherSubscription.subscriber && this.subscriberMethod.equals(otherSubscription.subscriberMethod)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.subscriber.hashCode() + this.subscriberMethod.methodString.hashCode();
    }
}
