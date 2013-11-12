package fixy;

import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;

import org.yaml.snakeyaml.extensions.compactnotation.CompactConstructor;
import org.yaml.snakeyaml.extensions.compactnotation.CompactData;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;


class ConstructImport extends AbstractConstruct {
    private final CoreFixy coreFixy;
    private final List<String> importedPackages = Lists.newArrayList();

    public ConstructImport(CoreFixy coreFixy) {
        this.coreFixy = coreFixy;
    }

    @Override public Object construct(Node node) {
        String location = ((ScalarNode) node).getValue();
        if(!importedPackages.contains(location)) {
            importedPackages.add(location);
            coreFixy.loadEntities(location);
        }
        return null;
    }
}

class ConstructPackage extends AbstractConstruct {
    private final CoreFixy coreFixy;

    public ConstructPackage(CoreFixy coreFixy) {
        this.coreFixy = coreFixy;
    }

    @Override public Object construct(Node node) {
        String packageName = ((ScalarNode) node).getValue();
        coreFixy.setPackage(packageName);
        return "";
    }
}

/**
 * CoreFixy allows you to create Java classes from YAML markup.
 *
 */
public final class CoreFixy extends CompactConstructor implements Fixy {
    private final Map<String, Object> entityCache = Maps.newLinkedHashMap();
    private final Multimap<Class<?>, Processor<? super Object>> postProcessors = HashMultimap.create();
    private final Persister persister;
    private final String defaultPackage;
    private final BeanAccess beanAccess;
    private String packageName;

    public CoreFixy(Persister persister) {
        this(persister, "");
    }

    public CoreFixy(Persister persister, String defaultPackage) {
        this(persister, defaultPackage, BeanAccess.DEFAULT);
    }

    public CoreFixy(Persister persister, String defaultPackage, BeanAccess beanAccess) {
        this.yamlConstructors.put(new Tag("!import"), new ConstructImport(this));
        this.yamlConstructors.put(new Tag("!package"), new ConstructPackage(this));
        this.defaultPackage = defaultPackage;
        this.packageName = defaultPackage;
        this.persister = persister;
        this.beanAccess = beanAccess;
    }

    @Override
    protected Class<?> getClassForName(String name) throws ClassNotFoundException {
        if(!Strings.isNullOrEmpty(packageName)) {
            try {
                return super.getClassForName(packageName + "." + name);
            } catch (ClassNotFoundException ignored) { }
        }
        ClassNotFoundException exceptionToThrow;
        try {
            return super.getClassForName(name);
        } catch (ClassNotFoundException e) {
            exceptionToThrow = e;
        }
        try {
            return super.getClassForName("java.lang." + name);
        } catch (ClassNotFoundException ignored) { }
        throw exceptionToThrow;
    }

    @Override
    protected Object createInstance(ScalarNode node, CompactData data) throws Exception {
        if(!entityCache.containsKey(node.getValue())) {
            data.getArguments().clear();
            Object entity = super.createInstance(node, data);
            entityCache.put(node.getValue(), entity);
        }
        return entityCache.get(node.getValue());
    }

    void loadEntities(String... files) {
    	Yaml yaml = new Yaml(this);
        yaml.setBeanAccess(beanAccess);
        for(String file : files) {
            if(!file.startsWith("/")) {
                file = "/" + file;
            }
            String origPackage = this.packageName;
            this.packageName = this.defaultPackage;
            yaml.load(getClass().getResourceAsStream(file));
            this.packageName = origPackage;
        }
    }

    void persistEntities() {
        Queue<Object> processQueue = new LinkedList<Object>(entityCache.values());
        while(!processQueue.isEmpty()) {
            Object entity = processQueue.remove();
            for(Map.Entry<Class<?>, Processor<? super Object>> entry : postProcessors.entries()) {
                if(entity.getClass().isAssignableFrom(entry.getKey())) {
                    Processor<? super Object> postProcessor = entry.getValue();
                    postProcessor.processQueue = processQueue;
                    postProcessor.process(entity);
                }
            }
            persister.persist(entity);
        }
    }

    public void load(String... files) {
        loadEntities(files);
        persistEntities();
    }

    public <T> void addProcessor(Processor<T> postProcessor) {
        @SuppressWarnings("unchecked") //TODO: get the generic type of postProcessors right
        Processor<? super Object> casted = (Processor<? super Object>) postProcessor;
        postProcessors.put(postProcessor.getType(), casted);
    }

    void setPackage(String packageName) {
        this.packageName = packageName;
    }
}
