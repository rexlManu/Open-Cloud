/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.master.commands;

import de.tammo.cloud.command.Command;
import de.tammo.cloud.master.Master;
import de.tammo.cloud.master.network.wrapper.Wrapper;
import de.tammo.cloud.master.network.wrapper.WrapperMeta;

import java.util.UUID;

@Command.CommandInfo(name = "wrapper", aliases = {"w"})
public class WrapperCommand implements Command {

    public boolean execute(final String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                Master.getMaster().getLogger().info("<-- Wrapper -->");
                Master.getMaster().getNetworkHandler().getWrappers().forEach(wrapper -> Master.getMaster().getLogger().info("Wrapper on host " + wrapper.getWrapperMeta().getHost() + " with unique id " + wrapper.getWrapperMeta().getUuid().toString() + (wrapper.isConnected() ? " is connected" : " is not connected")));
                Master.getMaster().getLogger().info("");
                return true;
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("info")) {
                final Wrapper wrapper = Master.getMaster().getNetworkHandler().getWrapperByHost(args[1]);
                if (wrapper == null) {
                    Master.getMaster().getLogger().warn("Wrapper not available!");
                } else {
                    Master.getMaster().getLogger().info("Wrapper with unique id " + wrapper.getWrapperMeta().getUuid().toString() + " is " + (wrapper.isConnected() ? "connected" : "not connected"));
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("create")) {
                if (Master.getMaster().getNetworkHandler().getWrapperByHost(args[1]) != null) {
                    Master.getMaster().getLogger().warn("Wrapper already exists!");
                    return true;
                }

                final String key = Master.getMaster().getNetworkHandler().generateWrapperKey();
                final Wrapper wrapper = new Wrapper(new WrapperMeta(UUID.randomUUID(), args[1], key));
                Master.getMaster().getNetworkHandler().addWrapper(wrapper);
                Master.getMaster().getLogger().info("Added wrapper on host " + wrapper.getWrapperMeta().getHost() + " with unique id " + wrapper.getWrapperMeta().getUuid().toString());
                Master.getMaster().getLogger().info("Key for wrapper is: " + key);
                return true;
            } else if (args[0].equalsIgnoreCase("delete")) {
                final Wrapper wrapper = Master.getMaster().getNetworkHandler().getWrapperByHost(args[1]);
                if (wrapper == null) {
                    Master.getMaster().getLogger().warn("Wrapper is not created yet!");
                    return true;
                }
                Master.getMaster().getNetworkHandler().removeWrapper(wrapper);
                Master.getMaster().getLogger().info("Removed wrapper with host " + wrapper.getWrapperMeta().getHost() + "!");
                return true;
            }
        }
        return false;
    }

    public void printHelp() {
        Master.getMaster().getLogger().info("wrapper list");
        Master.getMaster().getLogger().info("wrapper info <host>");
        Master.getMaster().getLogger().info("wrapper create <host>");
        Master.getMaster().getLogger().info("wrapper delete <host>");
    }

}
