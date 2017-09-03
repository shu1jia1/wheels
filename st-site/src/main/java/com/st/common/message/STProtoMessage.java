package com.st.common.message;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.st.common.message.buidler.STProtoMessageBuilder;
import com.st.common.message.entity.CHeaderMessage;
import com.st.common.message.entity.STCommon.Address;
import com.st.common.message.entity.STCommon.CmdCode;
import com.st.common.message.entity.STCommon.MessageHeader;
import com.st.common.message.entity.STCommon.STMessage;

public class STProtoMessage {
    private STMessage stMessage;
    private MessageHeader header;
    private byte[] payload;
    private CHeaderMessage cmessage;

    public static STProtoMessageBuilder newBuilder() {
        return new STProtoMessageBuilder();
    }

    public STProtoMessage(STMessage stMessage) {
        this.stMessage = stMessage;
        this.header = stMessage.getHeader();
        this.payload = stMessage.getPayload().toByteArray();
    }

    public CmdCode getCmdCode() {
        return header.getCmdCode();
    }

    public boolean isHeartBeat() {
        return getCmdCode() == CmdCode.CMD_HeartBeat;
    }

    public <T extends MessageLite> T getInnerPayload(Class<T> clazz) {
        try {
            return (T) clazz.newInstance().getParserForType().parseFrom(payload);
        } catch (InvalidProtocolBufferException | InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

    public STMessage getStMessage() {
        return stMessage;
    }

    public void setStMessage(STMessage stMessage) {
        this.stMessage = stMessage;
    }

    public byte[] getBytes() {
        return stMessage.toByteArray();
    }

    public Address getDest() {
        return header.getDest();
    }

}
