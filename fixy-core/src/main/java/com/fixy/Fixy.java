package com.fixy;

public interface Fixy {
    public void load(String... files);
    public <T> void addProcessor(Processor<T> postProcessor);
}
