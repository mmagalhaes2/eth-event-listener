package com.math.cleanarchex.infra.driven.blockchainProvider.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;


@Component("asyncTaskService")
public class SingleThreadedAsyncTaskService implements AsyncTaskService {

    private final Map<String, ExecutorService> executorServices = new HashMap<>();

    @Override
    public void execute(String executorName, Runnable task) {
        getOrCreateExecutor(executorName).execute(task);
    }

    @Override
    public <T> Future<T> submit(String executorName, Callable<T> task) {
        return getOrCreateExecutor(executorName).submit(task);
    }

    @Override
    public CompletableFuture<Void> executeWithCompletableFuture(String executorName, Runnable task) {
        return CompletableFuture.runAsync(task, getOrCreateExecutor(executorName));
    }

    private ExecutorService getOrCreateExecutor(String executorName) {
        if (!executorServices.containsKey(executorName)) {
            executorServices.put(executorName, buildExecutor(executorName));
        }

        return executorServices.get(executorName);
    }

    protected ExecutorService buildExecutor(String executorName) {
        return Executors.newSingleThreadExecutor(
                new ThreadFactoryBuilder().setNameFormat(executorName + "-%d").build());
    }
}
