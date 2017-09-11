package com.github.shu1jia1.common.utils.string;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class CrcHelper {
    public static byte getCrc8(byte[] data) {
        byte crc = 0;
        for (int i = 0; i < data.length; i++) {
            crc ^= data[i];
        }
        return crc;
    }

    public static void main(String[] args) throws DecoderException {
        byte crc = CrcHelper.getCrc8(
                Hex.decodeHex("55 AA 55 AA 00 1E 00 00 00 00 00 00 00 00 FF FF FF FF 00 01 00 01 00 01 00 00 00 00 03"
                        .replaceAll("\\s", "").toCharArray()));
        System.out.println(String.format("%02X", crc));
    }
}
