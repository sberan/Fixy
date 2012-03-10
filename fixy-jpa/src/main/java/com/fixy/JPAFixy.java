package com.fixy;

import javax.persistence.EntityManager;

public class JPAFixy {
    public static Fixy create(EntityManager entityManager) {
        return new CoreFixy(new JPAPersister(entityManager));
    }

    public static Fixy create(EntityManager entityManager, String defaultPackage) {
        return new CoreFixy(new JPAPersister(entityManager), defaultPackage);
    }
}
