package com.st.common.message.entity;

import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;

import com.github.shu1jia1.common.exception.MessageDecodeException;
import com.st.common.message.entity.STCommon.Address;
import com.st.common.message.entity.STCommon.AddressType;
import com.st.common.message.entity.STCommon.CmdCode;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 * 包头解析
 * <p>文件名称: CHeaderMessage.java/p>
 * <p>文件描述: </p>
 * @version 2.0
 * @author  lov
 */
public class CHeaderMessageV2 {
    public static final byte[] COMMON_TAG = new byte[] { 0x55, (byte) 0xaa, 0x55, (byte) 0xaa };
    public static final byte[] PLC_TAG = new byte[] { (byte) 0xFF, (byte) 0x99, (byte) 0x88, (byte) 0xff };
    private byte[] tag = new byte[] { 0x55, (byte) 0xaa, 0x55, (byte) 0xaa };
    private int pkgLength;// 2bytes;
    private long flowNo; // 4bytes
    private byte[] dest = new byte[4]; // 4byte
    private byte[] src = new byte[4]; // 4byte
    private short cmd; // 1byte
    private short dataType; // 1byte
    private short statusCode; // 1byte
    private int pkgNum; // 1bytes
    private byte[] reverse = new byte[4]; // 4byte
    private byte[] data; // 低字节在前，高字节在后,不定
    private byte crc;

    private byte[] origBytes;
    private int SrcType;

    public long getFlowNo() {
        return flowNo;
    }

    public CHeaderMessageV2 formBytes(ByteBuf bytebufer) throws MessageDecodeException {
        origBytes = ByteBufUtil.getBytes(bytebufer);
        bytebufer.markReaderIndex();
        byte[] tagBytes = new byte[4];
        bytebufer.readBytes(tagBytes);
        //
        if (!Arrays.equals(tagBytes, COMMON_TAG) && !Arrays.equals(tagBytes, PLC_TAG)) {
            bytebufer.resetReaderIndex();
            throw new MessageDecodeException("tag not match" + toPrintBytes(bytebufer, 30));
        }
        pkgLength = bytebufer.readUnsignedShort();
        flowNo = bytebufer.readUnsignedInt();
        bytebufer.readBytes(dest);
        bytebufer.readBytes(src);
        cmd = bytebufer.readUnsignedByte();
        dataType = bytebufer.readUnsignedByte();
        statusCode = bytebufer.readUnsignedByte();
        pkgNum = bytebufer.readUnsignedByte(); // 1bytes
        bytebufer.readBytes(reverse);
        data = new byte[getDataLength()]; // 低字节在前，高字节在后,不定
        bytebufer.readBytes(data);
        crc = bytebufer.readByte();
        return this;
    }

    public CHeaderMessageV2 makeResponse(boolean success, byte[] data) {
        CHeaderMessageV2 resp = new CHeaderMessageV2();
        resp.tag = Arrays.copyOf(tag, tag.length);
        resp.flowNo = this.flowNo;
        resp.src = Arrays.copyOf(this.dest, this.dest.length);
        resp.dest = Arrays.copyOf(this.src, this.src.length);
        resp.cmd = this.cmd;
        resp.dataType = 0;// this.dataType;
        resp.statusCode = (short) (success ? 0 : 1);
        resp.pkgNum = this.pkgNum;
        resp.reverse = Arrays.copyOf(this.reverse, this.reverse.length);
        resp.data = data; // 低字节在前，高字节在后,不定
        // resp.crc = bytebufer.readByte();
        // 重算size
        resp.pkgLength = resp.getCLength();
        resp.build();
        return resp;
    }

