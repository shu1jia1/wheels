package com.st.modules.retransfer;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.Subscribe;
import com.st.common.event.MessageEventBus;
import com.st.common.message.STProtoMessage;
import com.st.common.message.entity.CHeaderMessageV2;
import com.st.common.message.entity.StMessage.LoginResponse;
import com.st.modules.message.event.MessageSendEvent;
import com.st.modules.message.event.UnknownMessageReceiveEvent;

@Service("retransferEventtHandler")
public class RetransferEventtHandler {
    private static final Logger logger = LoggerFactory.getLogger(RetransferEventtHandler.class);
    @Resource(name = "stEventBus")
    private MessageEventBus eventBus;

    @Subscribe
    public void handleLoginRequetReceiveEvent(final UnknownMessageReceiveEvent messageReceiveEvent) {
        // final STProtoMessage msg = loginEvent.getStMessage();
        // Address src = msg.getSrc();
        final CHeaderMessageV2 transMessage = messageReceiveEvent.getcMessage();
        String devNo = transMessage.getSrcStr();

        logger.info("recevie trans from {},dest {}." + transMessage.getDestStr(), devNo.toString(),
                transMessage.getSimpleInfo());
        eventBus.post(new MessageSendEvent(transMessage));
    }

    private STProtoMessage makeLoginResponse(STProtoMessage request, boolean success) {
        LoginResponse resp = LoginResponse.newBuilder().setResult(success).build();
        return STProtoMessage.newBuilder().withRequestMessage(request).withPayload(resp).build();
    }

}
