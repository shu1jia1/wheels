package com.st.modules.message;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.Subscribe;
import com.google.protobuf.MessageLite;
import com.st.common.event.MessageEventBus;
import com.st.common.message.STProtoMessage;
import com.st.common.message.entity.STCommon.CmdCode;
import com.st.common.message.entity.STCommon.STMessage;
import com.st.common.message.entity.StMessage.LoginRequet;
import com.st.modules.device.DeviceChannels;

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
@Service("messageService")
public class MessageService {
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
    public void handlerMessage(Channel channel, STProtoMessage stmessage) throws Exception {
        CmdCode cmdCode = stmessage.getCmdCode();
        switch (cmdCode) {
        case CMD_LoginRequet:
            eventBus.post(stmessage.getInnerPayload(LoginRequet.class));
            break;
        case CMD_HeartBeat:

            break;
        default:
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

    public class MessageSendEvent {
        private Channel channel;
        private STProtoMessage stProtoMessage;
        private Object message;

        public MessageSendEvent(STProtoMessage stProtoMessage) {
            this.stProtoMessage = stProtoMessage;
        }

        public STProtoMessage getStMessage() {
            return stProtoMessage;
        }

        public Object getMessage() {
            return message;
        }

        public void setMessage(Object message) {
            this.message = message;
        }

        public Channel getChannel() {
            return channel;
        }

        public void setChannel(Channel channel) {
            this.channel = channel;
        }

        public STProtoMessage getStProtoMessage() {
            return stProtoMessage;
        }

        public void setStProtoMessage(STProtoMessage stProtoMessage) {
            this.stProtoMessage = stProtoMessage;
        }

    }

    public class MessageReceiveEvent<T extends MessageLite> {
        private Channel channel;
        private STMessage stMessage;
        private T innerMessage;

        public MessageReceiveEvent(Channel channel, STMessage stMessage, T innerMessage) {
            super();
            this.channel = channel;
            this.stMessage = stMessage;
            this.innerMessage = innerMessage;
        }

        public T getInnerMessage() {
            return innerMessage;
        }
    }

    public class LoginRequetReceiveEvent extends MessageReceiveEvent<LoginRequet> {
        private LoginRequet innerMessage;

        public LoginRequetReceiveEvent(Channel channel, STMessage stMessage, LoginRequet innerMessage) {
            super(channel, stMessage, innerMessage);
        }
    }

}
