package com.handson.agent.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@Component
public class ThreadManager implements ApplicationListener<ContextClosedEvent> {
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    private List<Future<?>> futures = new ArrayList<>();

    public void addTask(Runnable task) {
        futures.add(taskExecutor.submit(task));
    }

    public void stopAllTasks() {
        for (Future<?> future : futures) {
            future.cancel(true); // Attempt to cancel execution of the task
        }
        taskExecutor.shutdown(); // Shut down the executor
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        stopAllTasks(); // Stop all tasks when the application context is closed
    }
}