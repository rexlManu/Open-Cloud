/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.network.registry;

import de.tammo.cloud.network.packet.Packet;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class PacketRegistry {

    private static final HashMap<Integer, Class<? extends Packet>> in = new HashMap<>();
    private static final HashMap<Integer, Class<? extends Packet>> out = new HashMap<>();

    public static Packet getPacketById(final int id, final PacketDirection direction) throws IllegalAccessException, InstantiationException {
        return direction.getPackets().get(id).newInstance();
    }

    public static int getIdByPacket(final Packet packet, final PacketDirection direction) {
        return direction.getPackets().entrySet().stream().filter(entry -> entry.getValue() == packet.getClass()).map(Map.Entry::getKey).findFirst().orElse(-1);
    }

    public enum PacketDirection {
        IN,
        OUT;

        @Getter
        private final HashMap<Integer, Class<? extends Packet>> packets = new HashMap<>();

        public void addPacket(final int id, final Class<? extends Packet> packet) {
            this.packets.put(id, packet);
        }
    }

}