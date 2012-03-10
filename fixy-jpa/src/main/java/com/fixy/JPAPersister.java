package com.fixy;

import javax.persistence.Entity;
import javax.persistence.EntityManager;

class JPAPersister implements Persister {

    private final EntityManager entityManager;

    public JPAPersister(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void persist(Object entity) {
        if (entity.getClass().isAnnotationPresent(Entity.class)) {
            entityManager.persist(entity);
        }
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

}
