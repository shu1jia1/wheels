package com.st.modules.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.eventbus.Subscribe;
import com.st.common.message.STProtoMessage;
import com.st.common.message.entity.STCommon.Address;
import com.st.common.message.entity.StMessage.LoginResponse;
import com.st.modules.message.event.LoginRequetReceiveEvent;
import com.st.site.device.service.DeviceService;

public class LoginReqEventtHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoginReqEventtHandler.class);

    @Autowired
    private DeviceService deviceService;

    @Subscribe
    public void handleLoginRequetReceiveEvent(final LoginRequetReceiveEvent loginEvent) {
        final STProtoMessage msg = loginEvent.getStMessage();
        Address src = msg.getSrc();
        logger.info("recevie Login from {},details {}.", src.toString(), loginEvent.getInnerPayload());
        final boolean success = deviceService.deviceLogIn(loginEvent.getInnerPayload(),loginEvent.getChannel().remoteAddress().toString());
        loginEvent.getChannel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                loginEvent.getChannel().writeAndFlush(makeLoginResponse(msg,success));
            }
        });
    }

    private STProtoMessage makeLoginResponse(STProtoMessage request, boolean success) {
        LoginResponse resp = LoginResponse.newBuilder().setResult(success).build();
        return STProtoMessage.newBuilder().withRequestMessage(request).withPayload(resp).build();
    }

}
