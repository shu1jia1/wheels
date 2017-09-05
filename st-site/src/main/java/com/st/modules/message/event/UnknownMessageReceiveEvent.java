package com.st.modules.message.event;

import com.google.protobuf.MessageLite;
import com.st.common.message.STProtoMessage;

import io.netty.channel.Channel;

public class UnknownMessageReceiveEvent extends MessageReceiveEvent<MessageLite> {

    public UnknownMessageReceiveEvent(Channel channel, STProtoMessage stMessage) {
        super(channel, stMessage);
    }
}
