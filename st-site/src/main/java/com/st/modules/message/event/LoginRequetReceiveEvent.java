package com.st.modules.message.event;

import com.st.common.message.STProtoMessage;
import com.st.common.message.entity.CHeaderMessageV2;
import com.st.common.message.entity.StMessage.LoginRequet;

import io.netty.channel.Channel;

public class LoginRequetReceiveEvent extends MessageReceiveEvent<LoginRequet> {
    public LoginRequetReceiveEvent(Channel channel, STProtoMessage stMessage, Class<LoginRequet> clazz) {
        super(channel, stMessage, clazz);
    }

    public LoginRequetReceiveEvent(Channel channel, CHeaderMessageV2 cMessage) {
        super(channel, cMessage);
    }
}