    private void build() {
        if (pkgLength == 0) {
            // throw new MessageDecodeException("pkgLength not set.");
            this.pkgLength = getCLength();
        }
        ByteBuf bytebufer = Unpooled.buffer(this.pkgLength);
        bytebufer.writeBytes(tag);
        bytebufer.writeShort(pkgLength);
        bytebufer.writeInt((int) (flowNo & 0xffffffff));
        bytebufer.writeBytes(dest);
        bytebufer.writeBytes(src);
        bytebufer.writeByte(cmd);
        bytebufer.writeByte(dataType);
        bytebufer.writeByte(statusCode);
        bytebufer.writeByte(pkgNum);
        bytebufer.writeBytes(reverse);
        bytebufer.writeBytes(data);
        // crc
        bytebufer.writeByte(0x01);
        origBytes = new byte[pkgLength];
        bytebufer.readBytes(origBytes);
    }

    private void resetCrc() {

    }

    public byte[] toBytes() throws MessageDecodeException {
        if (origBytes == null || origBytes.length == 0) {
            this.build();
        }
        return origBytes;
    }

    public int getCLength() {
        return getHeaderLength() + 1 + data.length;
    }

    public int getHeaderLength() {
        return 26;
    }

    /**
     * 数据长度等于pkgLength-headerlen-crc
     * @return
     */
    public int getDataLength() {
        return pkgLength - getHeaderLength() - 1;
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

    public byte[] getDest() {
        return dest;
    }

    public String getDestStr() {
        return new String(Hex.encodeHex(dest));
    }

    public String getSrcStr() {
        return new String(Hex.encodeHex(src));
    }

    public void setDest(byte[] dest) {
        this.dest = dest;
    }

    public byte[] getSrc() {
        return src;
    }

    public void setSrc(byte[] src) {
        this.src = src;
    }

    public Address getSrc(CHeaderMessageV2 cMessage) {
        Address.Builder builder = Address.newBuilder().setIdentify(cMessage.getSrcStr());
        if (Arrays.equals(tag, COMMON_TAG)) {

        }
        return builder.build();
    }

    public String getSimpleInfo() {
        StringBuilder builder = new StringBuilder();
        builder.append("CHeaderMessageV2{flowNo:");
        builder.append(flowNo);
        builder.append(", dest:");
        builder.append(Arrays.toString(dest));
        builder.append(", src:");
        builder.append(Arrays.toString(src));
        builder.append(", cmd:");
        builder.append(cmd);
        builder.append(", dataType:");
        builder.append(dataType);
        builder.append(", statusCode:");
        builder.append(statusCode);
        builder.append(", pkgNum:");
        builder.append(pkgNum);
        builder.append("}");
        return builder.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CHeaderMessageV2{tag:");
        builder.append(Arrays.toString(tag));
        builder.append(", pkgLength:");
        builder.append(pkgLength);
        builder.append(", flowNo:");
        builder.append(flowNo);
        builder.append(", dest:");
        builder.append(Arrays.toString(dest));
        builder.append(", src:");
        builder.append(Arrays.toString(src));
        builder.append(", cmd:");
        builder.append(cmd);
        builder.append(", dataType:");
        builder.append(dataType);
        builder.append(", statusCode:");
        builder.append(statusCode);
        builder.append(", pkgNum:");
        builder.append(pkgNum);
        builder.append(", reverse:");
        builder.append(Arrays.toString(reverse));
        builder.append(", data:");
        builder.append(Arrays.toString(data));
        builder.append(", crc:");
        builder.append(crc);
        builder.append(", origBytes:");
        builder.append(Arrays.toString(origBytes));
        builder.append("}");
        return builder.toString();
    }

    public CmdCode getCmdCode() {
        CmdCode code = CmdCode.forNumber(cmd);
        return code;
    }

    public short getCmd() {
        return cmd;
    }

    public int getSrcType() {
        if (Arrays.equals(tag, COMMON_TAG)) {
            return AddressType.MACHINE_VALUE;
        }
        if (Arrays.equals(tag, PLC_TAG)) {
            return AddressType.PLC_VALUE;
        }
        return 0;
    }

}
