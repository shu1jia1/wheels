package com.github.shu1jia1.common.task.factory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>文件名称: NamedThreadFactory.java</p>
 * <p>文件描述: 带命名的池线程工厂</p>
 * @author  lov
 */
public class NamedThreadFactory implements ThreadFactory {
    private static final AtomicInteger    poolNumber        = new AtomicInteger(1);
    private final ThreadGroup            group;
    private final AtomicInteger            threadNumber    = new AtomicInteger(1);
    private final String                namePrefix;

    public NamedThreadFactory(String name) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        namePrefix = name + "-pool-" + poolNumber.getAndIncrement() + "-thread-";
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}
