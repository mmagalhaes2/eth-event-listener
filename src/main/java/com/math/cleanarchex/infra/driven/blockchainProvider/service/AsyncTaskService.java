package com.math.cleanarchex.infra.driven.blockchainProvider.service;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface AsyncTaskService {

    void execute(String executorName, Runnable task);

    CompletableFuture<Void> executeWithCompletableFuture(String executorName, Runnable task);

    <T> Future<T> submit(String executorName, Callable<T> task);
}
