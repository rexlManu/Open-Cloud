/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.core.threading;

public class ThreadBuilder {

    private final Thread thread;

    public ThreadBuilder(final Runnable runnable) {
        this.thread = new Thread(runnable);
    }

    public ThreadBuilder(final String name, final Runnable runnable) {
        this.thread = new Thread(runnable, name);
    }

    public final ThreadBuilder setDaemon() {
        this.thread.setDaemon(true);
        return this;
    }

    public final ThreadBuilder startThread() {
        this.thread.start();
        return this;
    }

    public final Thread start() {
        this.thread.start();
        return this.thread;
    }

}