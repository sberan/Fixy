package com.fixy;

import java.util.Queue;

public abstract class Processor<T> {
    Queue<Object> processQueue;
    private final Class<? super T> type;

    public Processor(Class<? super T> type) {
        this.type = type;
    }

    public abstract void process(T entity);

    protected void addEntity(Object entity) {
        processQueue.add(entity);
    }

    public Class<? super T> getType() {
        return type;
    }
}