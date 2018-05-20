/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.wrapper.setup;

import de.tammo.cloud.core.logging.Logger;
import de.tammo.cloud.core.setup.Setup;
import de.tammo.cloud.core.setup.requests.StringRequest;
import de.tammo.cloud.wrapper.Wrapper;
import de.tammo.cloud.wrapper.network.packets.WrapperKeyOutPacket;
import jline.console.ConsoleReader;

import java.io.IOException;

public class WrapperSetup implements Setup {

    public void setup(final Logger logger, final ConsoleReader reader) throws IOException {
        if (Wrapper.getWrapper().getConfiguration().getKey().isEmpty()) {
            new StringRequest().request(logger, "Type in the key, you received from the Open-Master", reader, key -> Wrapper.getWrapper().getConfiguration().setKey(key));
            Wrapper.getWrapper().getNetworkHandler().sendPacketToMaster(new WrapperKeyOutPacket(Wrapper.getWrapper().getConfiguration().getKey()));
        }
    }

}
