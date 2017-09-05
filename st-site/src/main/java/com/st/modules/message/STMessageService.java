package com.st.modules.message;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.Subscribe;
import com.st.common.event.MessageEventBus;
import com.st.common.message.STProtoMessage;
import com.st.common.message.entity.STCommon.CmdCode;
import com.st.common.message.entity.StMessage.LoginRequet;
import com.st.modules.device.DeviceChannels;
import com.st.modules.message.event.HeartbeatReceiveEvent;
import com.st.modules.message.event.LoginRequetReceiveEvent;
import com.st.modules.message.event.MessageSendEvent;
import com.st.modules.message.event.UnknownMessageReceiveEvent;

import io.netty.channel.Channel;

/**
 * 处理消息发送和接收消息的分发
 * 发送:
 * 通过eventbus.post(MessageSendEvent)
 * 
 * 接收订阅具体的消息类型()
 * 
 * <p>文件名称: MessageService.java/p>
 * <p>文件描述: </p>
 * @version 1.0
 * @author  lov
 */
@Service("stMessageService")
public class STMessageService {
    private static final Logger logger = LoggerFactory.getLogger(STMessageService.class);

    @Resource(name = "stEventBus")
    private MessageEventBus eventBus;

    @Autowired
    private DeviceChannels deviceChannels;

    /**
     * 从这里把消息通过EventBus分发出去
     * @param channel
     * @param stmessage
     * @throws Exception
     */
    public void handlerMessage(Channel channel, STProtoMessage stMessage) throws Exception {
        CmdCode cmdCode = stMessage.getCmdCode();
        switch (cmdCode) {
        case CMD_LoginRequet:
            eventBus.post(new LoginRequetReceiveEvent(channel, stMessage, LoginRequet.class));
            break;
        case CMD_HeartBeat:
            eventBus.post(new HeartbeatReceiveEvent(channel, stMessage, LoginRequet.class));
            break;
        default:
            logger.info("receive cmd {}. unknown", stMessage.getSimpleInfo());
            eventBus.post(new UnknownMessageReceiveEvent(channel, stMessage));
            // returnMessage(channel, result, new Exception("unknown message."),
            // "1005");
            return;
        }
    }

    @Subscribe
    public void respMessage(MessageSendEvent sendEvent) {
        if (sendEvent == null) {
            return;
        }
        if (sendEvent.getStMessage() != null) {
            Channel destChannel = deviceChannels.findChannel(sendEvent.getStMessage().getDest());
            destChannel.writeAndFlush(sendEvent.getStMessage().getStMessage());
        }
    }
}
