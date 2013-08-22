package com.fixy;

import org.hibernate.SessionFactory;
import org.yaml.snakeyaml.introspector.BeanAccess;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds a Fixy for use with JPA.
 */
public class HibernateFixyBuilder {

    private SessionFactory sessionFactory;
    private boolean mergeEntities;
    private String defaultPackage;
    private BeanAccess beanAccess = BeanAccess.DEFAULT;

    /**
     * Creates the builder with a given Hibernate SessionFactory.
     *
     * @param entityManager the Hibernate SessionFactory to use
     */
    public HibernateFixyBuilder(SessionFactory entityManager) {
        this.sessionFactory = checkNotNull(entityManager);
    }

    /**
     * <p>Sets the default package for your entities.</p>
     * <p>The default package will be used if you do not provide a package in your Yaml files.</p>
     *
     * @param defaultPackage the default package name
     * @return the HibernateFixyBuilder for further configuration
     */
    public HibernateFixyBuilder withDefaultPackage(String defaultPackage) {
        this.defaultPackage = checkNotNull(defaultPackage);
        return this;
    }

    /**
     * Enables entity merging instead of persisting.
     *
     * @return the HibernateFixyBuilder for further configuration
     */
    public HibernateFixyBuilder mergeEntities() {
        this.mergeEntities = true;
        return this;
    }

    /**
     * Enables field access for entities. This allows immutable fields inside entities.
     *
     * @return the HibernateFixyBuilder for further configuration
     */
    public HibernateFixyBuilder useFieldAccess() {
        this.beanAccess = BeanAccess.FIELD;
        return this;
    }

    /**
     * Builds the Fixy using the provided builder configuration.
     *
     * @return the configured Fixy
     */
    public Fixy build() {
        return new CoreFixy(
            new HibernatePersister(sessionFactory, mergeEntities),
            defaultPackage,
            beanAccess);
    }

}
