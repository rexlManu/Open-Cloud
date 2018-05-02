/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.wrapper.commands;

import de.tammo.cloud.core.command.Command;
import de.tammo.cloud.wrapper.Wrapper;

@Command.CommandInfo(name = "stop", aliases = {"shutdown", "terminate"})
public class StopCommand implements Command {

    public void execute(final String[] args) {
        Wrapper.getWrapper().setRunning(false);
    }

}
