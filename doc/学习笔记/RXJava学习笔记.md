[TOC]
#什么是RXJava
RxJava 是一个在Java虚拟机上实现的响应式扩展库：提供了基于observable序列实现的异步调用及基于事件编程。 它扩展了观察者模式，支持数据、事件序列并允许你合并序列，无需关心底层的线程处理、同步、线程安全、并发数据结构和非阻塞I/O处理。 

下面这句话很常见，但是还不能理解为什么这么说。
>这看起来很像设计模式中的观察者模式，他们最重要的区别之一在于在没有subscriber之前，observable不会产生事件。

#RXJava的HelloWorld
一个简单的RxJava HelloWorld的代码如下。

    // 创建observable
    Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            subscriber.onNext("Hello RxJava");
            subscriber.onCompleted();
        }
    });
     
    // 创建subscriber
    Subscriber<String> subscriber = new Subscriber<String>() {
        @Override
        public void onCompleted() {
     
        }
     
        @Override
        public void onError(Throwable e) {
     
        }
        @Override
        public void onNext(String s) {
            log(s);
        }
    };
     
    // 订阅
    observable.subscribe(subscriber);

这里的代码对于一句简单的HelloWorld的而言太繁琐了。因此，RxJava提供了一些简化的方法。

首先是创建observable，如果我们只需要发送一个事件（这里的事件是字符串”Hellow RxJava”），我们可以使用Observable类的just方法，简化后的代码如下

    // 创建observable
    Observable<String> observable = Observable.just("Hello RxJava");

同样，如果我们不关心subscriber是否结束（onComplete())或者发生错误(onError()),subscriber的代码可以简化为

    // 创建subscriber
    Action1<String> subscriber = new Action1<String>() {
        @Override
        public void call(String s) {
            log(s);
        }
    };

我们直接把创建和订阅连接起来，完整的代码如下。

    Observable.just("Hello RxJava").subscribe(new Action1<String>() {
        @Override
        public void call(String s) {
            log(s);
        }
    });

最后，使用Java 8的lambda(Android上可以使用Retrolambda)，这个HelloWorld的最终版本如下：

    Observable.just("Hello RxJava")
        .subscribe(s -> log(s));



#RXJava的使用场景


#如何使用RXJava
在spring cloud和第三方的rxjava-spring-boot-starter
