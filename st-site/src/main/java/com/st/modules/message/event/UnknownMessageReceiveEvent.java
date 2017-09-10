package com.st.modules.message.event;

import com.google.protobuf.MessageLite;
import com.st.common.message.STProtoMessage;
import com.st.common.message.entity.CHeaderMessageV2;

import io.netty.channel.Channel;

public class UnknownMessageReceiveEvent extends MessageReceiveEvent<MessageLite> {

    public UnknownMessageReceiveEvent(Channel channel, STProtoMessage stMessage) {
        super(channel, stMessage);
    }

    public UnknownMessageReceiveEvent(Channel channel, CHeaderMessageV2 cMessage) {
        super(channel, cMessage);
    }
}
