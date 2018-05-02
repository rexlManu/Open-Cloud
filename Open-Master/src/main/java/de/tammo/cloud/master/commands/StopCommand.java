/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.master.commands;

import de.tammo.cloud.core.command.Command;
import de.tammo.cloud.master.Master;

@Command.CommandInfo(name = "stop", aliases = {"shutdown", "terminate"})
public class StopCommand implements Command {

    public void execute(final String[] args) {
        Master.getMaster().setRunning(false);
    }
}