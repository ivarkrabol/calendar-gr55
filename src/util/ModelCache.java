package util;

import models.Model;

import java.util.HashMap;

/**
 * Created by ivar on 23.02.2015.
 *
 * This class keeps track of all model objects in the memory,
 * making sure there are no duplicates.
 *
 * The idea is that each Model subclass should have one or more static methods used to
 * bind an instance from database data, which uses 'ModelCache.contains(type, key)' to
 * check if there is already an object of that type with that (primary) key in the memory;
 * If there is it should then get that memory object using 'ModelCache.get(type, key)'
 * (and overwriting it with potentially updated data from the database.)
 * If not, it should create a new instance of that type, and put it into the cache using
 * 'ModelCache.put(key, instance)' before binding it.
 */
public class ModelCache {
    private HashMap<Class<? extends Model>, HashMap<Object, Model>> cache;

    public ModelCache(){
        cache = new HashMap<Class<? extends Model>, HashMap<Object, Model>>();
    }

    public boolean contains(Class<? extends Model> type, Object key) {
        return cache.containsKey(type) && cache.get(type).containsKey(key);
    }

    public <ModelType extends Model> ModelType get(Class<ModelType> type, Object key) {
        return (ModelType)cache.get(type).get(key);
    }

    public void put(Object key, Model instance) {
        if (!cache.containsKey(instance.getClass())) {
            cache.put(instance.getClass(), new HashMap<Object, Model>());
        }
        cache.get(instance.getClass()).put(key, instance);
    }
}
