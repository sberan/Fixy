package fixy;

import javax.persistence.EntityManager;

/**
 * Creates a Fixy for use with JPA.
 *
 * @deprecated Please use {@link JpaFixyBuilder} instead.
 */
@Deprecated
public class JPAFixy {

    private JPAFixy() {}

    @Deprecated
    public static Fixy create(EntityManager entityManager) {
        return new CoreFixy(new JPAPersister(entityManager));
    }

    @Deprecated
    public static Fixy create(EntityManager entityManager, boolean mergeEntities) {
        return new CoreFixy(new JPAPersister(entityManager,mergeEntities));
    }

    @Deprecated
    public static Fixy create(EntityManager entityManager, String defaultPackage) {
        return new CoreFixy(new JPAPersister(entityManager), defaultPackage);
    }

    @Deprecated
    public static Fixy create(EntityManager entityManager, String defaultPackage, boolean mergeEntities) {
        return new CoreFixy(new JPAPersister(entityManager, mergeEntities), defaultPackage);
    }
}
