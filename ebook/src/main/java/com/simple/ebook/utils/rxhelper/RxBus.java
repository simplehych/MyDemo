package com.simple.ebook.utils.rxhelper;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by LiangLu on 17-12-18.
 * 原理:PublishSubject本身作为观察者和被观察者。
 */

public class RxBus {
    private static volatile RxBus sInstance;
    private final PublishSubject<Object> mEventBus = PublishSubject.create();

    public static RxBus getInstance() {
        if (sInstance == null) {
            synchronized (RxBus.class) {
                if (sInstance == null) {
                    sInstance = new RxBus();
                }
            }
        }
        return sInstance;
    }

    /**
     * 发送事件(post event)
     *
     * @param event : event object(事件的内容)
     */
    public void post(Object event) {
        mEventBus.onNext(event);
    }

    /**
     * @param code
     * @param event
     */
    public void post(int code, Object event) {
        Message msg = new Message(code, event);
        mEventBus.onNext(msg);
    }

    /**
     * 返回Event的管理者,进行对事件的接受
     *
     * @return
     */
    public Observable toObservable() {
        return mEventBus;
    }

    /**
     * @param cls :保证接受到制定的类型
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(Class<T> cls) {
        //ofType起到过滤的作用,确定接受的类型
        return mEventBus.ofType(cls);
    }

    public <T> Observable<T> toObservable(final int code, final Class<T> cls) {
        return mEventBus.ofType(Message.class)
                .filter(new Predicate<Message>() {
                    @Override
                    public boolean test(Message msg) throws Exception {
                        return msg.code == code && cls.isInstance(msg.event);
                    }
                })
                .map(new Function<Message, T>() {
                         @Override
                         public T apply(Message msg) throws Exception {
                             return (T) msg.event;
                         }
                     }
                );

    }

    class Message {
        int code;
        Object event;

        public Message(int code, Object event) {
            this.code = code;
            this.event = event;
        }
    }
}