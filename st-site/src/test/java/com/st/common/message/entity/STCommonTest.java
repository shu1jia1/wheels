package com.st.common.message.entity;

import org.junit.Test;
import org.springframework.security.crypto.codec.Hex;

import com.github.shu1jia1.common.utils.string.PrintUtil;
import com.google.protobuf.InvalidProtocolBufferException;
import com.st.common.message.STProtoMessage;
import com.st.common.message.entity.STCommon.AddressType;
import com.st.common.message.entity.STCommon.CmdCode;
import com.st.common.message.entity.STCommon.MessageHeader;
import com.st.common.message.entity.STCommon.STMessage;
import com.st.common.message.entity.StMessage.LoginRequet;
import com.st.modules.network.handler.STMessageHandler;

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
        STMessage stMst;
        try {
            stMst = STMessage.getDefaultInstance().parseFrom(data);
            System.out.println(stMst);
        } catch (InvalidProtocolBufferException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testDecoder() throws InvalidProtocolBufferException {
        // String str ="[0A, 2E, 0A, 09, 6D, 65, 73, 73, 61, 67, 65, 49, 64, 12,
        // 0D, 63, 6F, 72, 72, 65, 6C, 61, 74, 69, 6F, 6E, 49, 64, 20, 02, 2A,
        // 08, 08, 02, 12, 04, 47, 64, 31, 30, 32, 06, 08, 01, 12, 02, 30, 31,
        // 12, 07, 0A, 03, 31, 32, 33, 10, 02]".replaceAll("[\\[,\\s\\]]", "");
        String str = "0a 16 65 6e 63 6f 64 65 6c 67 6e 6d 73 67 2e 6d 65 73 73 61 67 65 49 64 12 1a 65 6e 63 6f 64 65 6c 67 6e 6d 73 67 2e 63 6f 72 72 65 6c 61 74 69 6f 6e 49 64 18 00 20 01 2a 10 08 03 12 0c 64 65 73 74 6c 78 6d 31 32 33 34 35 32 10 08 04 12 0c 64 65 73 74 6c 78 6d 31 32 33 34 35"
                .replaceAll("[\\[,\\s\\]]", "");
        System.out.println(str);
        byte[] data = Hex.decode(str);
        System.out.println(PrintUtil.to0xHexStr(data));

        MessageHeader message = MessageHeader.getDefaultInstance().parseFrom(data);
        System.out.println(message);
    }

}
