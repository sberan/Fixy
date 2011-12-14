package com.pearson.fixie;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.extensions.compactnotation.CompactData;
import org.yaml.snakeyaml.extensions.compactnotation.PackageCompactConstructor;
import org.yaml.snakeyaml.nodes.ScalarNode;

import javax.persistence.EntityManager;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class EntityConstructor extends PackageCompactConstructor {
    Map<String, Object> entityCache = Maps.newLinkedHashMap();
    Multimap<Class<?>, EntityPostProcessor<? super Object>> postProcessors = HashMultimap.create();
    private List<Object> allEntities = Lists.newArrayList();
    private Queue<Object> processQueue = Lists.newLinkedList();

    public EntityConstructor(String packageName) {
        super(packageName);
    }

    @Override
    protected Object createInstance(ScalarNode node, CompactData data) throws Exception {
        List<String> arguments = Lists.newArrayList();
        if(!entityCache.containsKey(node.getValue())) {
            data.getArguments().clear();
            Object entity = super.createInstance(node, data);
            entityCache.put(node.getValue(), entity);
        }
        return entityCache.get(node.getValue());
    }

    public void loadEntities(InputStream... files) {
        Yaml yaml = new Yaml(this);
        for(InputStream file : files) {
            yaml.load(file);
        }
    }

    public void persistEntities(EntityManager entityManager) {
        Queue<Object> processQueue = new LinkedList<Object>(entityCache.values());
        allEntities.addAll(entityCache.values());
        while(!processQueue.isEmpty()) {
            Object entity = processQueue.remove();
            for(Map.Entry<Class<?>, EntityPostProcessor<? super Object>> entry : postProcessors.entries()) {
                if(entity.getClass().isAssignableFrom(entry.getKey())) {
                    EntityPostProcessor<? super Object> postProcessor = entry.getValue();
                    postProcessor.processQueue = processQueue;
                    postProcessor.allEntities = allEntities;
                    postProcessor.process(entity);
                }
            }
            entityManager.persist(entity);
        }
    }

    public <T> void addPostProcessor(EntityPostProcessor<T> postProcessor) {
        postProcessors.put(postProcessor.getType(), (EntityPostProcessor<? super Object>) postProcessor);
    }

    public void removeEntities(EntityManager entityManager) {
        Collections.reverse(allEntities);
        for(Object entity : allEntities) {
            entityManager.remove(entity);
        }
    }
}
