/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.masterapi.event;

import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventHandler {

    private final HashMap<Class, CopyOnWriteArrayList<MethodData>> methods = new HashMap<>();

    public void registerEvent(final Object target) {
        Arrays.stream(target.getClass().getDeclaredMethods()).filter(method -> method.getParameterCount() > 0 && method.isAnnotationPresent(EventTarget.class)).forEach(method -> {
            final Class<?> eventClass = method.getParameterTypes()[0];
            final MethodData methodData = new MethodData(target, method);

            if (this.methods.containsKey(eventClass)) {
                this.methods.get(eventClass).add(methodData);
            } else {
                this.methods.put(eventClass, new CopyOnWriteArrayList<>(Arrays.asList(methodData)));
            }
        });
    }

    public void unregisterEvent(final Object target) {
        this.methods.keySet().forEach(event -> this.methods.get(event).stream().filter(methodData -> methodData.getSource().equals(target)).forEach(methodData -> this.methods.get(event).remove(methodData)));
    }

    public void fireEvent(final Event event) {
        final CopyOnWriteArrayList<MethodData> methodDatas = this.methods.get(event.getClass());

        if(methodDatas == null) return;

        methodDatas.forEach(methodData -> {
            if (!methodData.getMethod().isAccessible()) {
                methodData.getMethod().setAccessible(true);
            }
            try {
                methodData.getMethod().invoke(methodData.getSource(), event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    @Data
    private class MethodData {

        private final Object source;
        private final Method method;
    }

}
