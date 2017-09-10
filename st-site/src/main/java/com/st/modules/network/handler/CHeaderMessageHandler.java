package com.st.modules.network.handler;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.st.common.message.entity.CHeaderMessageV2;
import com.st.modules.device.DeviceChannels;
import com.st.modules.message.STMessageService;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;

//@Component
//@Qualifier("websocketHandler")
//@ChannelHandler.Sharable

@Sharable
@Service("cMessageHandler")
public class CHeaderMessageHandler extends SimpleChannelInboundHandler<CHeaderMessageV2> {
    private static final Logger logger = LoggerFactory.getLogger(CHeaderMessageHandler.class);

    // @Autowired
    // private MessageService messageService;
    // @Autowired
    // private LoginService loginService;

    @Autowired
    private DeviceChannels deviceChannels;

    @Resource(name = "stMessageService")
    private STMessageService stMessageService;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // closed on shutdown.
        deviceChannels.add(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CHeaderMessageV2 msg) throws Exception {
        stMessageService.handlerMessage(ctx.channel(), msg);
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
        logger.info(ctx.channel() + " error occoured. close.", cause);
        super.exceptionCaught(ctx, cause);
    }

}