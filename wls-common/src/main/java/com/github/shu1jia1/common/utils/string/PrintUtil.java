package com.github.shu1jia1.common.utils.string;

import java.math.BigInteger;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;

public final class PrintUtil {

    private static final Logger logger = LoggerFactory.getLogger(PrintUtil.class);

    private PrintUtil() {
        // no comment
    }

    /**
     * 获取字节流的16进制编码
     * 
     * @param inBytes
     * @return
     */
    public static String toHexStr(byte[] inBytes) {
        return printBytes(inBytes, "%02X");
    }

    /**
     * 获取字节流的16进制编码
     * 
     * @param inBytes
     * @param maxLenth
     *            最多打印字节数
     * @return
     */
    public static String toHexStr(byte[] inBytes, int maxLenth) {
        return toPrintBytes(inBytes, "%02X", maxLenth);
    }

    /**
     * 获取字节流的16进制编码，0x开头
     * 
     * @param inBytes
     * @return
     */
    public static String to0xHexStr(byte[] inBytes) {
        return printBytes(inBytes, "0x%02X");
    }

    /**
     * 获取字节流的10进制编码
     * 
     * @param inBytes
     * @return
     */
    public static String to10Str(byte[] inBytes) {
        return printBytes(inBytes, "%02d");
    }

    /**
     * 打印字节数组，使用format格式化单个byte (String.format(format, inBytes[i] & 0xff));
     * format = %02X 会打印HEX String
     * 
     * @param inBytes
     * @param format
     * @return
     */
    private static String printBytes(byte[] inBytes, String format) {
        return toPrintBytes(inBytes, format, inBytes.length);
    }
    
    
    /**
     * 打印字节数组，使用format格式化单个byte (String.format(format, inBytes[i] & 0xff));
     * format = %02X 会打印HEX String
     * 
     * @param inBytes
     * @param format
     * @return
     */
    public static String toPrintBytes(byte[] inBytes, String format, int maxLength) {
        if (inBytes == null) {
            return "null";
        }
        int len = maxLength;
        if (inBytes.length < maxLength) {
            len = inBytes.length;
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < len; i++) {
            sb.append(String.format(format, inBytes[i] & 0xff));
            if (i == len - 1) {
                sb.append("]");
            } else {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    /**
     * 打印列表
     * 
     * @param cs
     */
    public static String toString(Collection<?> cs) {
        if (cs == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder("[");
        sb.append(StringUtils.join(cs.iterator(), ','));
        sb.append("]");
        return sb.toString();
    }

    /**
     * 打印列表 多行
     * 
     * @param cs
     */
    public static String toMultiLineString(Collection<?> cs) {
        if (cs == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder("[");
        sb.append(StringUtils.join(cs.iterator(), ",\n"));
        sb.append("]");
        return sb.toString();
    }

    /**
     * 打印16进制Str
     * 
     * @param i
     * @return
     */
    public static String toHexStr(BigInteger i) {
        if (i == null) {
            return "null";
        }
        return "0x" + i.toString(16);
    }

    public static void main(String[] args) {
        logger.info(String.format("%02d", (byte) 0xff));
        logger.info(BigInteger.valueOf(255255).toString(16));
    }

}
