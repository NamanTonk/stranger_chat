package com.newEra.strangers.chat.thread_util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CustomThreadPoolExecutor {
    private ExecutorService executorService;

    public CustomThreadPoolExecutor(int numThreads) {
        this.executorService = Executors.newFixedThreadPool(numThreads);
    }

    public void submitTaskAndLeave(Runnable task) {
        this.executorService.execute(task);
    }

    public void submitTaskAndWaitToComplete(Runnable task) {
        try {
            this.executorService.submit(task).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<Callable<Object>, Future<Object>> executeConcurrently(List<Callable<Object>> tasks) {
        Map<Callable<Object>, Future<Object>> futureResult = new HashMap();
        for (Callable<Object> callable : tasks) {
            futureResult.put(callable, this.executorService.submit(callable));
        }
        return futureResult;
    }

    public Map<Callable<Object>, Object> executeAndWaitToComplete(List<Callable<Object>> tasks) {
        Map<Callable<Object>, Object> result = new HashMap();
        Map<Callable<Object>, Future<Object>> futureResult = executeConcurrently(tasks);
        try {
            for (Callable<Object> callable : tasks) {
                result.put(callable, ((Future) futureResult.get(callable)).get());
            }
        } catch (Exception e) {
        }
        return result;
    }
    public Object executeAndWait(Callable<Object> task) throws InterruptedException, ExecutionException {
        return this.executorService.submit(task).get();
    }

}
