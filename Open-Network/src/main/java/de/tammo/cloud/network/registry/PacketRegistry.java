/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.network.registry;

import de.tammo.cloud.network.packet.Packet;

import java.util.ArrayList;

public class PacketRegistry {

    private static final ArrayList<Class<? extends Packet>> in = new ArrayList<>();
    private static final ArrayList<Class<? extends Packet>> out = new ArrayList<>();

    public enum PacketDirection {
        IN,
        OUT;
    }

}

