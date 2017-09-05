package com.st.modules.message.event;

import com.st.common.message.STProtoMessage;

import io.netty.channel.Channel;

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
