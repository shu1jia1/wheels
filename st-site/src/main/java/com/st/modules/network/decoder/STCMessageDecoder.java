package com.st.modules.network.decoder;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.shu1jia1.common.utils.string.PrintUtil;
import com.st.common.message.STProtoMessage;
import com.st.common.message.entity.CHeaderMessageV2;
import com.st.common.message.entity.STCommon.Address;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

@Sharable
public class STCMessageDecoder extends MessageToMessageDecoder<ByteBuf> {
    private static final Logger logger = LoggerFactory.getLogger(STCMessageDecoder.class);

    public STCMessageDecoder() {
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        final byte[] array;
        final int offset;
        final int length = msg.readableBytes();
        if (msg.hasArray()) {
            array = msg.array();
            offset = msg.arrayOffset() + msg.readerIndex();
        } else {
            array = new byte[length];
            msg.getBytes(msg.readerIndex(), array, 0, length);
            offset = 0;
        }
        // ByteBuf byteBuf = Unpooled.buffer(length);
        CHeaderMessageV2 cMessage;
        try {
            ByteBuf copiedBuffer = Unpooled.copiedBuffer(msg);
            cMessage = new CHeaderMessageV2().formBytes(copiedBuffer);
            out.add(cMessage);
        } catch (Exception e) {
            logger.info("exception discard bytes:{}", e.getCause());
            logger.debug("exception discard bytes:" + ByteBufUtil.hexDump(msg), e);
        }
    }


}