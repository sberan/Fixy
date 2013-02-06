package com.fixy;

import javax.persistence.EntityManager;

public class JPAFixy {
    public static Fixy create(EntityManager entityManager) {
        return new CoreFixy(new JPAPersister(entityManager));
    }

    public static Fixy create(EntityManager entityManager, boolean mergeEntities) {
        return new CoreFixy(new JPAPersister(entityManager,mergeEntities));
    }
    
    public static Fixy create(EntityManager entityManager, String defaultPackage) {
        return new CoreFixy(new JPAPersister(entityManager), defaultPackage);
    }
    
    public static Fixy create(EntityManager entityManager, String defaultPackage, boolean mergeEntities) {
        return new CoreFixy(new JPAPersister(entityManager, mergeEntities), defaultPackage);
    }
}
