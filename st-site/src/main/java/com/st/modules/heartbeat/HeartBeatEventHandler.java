package com.st.modules.heartbeat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.st.common.message.STProtoMessage;
import com.st.common.message.entity.STCommon.Address;
import com.st.common.message.entity.StMessage.LoginRequet;
import com.st.modules.message.event.HeartbeatReceiveEvent;

public class HeartBeatEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(HeartBeatEventHandler.class);
    
    @Subscribe
    public void handlerHeartBeat(HeartbeatReceiveEvent heartEvent) {
        STProtoMessage msg = heartEvent.getStMessage();
        Address src = msg.getSrc();
        logger.info("recevie heart beat from {}", src.toString());
    }

    @Subscribe
    public void handlerLogin(LoginRequet request) {

    }

}
