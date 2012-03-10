package com.fixy;

import com.google.code.morphia.Datastore;

public class MorphiaFixy {
    public static Fixy create(Datastore datastore, String defaultPackage) {
        return  new CoreFixy(new MorphiaPersister(datastore), defaultPackage);
    }

    public Fixy create(Datastore datastore) {
        return new CoreFixy(new MorphiaPersister(datastore));
    }
}
