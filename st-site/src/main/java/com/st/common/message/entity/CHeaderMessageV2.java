package com.st.common.message.entity;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.codec.binary.Hex;

import com.github.shu1jia1.common.exception.MessageDecodeException;
import com.github.shu1jia1.common.utils.string.CrcHelper;
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
    public static AtomicInteger FlownoGen = new AtomicInteger(0);

    public static final byte[] COMMON_TAG = new byte[] { 0x55, (byte) 0xaa, 0x55, (byte) 0xaa };
    public static final byte[] PLC_TAG = new byte[] { (byte) 0xFF, (byte) 0x99, (byte) 0x88, (byte) 0xff };
    private byte[] tag = new byte[] { 0x55, (byte) 0xaa, 0x55, (byte) 0xaa };
    private int pkgLength;// 2bytes;
    private long flowNo; // 4bytes
    private byte[] dest = new byte[4]; // 4byte
    private byte[] src = new byte[4]; // 4byte
    private short dstType;
    private short srcType;
    private short cmd; // 1byte
    private short dataType = 0; // 1byte
    private short statusCode = 0; // 1byte
    private int pkgNum = 1; // 1bytes
    private byte[] reverse = new byte[4]; // 4byte
    private byte[] data; // 低字节在前，高字节在后,不定
    private byte crc;

    private byte[] origBytes;

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
        dstType = bytebufer.readUnsignedByte();
        srcType = bytebufer.readUnsignedByte();
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
        resp.dstType = this.srcType;
        resp.srcType = this.dstType;
        resp.cmd = this.cmd;
        resp.dataType = 2;// this.dataType;
        resp.statusCode = (short) (success ? 0 : 1);
        resp.pkgNum = this.pkgNum;
        resp.reverse = Arrays.copyOf(this.reverse, this.reverse.length);
        resp.data = data; // 低字节在前，高字节在后,不定
        // resp.crc = bytebufer.readByte();
        // 重算size
        resp.pkgLength = resp.getCLength();
        return resp;
    }

    public CHeaderMessageV2 build() {
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
        bytebufer.writeByte(dstType);
        bytebufer.writeByte(srcType);
        bytebufer.writeByte(cmd);
        bytebufer.writeByte(dataType);
        bytebufer.writeByte(statusCode);
        bytebufer.writeByte(pkgNum);
        bytebufer.writeBytes(reverse);
        bytebufer.writeBytes(data);
        // crc
        byte crc = CrcHelper.getCrc8(ByteBufUtil.getBytes(bytebufer));
        bytebufer.writeByte(crc);
        origBytes = new byte[pkgLength];
        bytebufer.readBytes(origBytes);
        return this;
    }

    public CHeaderMessageV2 withCmd(CmdCode cmd) {
        this.cmd = (short) cmd.getNumber();
        return this;
    }

    public CHeaderMessageV2 withDest(AddressType type, String id) {
        this.dstType = (short) type.getNumber();
        byte[] devNo = org.springframework.security.crypto.codec.Hex.decode(id);
        System.arraycopy(devNo, devNo.length - 4, dest, 0, 4);
        return this;
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
        return 28;
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

    public Address getSrcAddr() {
        return Address.newBuilder().setIdentify(new String(Hex.encodeHex(src)))
                .setAddrType(AddressType.forNumber(srcType)).build();
    }

    public Address getDestAddr() {
        return Address.newBuilder().setIdentify(new String(Hex.encodeHex(dest)))
                .setAddrType(AddressType.forNumber(dstType)).build();
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
        builder.append("{CMsg-");
        builder.append(flowNo).append("");
        builder.append(CmdCode.forNumber(cmd) == null ? "cmd:" + cmd : CmdCode.forNumber(cmd));
        builder.append("src");
        builder.append(AddressType.forNumber(srcType) == null ? srcType : AddressType.forNumber(srcType));
        builder.append(Arrays.toString(src));
        builder.append("--->dst");
        builder.append(AddressType.forNumber(dstType) == null ? dstType : AddressType.forNumber(dstType));
        builder.append(Arrays.toString(dest));
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
        builder.append(CmdCode.forNumber(cmd) == null ? cmd : CmdCode.forNumber(cmd));
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

    public static byte[] getCommonTag() {
        return COMMON_TAG;
    }

    public static byte[] getPlcTag() {
        return PLC_TAG;
    }

    public byte[] getTag() {
        return tag;
    }

    public int getPkgLength() {
        return pkgLength;
    }

    public short getDstType() {
        return dstType;
    }

    public short getSrcType() {
        return srcType;
    }

    public short getDataType() {
        return dataType;
    }

    public short getStatusCode() {
        return statusCode;
    }

    public int getPkgNum() {
        return pkgNum;
    }

    public byte[] getReverse() {
        return reverse;
    }

    public byte[] getData() {
        return data;
    }

    public byte getCrc() {
        return crc;
    }

    public byte[] getOrigBytes() {
        return origBytes;
    }

    public static CHeaderMessageV2 makeForceLogin() {
        CHeaderMessageV2 msg = new CHeaderMessageV2();
        msg = msg.withCmd(CmdCode.CMD_LoginRequet).withAllDest().withCloudSrc().withAutoFlowNo().withDataType(1)
                .withData(new byte[] { 0x03 });
        msg.build();
        return msg;
    }

    private CHeaderMessageV2 withDataType(int dataType) {
        this.dataType = (short) dataType;
        return this;
    }

    private CHeaderMessageV2 withData(byte[] data) {
        this.data = data;
        return this;
    }

    private CHeaderMessageV2 withAutoFlowNo() {
        this.flowNo = FlownoGen.getAndIncrement();
        return this;
    }

    private CHeaderMessageV2 withCloudSrc() {
        this.withSrcType(AddressType.CLOUD);
        this.withSrc(new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF });
        return this;

    }

    private CHeaderMessageV2 withSrc(byte[] srcbytes) {
        if (srcbytes == null || srcbytes.length != 4) {
            throw new IllegalArgumentException("dest not right format.");
        }
        this.src = srcbytes;
        return this;
    }

    private CHeaderMessageV2 withSrcType(AddressType type) {
        this.srcType = (short) type.getNumber();
        return this;
    }

    private CHeaderMessageV2 withAllDest() {
        this.withDstType(AddressType.ALL);
        this.withDest(new byte[] { 0x00, 0x00, 0x00, 0x00 });
        return this;
    }

    private CHeaderMessageV2 withDest(byte[] dest) {
        if (dest == null || dest.length != 4) {
            throw new IllegalArgumentException("dest not right format.");
        }
        this.dest = dest;
        return this;
    }

    private CHeaderMessageV2 withDstType(AddressType type) {
        this.dstType = (short) type.getNumber();
        return this;
    }

}
