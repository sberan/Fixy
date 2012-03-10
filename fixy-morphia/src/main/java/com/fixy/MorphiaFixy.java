package com.fixy;

import com.google.code.morphia.Datastore;
import com.pearson.fixy.Fixy;

public class MorphiaFixy extends Fixy {
    public MorphiaFixy(Datastore datastore, String defaultPackage) {
        super(new MorphiaPersister(datastore), defaultPackage);
    }

    public MorphiaFixy(Datastore datastore) {
        super(new MorphiaPersister(datastore));
    }
}
