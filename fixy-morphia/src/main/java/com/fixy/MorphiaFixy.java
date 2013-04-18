package com.fixy;

import com.google.code.morphia.Datastore;

/**
 * Creates a Fixy for use with Morphia.
 *
 * @deprecated Please use {@link MorphiaFixyBuilder} instead.
 */
@Deprecated
public class MorphiaFixy {

    private MorphiaFixy() {}

    @Deprecated
    public static Fixy create(Datastore datastore, String defaultPackage) {
        return  new CoreFixy(new MorphiaPersister(datastore), defaultPackage);
    }

    @Deprecated
    public static Fixy create(Datastore datastore) {
        return new CoreFixy(new MorphiaPersister(datastore));
    }
}
