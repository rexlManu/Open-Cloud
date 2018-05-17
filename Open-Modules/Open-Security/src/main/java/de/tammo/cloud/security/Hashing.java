/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.security;

import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;

public class Hashing {

    private static SHA3.DigestSHA3 DIGESTSHA3 = new SHA3.Digest512();

    public static String hash(final String input) {
        return Hex.toHexString(DIGESTSHA3.digest(input.getBytes()));
    }

    public static boolean verify(final String input, final String hash) {
        return hash(input).equals(hash);
    }

}
