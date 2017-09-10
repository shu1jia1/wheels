package com.st.common.message.entity;

import static org.junit.Assert.*;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class CHeaderMessageV2Test {

    @Test
    public void test() {
        ByteBuf byteBuf = Unpooled.buffer(12);
        byteBuf.writeInt(1);
        byteBuf.writeInt(2);
        byteBuf.writeInt(3);
        
        System.out.println(byteBuf.readInt());
        System.out.println(byteBuf.readInt());
        System.out.println(byteBuf.readInt());
    }

}
