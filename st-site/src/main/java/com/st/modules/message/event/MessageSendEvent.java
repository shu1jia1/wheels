package com.st.modules.message.event;

import com.st.common.message.STProtoMessage;
import com.st.common.message.entity.CHeaderMessageV2;

import io.netty.channel.Channel;

public class MessageSendEvent {
    private Channel channel;
    private STProtoMessage stProtoMessage;
    private CHeaderMessageV2 cMessage;

    public MessageSendEvent(STProtoMessage stProtoMessage) {
        this.stProtoMessage = stProtoMessage;
    }

    public MessageSendEvent(CHeaderMessageV2 cMessage) {
        this.cMessage = cMessage;
    }

    public STProtoMessage getStMessage() {
        return stProtoMessage;
    }

    public CHeaderMessageV2 getcMessage() {
        return cMessage;
    }

    public void setcMessage(CHeaderMessageV2 cMessage) {
        this.cMessage = cMessage;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public STProtoMessage getStProtoMessage() {
        return stProtoMessage;
    }

    public void setStProtoMessage(STProtoMessage stProtoMessage) {
        this.stProtoMessage = stProtoMessage;
    }
 

}
