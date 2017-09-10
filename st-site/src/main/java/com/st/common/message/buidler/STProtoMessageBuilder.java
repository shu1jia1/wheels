package com.st.common.message.buidler;

import org.apache.commons.lang.StringUtils;

import com.github.shu1jia1.common.utils.id.IDGenerator;
import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;
import com.st.common.message.STProtoMessage;
import com.st.common.message.entity.STCommon.Address;
import com.st.common.message.entity.STCommon.AddressType;
import com.st.common.message.entity.STCommon.CmdCode;
import com.st.common.message.entity.STCommon.MessageHeader;
import com.st.common.message.entity.STCommon.STMessage;

/**
 * STprotoMessage构造器
 * <p>文件名称: STProtoMessageBuilder.java/p>
 * <p>文件描述: </p>
 * @version 1.0
 * @author  lov
 */
public class STProtoMessageBuilder {
    private byte[] payload;
    private CmdCode cmdCode;
    private Address src;
    private Address dst;
    private String messageId;
    private String correlationId;
    private Long flowNo;

    public STProtoMessageBuilder withRequestMessage(com.st.common.message.STProtoMessage request) {
        this.correlationId = request.getCorrelationId();
        this.dst = request.getSrc();
        this.src = request.getDest();
        return this;
    }

    public STProtoMessageBuilder withPayload(byte[] payload) {
        this.payload = payload;
        return this;
    }

    public STProtoMessageBuilder withPayload(MessageLite protoPayload) {
        this.payload = protoPayload.toByteArray();
        this.cmdCode = CmdCode.valueOf("CMD_" + protoPayload.getClass().getSimpleName());
        return this;
    }

    public STProtoMessageBuilder setCmdCode(CmdCode cmdCode) {
        this.cmdCode = cmdCode;
        return this;
    }   

    public STProtoMessageBuilder setFlowNo(long flowNo) {
        this.flowNo = flowNo;
        return this;
    }

    public STProtoMessageBuilder withSrc(Address src) {
        this.src = src;
        return this;
    }

    public STProtoMessageBuilder withDst(Address dst) {
        this.dst = dst;
        return this;
    }

    public STProtoMessageBuilder withMachineDst(String machineId) {
        this.dst = Address.newBuilder().setDestType(AddressType.MACHINE).setIdentify(machineId).build();
        return this;
    }

    public STProtoMessageBuilder withMessageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public STProtoMessageBuilder withCorrelationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    public STProtoMessage build() {
        // checkNotNull(cmdCode);
        // checkNotNull(dst);
        MessageHeader.Builder headerBuilder = MessageHeader.newBuilder().setCmdCode(cmdCode).setMessageId(messageId);
        if (StringUtils.isEmpty(messageId)) {
            if (flowNo != null) {
                messageId = dst.getIdentify() + "_" + flowNo;
            } else {
                messageId = IDGenerator.getInstance().generate();
            }
        }
        headerBuilder.setCorrelationId(messageId);
        if (StringUtils.isNotEmpty(correlationId)) {
            headerBuilder.setCorrelationId(correlationId);
        } else {
            headerBuilder.setCorrelationId(messageId);
        }

        if (src == null) {
            src = Address.newBuilder().setDestType(AddressType.CLOUD).setIdentify("01").build();
        }

        headerBuilder.setDest(dst).setSrc(src);

        STMessage.Builder msgBuilder = STMessage.newBuilder().setHeader(headerBuilder);
        if (payload != null) {
            msgBuilder.setPayload(ByteString.copyFrom(payload));
        }
        return new STProtoMessage(msgBuilder.build());
    }

}
