package com.fixy;

import com.google.code.morphia.Datastore;
import org.yaml.snakeyaml.introspector.BeanAccess;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds a Fixy for use with Morphia.
 */
public class MorphiaFixyBuilder {

    private Datastore datastore;
    private String defaultPackage;
    private BeanAccess beanAccess = BeanAccess.DEFAULT;

    /**
     * Creates the builder with a given Morphia Datastore.
     *
     * @param datastore the Morphia datastore to use
     */
    public MorphiaFixyBuilder(Datastore datastore) {
        this.datastore = checkNotNull(datastore);
    }

    /**
     * <p>Sets the default package for your entities.</p>
     * <p>The default package will be used if you do not provide a package in your Yaml files.</p>
     *
     * @param defaultPackage the default package name
     * @return the MorphiaFixyBuilder for further configuration
     */
    public MorphiaFixyBuilder withDefaultPackage(String defaultPackage) {
        this.defaultPackage = checkNotNull(defaultPackage);
        return this;
    }

    /**
     * Enables field access for entities. This allows immutable fields inside entities.
     *
     * @return the MorphiaFixyBuilder for further configuration
     */
    public MorphiaFixyBuilder useFieldAccess() {
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
            new MorphiaPersister(datastore), defaultPackage, beanAccess);
    }
}
