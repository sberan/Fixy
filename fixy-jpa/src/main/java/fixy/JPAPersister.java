package fixy;

import javax.persistence.Entity;
import javax.persistence.EntityManager;

class JPAPersister implements Persister {

    private final EntityManager entityManager;
    private boolean mergeEntities;

    public JPAPersister(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public JPAPersister(EntityManager entityManager, boolean mergeEntities) {
        this.entityManager = entityManager;
        this.mergeEntities = mergeEntities;
    }
    
    public void persist(Object entity) {
        if (entity.getClass().isAnnotationPresent(Entity.class)) {
            if (mergeEntities) {
                entityManager.merge(entity);
            } else {
                entityManager.persist(entity);
            }

        }
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

}
