package org.tron.tronj.utils;

import org.junit.jupiter.api.Test;
import org.bouncycastle.util.encoders.Hex;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.tron.tronj.utils.Base58Check.bytesToBase58;
import static org.tron.tronj.utils.Base58Check.base58ToBytes;

public class Base58CheckTest {

    @Test
    public void testAddressConverting() {
        byte[] rawAddr = Hex.decode("4159d3ad9d126e153b9564417d3a05cf51c1964edf");
        assertArrayEquals(rawAddr, base58ToBytes("TJAAinkKN2h9KxtBZXw6SyL7HwCQXnzFsE"));
        assertEquals(bytesToBase58(rawAddr), "TJAAinkKN2h9KxtBZXw6SyL7HwCQXnzFsE");
    }
}
