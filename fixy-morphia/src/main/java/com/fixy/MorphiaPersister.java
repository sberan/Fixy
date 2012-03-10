package com.fixy;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Entity;
import com.pearson.fixy.Persister;

public class MorphiaPersister implements Persister {

    private final Datastore ds;
    
    public MorphiaPersister(Datastore ds) {
        this.ds = ds;
    }
    
    public void persist(Object entity) {
        if (entity.getClass().isAnnotationPresent(Entity.class)) {
            ds.save(entity);
        }
    }
}
