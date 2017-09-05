package com.st.modules.message.event;

import com.st.common.message.STProtoMessage;
import com.st.common.message.entity.StMessage.LoginRequet;

import io.netty.channel.Channel;

public class HeartbeatReceiveEvent extends MessageReceiveEvent<LoginRequet> {
    public HeartbeatReceiveEvent(Channel channel, STProtoMessage stMessage, Class<LoginRequet> clazz) {
        super(channel, stMessage, clazz);
    }
}