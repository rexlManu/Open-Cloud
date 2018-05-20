/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.core.logging;

import lombok.RequiredArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@RequiredArgsConstructor
public class Logger {

    private final String logPath;

    private final String prefix;

    private final LogLevel level;

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy//MM//dd");

    public void debug(final Object any) {
        this.log(any, LogLevel.DEBUG);
    }

    public void info(final Object any) {
        this.log(any, LogLevel.INFO);
    }

    public void warn(final Object any) {
        this.log(any, LogLevel.WARNING);
    }

    public void error(final Object any, final Exception exception) {
        this.log(any, LogLevel.ERROR);
        exception.printStackTrace();
    }

    private void log(final Object any, final LogLevel logLevel) {
        if (this.level.getLevel() > logLevel.getLevel()) return;

        System.out.println("\r[" + this.timeFormat.format(new Date()) + "] " + this.prefix + " [" + logLevel.getName() + "] " + any.toString());
        System.out.print("\r> ");
    }

}
