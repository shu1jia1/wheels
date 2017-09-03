package com.st.common.message.entity;

import java.util.Arrays;

import com.github.shu1jia1.common.exception.MessageDecodeException;
import static com.google.common.base.Preconditions.*;

import io.netty.buffer.ByteBuf;

public class CHeaderMessage {
    byte[] tag = new byte[] { 0x55, (byte) 0xaa, 0x55, (byte) 0xaa };
    int pkgLength;// 2bytes;
    byte[] dest = new byte[4]; // 4byte
    byte[] src = new byte[4]; // 4byte
    byte destType;
    byte signType;
    int sig; // 2byte
    byte[] ret = new byte[2]; // 2byte
    byte ackYn;
    int pkgNum; // 2bytes
    int packageNo; // 2bytes
    int dataLength; // 长度
    byte icrc;//
    byte[] data; // 低字节在前，高字节在后,不定
    byte crc;
    byte[] end = new byte[] { 0x0d, 0x0a };

    public CHeaderMessage formBytes(ByteBuf bytebufer) throws MessageDecodeException {
        bytebufer.markReaderIndex();
        byte[] tagBytes = new byte[4];
        bytebufer.readBytes(tagBytes);
        //
        if (!Arrays.equals(tagBytes, tagBytes)) {
            bytebufer.resetReaderIndex();
            throw new MessageDecodeException("tag not match" + toPrintBytes(bytebufer, 30));
        }
        pkgLength = bytebufer.readUnsignedShort();
        bytebufer.readBytes(dest);
        bytebufer.readBytes(src);
        destType = bytebufer.readByte();
        signType = bytebufer.readByte();
        sig = bytebufer.readUnsignedShort();
        bytebufer.readBytes(ret);
        ackYn = bytebufer.readByte();
        pkgNum = bytebufer.readUnsignedShort(); // 2bytes
        packageNo = bytebufer.readUnsignedShort(); // 2bytes
        dataLength = bytebufer.readUnsignedShort(); // 2bytes // 长度
        icrc = bytebufer.readByte();//
        data = new byte[dataLength]; // 低字节在前，高字节在后,不定
        crc = bytebufer.readByte();
        byte[] endBytes = new byte[2];
        if (!Arrays.equals(end, endBytes)) {
            throw new MessageDecodeException("end tag not match");
        }
        return this;
    }

    public ByteBuf toBytes() throws MessageDecodeException {
        if (pkgLength == 0) {
            throw new MessageDecodeException("pkgLength not set.");
        }
        return null;
    }

    public int getCLength() {
        return 4 + 2 + 4 + 4 + 1 + 1 + 2 + 2 + 1 + 2 + 2 + 1 + 2 + dataLength + 1 + 2;
    }

    public int getLength() {
        return pkgLength;
    }

    /**
     * 打印字节数组，使用format格式化单个byte (String.format(format, inBytes[i] & 0xff));
     * format = %02X 会打印HEX String
     * 
     * @param inBytes
     * @param format
     * @return
     */
    private String toPrintBytes(ByteBuf inBytes, int maxLength) {
        String format = "%02X";
        if (inBytes == null) {
            return "null";
        }
        int len = maxLength;
        if (inBytes.readableBytes() < maxLength) {
            len = inBytes.readableBytes();
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < len; i++) {
            sb.append(String.format(format, inBytes.readByte() & 0xff));
            if (i == len - 1) {
                sb.append("]");
            } else {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

}
