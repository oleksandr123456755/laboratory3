package org.fpm.di;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class DummyContainer implements Container {
    DummyBinder dummyBinder;
    public DummyContainer(Configuration configuration) {
        dummyBinder = new DummyBinder(configuration);
        dummyBinder.GetToCreate().forEach(clazz -> dummyBinder.postBind(clazz, instanceCreater(clazz)));
    }

    @Override
    public <T> T getComponent(Class<T> clazz) {
        if (dummyBinder.getClasses().contains(clazz))
            return instanceCreater(clazz);
        if (dummyBinder.getImplementClasses().containsKey(clazz))
            return getComponent((Class<T>) dummyBinder.getImplementClasses().get(clazz));
        if (dummyBinder.getInstanceClasses().containsKey(clazz))
            return (T) dummyBinder.getInstanceClasses().get(clazz);
        return null;
    }

    private <T> T instanceCreater(Class<T> forCreate){
        for (Constructor<?> constructor: forCreate.getConstructors()){
            if (constructor.isAnnotationPresent(Inject.class)){
                Object[] objects = new Object[constructor.getParameterCount()];
                for (int i=0; i<objects.length; i++){
                    objects[i] = getComponent(constructor.getParameterTypes()[i]);
                }
                try {
                    return (T) constructor.newInstance(objects);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        try {
            return forCreate.getConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }
}
