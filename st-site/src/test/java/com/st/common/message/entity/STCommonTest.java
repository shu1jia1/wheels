package com.st.common.message.entity;

import org.junit.Test;

import com.github.shu1jia1.common.utils.string.PrintUtil;
import com.st.common.message.STProtoMessage;
import com.st.common.message.entity.STCommon.Address;
import com.st.common.message.entity.STCommon.AddressType;
import com.st.common.message.entity.STCommon.CmdCode;
import com.st.common.message.entity.StMessage.LoginRequet;

public class STCommonTest {

    @Test
    public void test() {
        STProtoMessage proto = STProtoMessage.newBuilder().setCmdCode(CmdCode.CMD_ForceLogin).withMessageId("messageId")
                .withCorrelationId("correlationId").withMachineDst("Gd10")
                .withPayload(new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04 }).build();
        byte[] data = proto.getStMessage().toByteArray();
        System.out.println(PrintUtil.toHexStr(data));
    }

    @Test
    public void testWithPayload() {
        STProtoMessage proto = STProtoMessage.newBuilder().setCmdCode(CmdCode.CMD_ForceLogin).withMessageId("messageId")
                .withCorrelationId("correlationId").withMachineDst("Gd10")
                .withPayload(LoginRequet.newBuilder().setDevno("123").setType(AddressType.MACHINE_VALUE).build())
                .build();
        System.out.println(proto.getStMessage());
        byte[] data = proto.getStMessage().toByteArray();
        System.out.println(PrintUtil.toHexStr(data));
    }

}
