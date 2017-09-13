package com.st.modules.message;

import com.st.common.message.entity.CHeaderMessageV2;

/**
 * 消息发送接口
 * <p>文件名称: IMessageSender.java/p>
 * <p>文件描述: </p>
 * @version 1.0
 * @author  lov
 */
public interface IMessageSender {
    /**
     * 发送CheaderMesasge
     * @param sendMessage
     * @return
     */
    public boolean sendMessage(final CHeaderMessageV2 sendMessage);
}
