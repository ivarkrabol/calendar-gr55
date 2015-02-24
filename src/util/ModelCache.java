package util;

import models.Model;

import java.util.HashMap;

/**
 * Created by ivar on 23.02.2015.
 *
 * This class keeps track of all model objects in the memory,
 * making sure there are no duplicates.
 *
 * The idea is that each Model subclass should have one or more static methods
 * used to bind an instance from database data, which uses 'contains(type, key)'
 * to check if there is already an object of that type with that key in the memory.
 * If there is it should then get that memory object using 'get(type, key)' and
 * overwriting it with potentially updated data from the database. If there is none,
 * it should create a new instance of that type, and put it into the cache using
 * 'put(key, instance)'.
 *
 */
public class ModelCache {
    private HashMap<Class<? extends Model>, HashMap<Object, Model>> cache;

    private static ModelCache singleton;

    private ModelCache(){
        cache = new HashMap<Class<? extends Model>, HashMap<Object, Model>>();
    }

    private static ModelCache getSingleton() {
        if (singleton == null) singleton = new ModelCache();
        return singleton;
    }

    private boolean hasTypeMap(Class<? extends Model> type) {
        return cache.containsKey(type);
    }

    private HashMap<Object, Model> getTypeMap(Class<? extends Model> type) {
        return cache.get(type);
    }

    public static boolean contains(Class<? extends Model> type, Object key) {
        return getSingleton().hasTypeMap(type) && getSingleton().getTypeMap(type).containsKey(key);
    }

    public static <ModelType extends Model> ModelType get(Class<ModelType> type, Object key) {
        return (ModelType)getSingleton().getTypeMap(type).get(key);
    }

    public static void put(Object key, Model instance) {
        ModelCache modelCache = getSingleton();
        if (!modelCache.cache.containsKey(instance.getClass())) {
            modelCache.cache.put(instance.getClass(), new HashMap<Object, Model>());
        }
        modelCache.getTypeMap(instance.getClass()).put(key, instance);
    }

}
