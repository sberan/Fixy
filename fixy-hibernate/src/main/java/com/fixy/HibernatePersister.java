package com.fixy;

import org.hibernate.SessionFactory;

import javax.persistence.Entity;

class HibernatePersister implements Persister {

    private final SessionFactory sessionFactory;
    private boolean mergeEntities;

    public HibernatePersister(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public HibernatePersister(SessionFactory sessionFactory, boolean mergeEntities) {
        this.sessionFactory = sessionFactory;
        this.mergeEntities = mergeEntities;
    }
    
    public void persist(Object entity) {
        if (entity.getClass().isAnnotationPresent(Entity.class)) {
            if (mergeEntities) {
                sessionFactory.getCurrentSession().merge(entity);
            } else {
                sessionFactory.getCurrentSession().persist(entity);
            }

        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
