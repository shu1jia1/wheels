package com.st.modules.message.event;

import com.st.common.message.STProtoMessage;
import com.st.common.message.entity.CHeaderMessageV2;
import com.st.common.message.entity.StMessage.LoginRequet;

import io.netty.channel.Channel;

/**
 * 消息转发事件
 * <p>文件名称: MessageTransferEvent.java/p>
 * <p>文件描述: </p>
 * @version 1.0
 * @author  lov
 */
public class MessageTransferEvent extends MessageReceiveEvent<LoginRequet> {
    public MessageTransferEvent(Channel channel, STProtoMessage stMessage, Class<LoginRequet> clazz) {
        super(channel, stMessage, clazz);
    }

    public MessageTransferEvent(Channel channel, CHeaderMessageV2 cMessage) {
        super(channel, cMessage);
    }
}
