package com.pearson.fixie;

import java.util.List;
import java.util.Queue;

public abstract class EntityPostProcessor<T> {
    Queue<Object> processQueue;

    private final Class<? super T> type;
    public List<Object> allEntities;

    public EntityPostProcessor(Class<? super T> type) {
        this.type = type;
    }

    public abstract void process(T entity);

    protected void addEntity(Object entity) {
        processQueue.add(entity);
        allEntities.add(entity);
    }

    public Class<? super T> getType() {
        return type;
    }
}