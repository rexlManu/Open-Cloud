/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.command;

import com.google.common.reflect.ClassPath;
import de.tammo.cloud.core.logging.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class CommandHandler {

	private final ArrayList<Command> commands = new ArrayList<>();

	public CommandHandler(final String commandPackage, final Logger logger) {
		try {
			for (final ClassPath.ClassInfo classInfo : ClassPath.from(this.getClass().getClassLoader()).getTopLevelClasses(commandPackage)) {
				final Class commandClass = Class.forName(classInfo.getName());
				if (Command.class.isAssignableFrom(commandClass) && commandClass.isAnnotationPresent(Command.CommandInfo.class)) {
					this.commands.add((Command) commandClass.newInstance());
					logger.debug("Command " + classInfo.getSimpleName() + " was added to the command list!");
				} else {
					logger.warn("Command " + classInfo.getSimpleName() + " does not implement the Command interface or have the CommandInfo annotation!");
				}
			}
		} catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
			logger.error("Could not initialize commands!", e);
		}
	}

	public void executeCommand(final String message, final Logger logger) {
		if (message.trim().isEmpty()) return;

		final AtomicBoolean found = new AtomicBoolean(false);

		this.commands.forEach(command -> {
			final Command.CommandInfo commandInfo = command.getClass().getAnnotation(Command.CommandInfo.class);
			final ArrayList<String> triggers = new ArrayList<>(Arrays.asList(commandInfo.aliases()));
			triggers.add(commandInfo.name());

			final String[] arguments = message.split(" ");

			if (triggers.stream().anyMatch(trigger -> arguments[0].equalsIgnoreCase(trigger))) {
				final String[] args = new String[arguments.length - 1];
				System.arraycopy(arguments, 1, args, 0, args.length);
				if (!command.execute(args)) command.printHelp();
				found.set(true);
			}
		});

		if (!found.get()) logger.info("Command not found!");
	}

}
