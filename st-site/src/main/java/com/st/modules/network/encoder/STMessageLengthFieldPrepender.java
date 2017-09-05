package com.st.modules.network.encoder;

import java.nio.ByteOrder;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.internal.ObjectUtil;

/**
 * An encoder that prepends the length of the message.  The length value is
 * prepended as a binary form.
 * <p>
 * For example, <tt>{@link STMessageLengthFieldPrepender}(2)</tt> will encode the
 * following 12-bytes string:
 * <pre>
 * +----------------+
 * | "HELLO, WORLD" |
 * +----------------+
 * </pre>
 * into the following:
 * <pre>
 * +--------+----------------+
 * + 0x000C | "HELLO, WORLD" |
 * +--------+----------------+
 * </pre>
 * If you turned on the {@code lengthIncludesLengthFieldLength} flag in the
 * constructor, the encoded data would look like the following
 * (12 (original data) + 2 (prepended data) = 14 (0xE)):
 * <pre>
 * +--------+----------------+
 * + 0x000E | "HELLO, WORLD" |
 * +--------+----------------+
 * </pre>
 */
@Sharable
public class STMessageLengthFieldPrepender extends MessageToMessageEncoder<ByteBuf> {

    private static final byte[] TAG = new byte[] { (byte) 0xAA, 0x55 };
    private static final byte[] RET = new byte[] { 0x00, 0x00 };

    private final ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
    private final int lengthFieldLength = 2;
    private final boolean lengthIncludesLengthFieldLength = true;
    private final int lengthAdjustment = 4; // 长度之外有4个字节

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        int length = msg.readableBytes() + lengthAdjustment;
        if (lengthIncludesLengthFieldLength) {
            length += lengthFieldLength;
        }

        if (length < 0) {
            throw new IllegalArgumentException("Adjusted frame length (" + length + ") is less than zero");
        }
        out.add(ctx.alloc().buffer(TAG.length).order(byteOrder).writeBytes(TAG));
        switch (lengthFieldLength) {
        case 1:
            if (length >= 256) {
                throw new IllegalArgumentException("length does not fit into a byte: " + length);
            }
            out.add(ctx.alloc().buffer(1).order(byteOrder).writeByte((byte) length));
            break;
        case 2:
            if (length >= 65536) {
                throw new IllegalArgumentException("length does not fit into a short integer: " + length);
            }
            out.add(ctx.alloc().buffer(2).order(byteOrder).writeShort((short) length));
            break;
        case 3:
            if (length >= 16777216) {
                throw new IllegalArgumentException("length does not fit into a medium integer: " + length);
            }
            out.add(ctx.alloc().buffer(3).order(byteOrder).writeMedium(length));
            break;
        case 4:
            out.add(ctx.alloc().buffer(4).order(byteOrder).writeInt(length));
            break;
        case 8:
            out.add(ctx.alloc().buffer(8).order(byteOrder).writeLong(length));
            break;
        default:
            throw new Error("should not reach here");
        }

        out.add(ctx.alloc().buffer(RET.length).order(byteOrder).writeBytes(RET));
        out.add(msg.retain());
    }
}
