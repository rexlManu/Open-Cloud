/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.command;

import java.lang.annotation.*;

public interface Command {

	boolean execute(final String[] args);

	default void printHelp() {}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@interface CommandInfo {

		String name();

		String[] aliases() default {};
	}

}
