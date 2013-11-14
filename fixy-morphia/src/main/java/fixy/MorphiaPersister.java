package fixy;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Entity;

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
