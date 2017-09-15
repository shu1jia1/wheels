package com.github.shu1jia1.common.task.handler.reject;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscardRejectedExecutionHandler implements RejectedExecutionHandler {

    private static final Logger logger = LoggerFactory.getLogger(DiscardRejectedExecutionHandler.class.toString());

    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
        logger.warn("{}: has been rejected", runnable.toString());
        logger.warn("Server: Queue Size: {}\n", executor.getQueue().size());
        logger.warn("Server: Pool Size: {}\n", executor.getPoolSize());
        logger.warn("Server: Active Count: {}\n", executor.getActiveCount());
        logger.warn("Server: Completed Tasks: {}\n", executor.getCompletedTaskCount());
    }

}
