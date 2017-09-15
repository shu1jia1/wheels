package com.st.modules.message;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;
import com.st.common.message.STProtoMessage;
import com.st.common.message.entity.CHeaderMessageV2;
import com.st.common.message.entity.STCommon.CmdCode;
import com.st.common.message.entity.StMessage.LoginRequet;
import com.st.modules.message.event.HeartbeatReceiveEvent;
import com.st.modules.message.event.LoginRequetReceiveEvent;
import com.st.modules.message.event.MessageTransferEvent;
import com.st.modules.message.eventhandler.DeviceOnlineNotify;
import com.st.modules.message.eventhandler.HeartBeatEventHandler;
import com.st.modules.message.eventhandler.LoginReqEventtHandler;
import com.st.modules.message.eventhandler.MessageSender;

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
public class STMessageService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(STMessageService.class);

    @Resource(name = "stEventBus")
    private EventBus eventBus;

    @Resource(name = "heartBeatEventHandler")
    private HeartBeatEventHandler heartBeatHanlder;

    @Resource(name = "loginReqEventtHandler")
    private LoginReqEventtHandler loginEventHandler;

    @Resource(name = "messageSender")
    private MessageSender messageSendAndTransHandler;

    @Resource(name = "deviceOnlineNotify")
    private DeviceOnlineNotify deviceOnlineNotify;

    @Override
    public void afterPropertiesSet() throws Exception {
        eventBus.register(heartBeatHanlder);
        eventBus.register(loginEventHandler);
        eventBus.register(messageSendAndTransHandler);
        eventBus.register(deviceOnlineNotify);
    }

    /**
     * 从这里把消息通过EventBus分发出去
     * @param channel
     * @param msg
     */
    public void handlerMessage(Channel channel, CHeaderMessageV2 msg) {
        logger.info("receive STmessage,{}", msg.getSimpleInfo());
        short cmd = msg.getCmd();
        switch (cmd) {
        case CmdCode.CMD_LoginRequet_VALUE:
            eventBus.post(new LoginRequetReceiveEvent(channel, msg));
            break;
        case CmdCode.CMD_HeartBeat_VALUE:
            eventBus.post(new HeartbeatReceiveEvent(channel, msg));
            break;
        default:
            logger.info("receive cmd {}. transfer", cmd);
            // this.respMessage(new MessageSendEvent(msg));
            eventBus.post(new MessageTransferEvent(channel, msg));
            return;
        }
    }

    /**
     * 从这里把消息通过EventBus分发出去
     * @param channel
     * @param stmessage
     * @throws Exception
     */
    @Deprecated
    public void handlerMessage(Channel channel, STProtoMessage stMessage) throws Exception {
        logger.info("receive STmessage,{}", stMessage.getSimpleInfo());
        CmdCode cmdCode = stMessage.getCmdCode();
        switch (cmdCode) {
        case CMD_LoginRequet:
            eventBus.post(new LoginRequetReceiveEvent(channel, stMessage, LoginRequet.class));
            break;
        case CMD_HeartBeat:
            eventBus.post(new HeartbeatReceiveEvent(channel, stMessage, LoginRequet.class));
            break;
        default:
            logger.info("receive cmd {}. transfer ", stMessage.getSimpleInfo());
            // eventBus.post(new UnknownMessageReceiveEvent(channel,
            // stMessage));
            // returnMessage(channel, result, new Exception("unknown message."),
            // "1005");
            return;
        }
    }

}
