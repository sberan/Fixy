package com.fixy;

import com.google.code.morphia.Datastore;

public class MorphiaFixy extends Fixy {
    public MorphiaFixy(Datastore datastore, String defaultPackage) {
        super(new MorphiaPersister(datastore), defaultPackage);
    }

    public MorphiaFixy(Datastore datastore) {
        super(new MorphiaPersister(datastore));
    }
}
