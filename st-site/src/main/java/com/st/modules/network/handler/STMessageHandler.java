package com.st.modules.network.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.asm.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParseException;
import com.st.common.message.entity.STCommon.STMessage;
import com.st.modules.device.DeviceChannels;
import com.st.modules.login.LoginService;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

//@Component
//@Qualifier("websocketHandler")
//@ChannelHandler.Sharable

@Service("stMessageHandler")
public class STMessageHandler extends SimpleChannelInboundHandler<STMessage> {
    private static final Logger logger = LoggerFactory.getLogger(STMessageHandler.class);

    // @Autowired
    // private MessageService messageService;
    // @Autowired
    // private LoginService loginService;

    @Autowired
    DeviceChannels deviceChannels;
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // closed on shutdown.
        deviceChannels.add(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, STMessage msg) throws Exception {
        // TODO Auto-generated method stub

    }

    // @Override
    // protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame
    // frame) throws Exception {
    // System.out.println("#######################################################
    // ");
    // Map<String, Object> result = new HashMap<>();
    //
    // // 접속자 채널 정보(연결 정보)
    // Channel channel = ctx.channel();
    //
    // if (!(frame instanceof TextWebSocketFrame))
    // throw new UnsupportedOperationException("unsupported frame type : " +
    // frame.getClass().getName());
    //
    // // 전송된 내용을 JSON 개체로 변환
    // Map<String, Object> data;
    // try {
    //
    // data = objectMapper.readValue(((TextWebSocketFrame) frame).text(),
    // new TypeReference<Map<String, Object>>() {
    // });
    //
    // } catch (JsonParseException | JsonMappingException e) {
    //
    // e.printStackTrace();
    //
    // messageService.returnMessage(channel, result, e, "1001");
    // return;
    //
    // }
    //
    // messageService.execute(channel, data, result);
    //
    // }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // loginService.removeUser(ctx.channel());
        ctx.close(); // TODO ??是否要关掉
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

}