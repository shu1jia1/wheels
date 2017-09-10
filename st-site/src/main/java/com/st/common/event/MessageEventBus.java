package com.st.common.event;

import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;

@Service("stEventBus")
public class MessageEventBus extends EventBus {
    public MessageEventBus() {
        super("STMessageEventBus");
    }

}
