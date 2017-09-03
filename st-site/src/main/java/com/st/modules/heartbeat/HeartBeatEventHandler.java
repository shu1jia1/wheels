package com.st.modules.heartbeat;

import com.google.common.eventbus.Subscribe;
import com.st.common.message.entity.StMessage.LoginRequet;

public class HeartBeatEventHandler {

    @Subscribe
    public void handlerHeartBeat(String deviceNo) {

    }

    @Subscribe
    public void handlerLogin(LoginRequet request) {
        

    }

}
