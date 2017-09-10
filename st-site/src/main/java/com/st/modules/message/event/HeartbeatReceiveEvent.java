package com.st.modules.message.event;

import org.springframework.stereotype.Service;

import com.st.common.message.STProtoMessage;
import com.st.common.message.entity.CHeaderMessageV2;
import com.st.common.message.entity.StMessage.LoginRequet;

import io.netty.channel.Channel;

public class HeartbeatReceiveEvent extends MessageReceiveEvent<LoginRequet> {
    public HeartbeatReceiveEvent(Channel channel, STProtoMessage stMessage, Class<LoginRequet> clazz) {
        super(channel, stMessage, clazz);
    }

    public HeartbeatReceiveEvent(Channel channel, CHeaderMessageV2 cMessage) {
        super(channel, cMessage);
    }
}