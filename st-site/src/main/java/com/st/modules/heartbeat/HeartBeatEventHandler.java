package com.st.modules.heartbeat;

import java.net.SocketAddress;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.Subscribe;
import com.st.common.message.entity.CHeaderMessageV2;
import com.st.modules.message.event.HeartbeatReceiveEvent;
import com.st.site.device.service.DeviceService;

@Service("heartBeatEventHandler")
public class HeartBeatEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(HeartBeatEventHandler.class);

    @Resource(name = "deviceService")
    private DeviceService deviceService;

    @Subscribe
    public void handlerHeartBeat(HeartbeatReceiveEvent heartEvent) {
        // STProtoMessage msg = heartEvent.getStMessage();
        // Address src = msg.getSrc();
        CHeaderMessageV2 loginMessage = heartEvent.getcMessage();
        String devno = loginMessage.getSrcStr();
        SocketAddress remoteAddr = heartEvent.getChannel().remoteAddress();
        logger.info("recevie {} heart beat from {}", devno, remoteAddr);
    }

}
