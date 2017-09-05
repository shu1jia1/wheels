package com.st.modules.message.event;

import com.google.protobuf.MessageLite;
import com.st.common.message.STProtoMessage;

import io.netty.channel.Channel;

public class MessageReceiveEvent<T extends MessageLite> {
    private Channel channel;
    private STProtoMessage stMessage;
    private T innerMessage;
    private Class<T> clazz;

    public MessageReceiveEvent(Channel channel, STProtoMessage stMessage) {
        super();
        this.channel = channel;
        this.stMessage = stMessage;
        // this.innerMessage = innerMessage;
    }

    public MessageReceiveEvent(Channel channel, STProtoMessage stMessage, Class<T> clazz) {
        super();
        this.channel = channel;
        this.stMessage = stMessage;
        this.clazz = clazz;
        // this.innerMessage = innerMessage;
    }

    public T getInnerPayload() {
        return (T) stMessage.getInnerPayload(clazz);
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public STProtoMessage getStMessage() {
        return stMessage;
    }

    public void setStMessage(STProtoMessage stMessage) {
        this.stMessage = stMessage;
    }

}