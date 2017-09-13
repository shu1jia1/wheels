package com.st.modules.message.eventhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.Subscribe;
import com.st.common.message.entity.CHeaderMessageV2;
import com.st.common.message.entity.STCommon.StatusCode;
import com.st.modules.device.DeviceChannels;
import com.st.modules.message.IMessageSender;
import com.st.modules.message.event.MessageSendEvent;
import com.st.modules.message.event.MessageTransferEvent;

import io.netty.channel.Channel;

/**
 * 消息发送器，同时负责MessageSendEvent和MessageTransEvent处理
 * <p>文件名称: MessageSender.java/p>
 * <p>文件描述: </p>
 * @version 1.0
 * @author  lov
 */
@Service("messageSender")
public class MessageSender implements IMessageSender {
    private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    @Autowired
    private DeviceChannels deviceChannels;

    @Override
    public boolean sendMessage(final CHeaderMessageV2 sendMessage) {
        final Channel destChannel = deviceChannels.findChannel(sendMessage.getDestStr());
        if (destChannel == null) {
            logger.info("Can't find channel for message {} ", sendMessage.getSimpleInfo());
            return false;
        } else {
            logger.info("Find dev {}  channel {} ", sendMessage.getDestAddr(), destChannel);
        }
        destChannel.eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                destChannel.writeAndFlush(sendMessage);
            }
        });
        return true;
    }

    @Subscribe
    public void handSendEvent(final MessageSendEvent sendEvent) {
        if (sendEvent == null) {
            return;
        }
        if (sendEvent.getcMessage() != null) {
            sendMessage(sendEvent.getcMessage());
        }
        // if (sendEvent.getStMessage() != null) {
        // destChannel.writeAndFlush(sendEvent.getStMessage().getStMessage());
        // }
    }

    @Subscribe
    public void handleTransEvent(final MessageTransferEvent msgTransEvent) {
        if (msgTransEvent == null || msgTransEvent.getcMessage() == null) {
            logger.info("recevie null trans event. ignore. {}", msgTransEvent);
            return;
        }
        CHeaderMessageV2 msg = msgTransEvent.getcMessage();
        if (!sendMessage(msg)) {
            // 如果发送失败，回应一个消息
            logger.info("Trans failed,notify src {} , {}", msg.getSrcStr(), msg.getSimpleInfo());
            final CHeaderMessageV2 resp = msg.makeResponse(false, new byte[0]).withStatusCode(StatusCode.SEND_FAILED)
                    .build();
            msgTransEvent.getChannel().eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    msgTransEvent.getChannel().writeAndFlush(resp);
                }
            });
        }
        logger.info("Trans {}", msg.getSimpleInfo());

    }

}
