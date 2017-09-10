package com.st.modules.login;

import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.Subscribe;
import com.st.common.message.STProtoMessage;
import com.st.common.message.entity.CHeaderMessageV2;
import com.st.common.message.entity.StMessage.LoginResponse;
import com.st.modules.message.event.LoginRequetReceiveEvent;
import com.st.site.device.entity.DeviceInfo;
import com.st.site.device.service.DeviceService;

@Service("loginReqEventtHandler")
public class LoginReqEventtHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoginReqEventtHandler.class);

    @Autowired
    private DeviceService deviceService;

    @Subscribe
    public void handleLoginRequetReceiveEvent(final LoginRequetReceiveEvent loginEvent) {
        // final STProtoMessage msg = loginEvent.getStMessage();
        // Address src = msg.getSrc();
        final CHeaderMessageV2 loginMessage = loginEvent.getcMessage();
        String devNo = loginMessage.getSrcStr();

        logger.info("recevie Login from {},details {}.", devNo.toString(), loginMessage.getSimpleInfo());
        // final boolean success =
        // deviceService.deviceLogIn(loginEvent.getInnerPayload(),loginEvent.getChannel().remoteAddress().toString());
        String version = "";
        DeviceInfo device = new DeviceInfo(devNo, loginMessage.getSrcType(), version);
        device.setName(devNo);
        SocketAddress remoteAddress = loginEvent.getChannel().remoteAddress();
        final boolean success = deviceService.deviceLogIn(device, remoteAddress.toString());

        loginEvent.getChannel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                loginEvent.getChannel().writeAndFlush(loginMessage.makeResponse(success, new byte[0]));
            }
        });
    }

    private STProtoMessage makeLoginResponse(STProtoMessage request, boolean success) {
        LoginResponse resp = LoginResponse.newBuilder().setResult(success).build();
        return STProtoMessage.newBuilder().withRequestMessage(request).withPayload(resp).build();
    }

}
