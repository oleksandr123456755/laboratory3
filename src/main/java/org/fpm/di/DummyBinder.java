package org.fpm.di;

import org.fpm.di.Binder;
import org.fpm.di.Configuration;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyBinder implements Binder {
    private List<Object> classes = new ArrayList<>();
    private Map<Object, Object> implementClasses = new HashMap<>();
    private Map<Object, Object> instanceClasses = new HashMap<>();
    private List<Class<?>> toCreate = new ArrayList<>();


    public DummyBinder(Configuration configuration) {
        configuration.configure(this);
    }

    @Override
    public <T> void bind(Class<T> clazz) {
        classes.add(clazz);
        if (clazz.isAnnotationPresent(Singleton.class)){
            toCreate.add(clazz);
            classes.remove(clazz);
        }
    }

    @Override
    public <T> void bind(Class<T> clazz, Class<? extends T> implementation) {
        implementClasses.put(clazz, implementation);
    }

    @Override
    public <T> void bind(Class<T> clazz, T instance) {
        instanceClasses.put(clazz,instance);
    }

    public <T> List<Class<?>> GetToCreate(){
        return toCreate;
    }

    public void postBind(Class<?> clazz, Object instance) {
        instanceClasses.put(clazz, instance);
    }

    public List<Object> getClasses(){
        return classes;
    }

    public Map<Object, Object> getImplementClasses() {
        return implementClasses;
    }

    public Map<Object, Object> getInstanceClasses() {
        return instanceClasses;
    }
}
