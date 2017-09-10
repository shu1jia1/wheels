package com.st.modules.network.init;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.st.modules.network.decoder.STCMessageDecoder;
import com.st.modules.network.decoder.STMessageFrameDecoder;
import com.st.modules.network.encoder.CMessageEncoder;
import com.st.modules.network.handler.CHeaderMessageHandler;
import com.st.modules.network.handler.STMessageHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

@Service("nettyChannelInitializer")
public class NettyChannelInitializer extends ChannelInitializer<Channel> {

    // private static final StringDecoder STRING_DECODER = new
    // StringDecoder(CharsetUtil.UTF_8);
    // private static final StringEncoder STRING_ENCODER = new
    // StringEncoder(CharsetUtil.UTF_8);

    // @Value("${netty.server.transfer.type}")
    // private String transferType;
    // @Value("${netty.server.transfer.maxContentLength}")
    // private int transferMaxContentLength;
    // @Value("${netty.server.transfer.websocket.path}")
    // private String transferWebsocketPath;
    // @Value("${netty.server.transfer.websocket.subProtocol}")
    // private String transferWebsocketSubProtocol;
    // @Value("${netty.server.transfer.websocket.allowExtensions}")
    // private boolean transferWebsocketAllowExtensions;
    // @Value("${netty.server.log.level.pipeline}")
    @Value("${tcp.log.level}")
    private String logLevelPipeline = LogLevel.INFO.toString();
    //
    // @Resource(name = "stMessageHandler")
    // STMessageHandler sTMessageHandler;
    //

    @Resource(name = "cMessageHandler")
    private CHeaderMessageHandler cMessageHandler;
    //
    // @Autowired
    // @Qualifier("websocketHandler")
    // private ChannelInboundHandlerAdapter websocketHandler;
    // @Autowired
    // @Qualifier("jsonHandler")
    // private ChannelInboundHandlerAdapter jsonHandler;

    /**
     * 채널 파이프라인 설정.
     * Netty.Server.Configuration.NettyServerConfiguration 에서 등록한 Bean 을 이용해 사용자의 통신을 처리할 Handler 도 등록.
     * Netty.Server.Handler.JsonHandler 에서 실제 사용자 요청 처리.
     *
     * @param channel
     * @throws Exception
     */
    @Override
    protected void initChannel(Channel channel) throws Exception {

        ChannelPipeline channelPipeline = channel.pipeline();
        channelPipeline.addLast(new LoggingHandler(LogLevel.valueOf(logLevelPipeline)))
                .addLast("frameDecoder", new STMessageFrameDecoder(65536, 4, 2, -6, 0)) //长度开始为4,长度2字节,长度因为算在了总数里，需要去掉
                .addLast("cDecoder", new STCMessageDecoder()) //
                .addLast("cEncode", new CMessageEncoder())//
                .addLast(cMessageHandler); //

        // .addLast("frameDecoder", new ProtobufVarint32FrameDecoder())
        // .addLast("frameDecoder", new
        // LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 4))
        // .addLast("pbDecoder", new
        // ProtobufDecoder(STMessage.getDefaultInstance()))
        // .addLast("frameEncode", new ProtobufVarint32LengthFieldPrepender())
        // .addLast("frameEncode", new STMessageLengthFieldPrepender()) //
        // .addLast("frameEncode", new LengthFieldPrepender(4)) //
        // .addLast("protobufEncode", new
        // ProtobufEncoder()).addLast(sTMessageHandler); //
        // .addLast(new ReaderIdleHandler(60)).addLast(serverHandler);
        // switch (transferType) {
        //
        // case "websocket":
        //
        // channelPipeline
        // .addLast(new HttpServerCodec())
        // .addLast(new HttpObjectAggregator(65536))
        // .addLast(new WebSocketServerCompressionHandler())
        // .addLast(new WebSocketServerProtocolHandler(transferWebsocketPath,
        // transferWebsocketSubProtocol, transferWebsocketAllowExtensions))
        // .addLast(new LoggingHandler(LogLevel.valueOf(logLevelPipeline)))
        // .addLast(websocketHandler);
        //
        // case "tcp":
        // default:

        // channelPipeline.addLast(new
        // (Integer.MAX_VALUE)).addLast(STRING_DECODER)
        // .addLast(STRING_ENCODER).addLast(new
        // LoggingHandler(LogLevel.valueOf(logLevelPipeline)))
        // .addLast(jsonHandler);
    }

}