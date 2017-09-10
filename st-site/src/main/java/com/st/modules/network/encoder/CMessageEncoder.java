package com.st.modules.network.encoder;

import static io.netty.buffer.Unpooled.wrappedBuffer;

import java.util.List;

import com.st.common.message.entity.CHeaderMessageV2;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

@Sharable
public class CMessageEncoder extends MessageToMessageEncoder<CHeaderMessageV2> {

    @Override
    protected void encode(ChannelHandlerContext ctx, CHeaderMessageV2 msg, List<Object> out) throws Exception {
            out.add(wrappedBuffer(msg.toBytes()));
            return;
    }

}
