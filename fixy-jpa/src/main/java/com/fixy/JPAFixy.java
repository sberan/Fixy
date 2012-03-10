package com.fixy;

import javax.persistence.EntityManager;

public class JPAFixy extends Fixy {
    public JPAFixy(EntityManager entityManager) {
        super(new JPAPersister(entityManager));
    }
    
    public JPAFixy(EntityManager entityManager, String defaultPackage) {
        super(new JPAPersister(entityManager), defaultPackage);
    }
}
