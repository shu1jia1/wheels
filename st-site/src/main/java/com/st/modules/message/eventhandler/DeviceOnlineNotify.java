package com.st.modules.message.eventhandler;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.shu1jia1.common.utils.string.PrintUtil;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.st.common.message.entity.CHeaderMessageV2;
import com.st.common.message.entity.STCommon.Address;
import com.st.common.message.entity.STCommon.AddressType;
import com.st.common.message.entity.STCommon.CmdCode;
import com.st.modules.device.DeviceChannels;
import com.st.modules.message.STMessageService;
import com.st.modules.message.event.LoginRequetReceiveEvent;
import com.st.modules.message.event.MessageSendEvent;
import com.st.site.device.service.DeviceService;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

@Service("deviceOnlineNotify")
public class DeviceOnlineNotify {
    private static final Logger logger = LoggerFactory.getLogger(DeviceOnlineNotify.class);

    @Autowired
    private DeviceService deviceService;

    @Resource(name = "deviceChannels")
    private DeviceChannels devChannels;

    @Resource(name = "stMessageService")
    private STMessageService stMessageService;

    @Resource(name = "stEventBus")
    private EventBus eventBus;

    @Subscribe
    public void handleLoginRequetReceiveEvent(final LoginRequetReceiveEvent loginEvent) {
        final CHeaderMessageV2 loginMessage = loginEvent.getcMessage();
        Address srcAddr = loginMessage.getSrcAddr();
        if (AddressType.GEOMATIVE == srcAddr.getAddrType()) {
            return;
        }
        String devNo = srcAddr.getIdentify();
        devChannels.addChannel(loginMessage.getDstType(), devNo, loginEvent.getChannel());

        List<String> gemativeIdList = deviceService.getManagedGenomativeId(devNo);
        if (gemativeIdList != null && !gemativeIdList.isEmpty()) {
            if (gemativeIdList.size() > 1) {
                logger.warn("geomative manage dev{}, is more than one!,current geoId is :", devNo,
                        PrintUtil.toString(gemativeIdList));
            }
            String gemativeId = gemativeIdList.get(0);
            byte[] respData = makeLoginNotify(loginMessage.getSrc(), loginMessage.getSrcType());
            final CHeaderMessageV2 onlineMessage = loginMessage.makeResponse(true, respData)
                    .withDest(AddressType.GEOMATIVE, gemativeId).withCloudSrc().withCmd(CmdCode.CMD_DeviceOnline)
                    .build();
            logger.info("Trans {} online msg to gemative {}.", loginMessage.getSrc(), gemativeIdList);
            eventBus.post(new MessageSendEvent(onlineMessage));
        } else {
            logger.warn("geomative manage dev {} is not set.", devNo);
        }

    }

    // 结构
    /** 设备数目两个字节，然后每个设备由一个字节的设备类型和四个字节的id组成
    {
        byte[2] devLen,
        DevData[devLen] devs;
        DevData{
            byte devType;
            byte[4] devAddr;
        }
    }*/
    private byte[] makeLoginNotify(byte[] devNo, int devType) {
        int pkgLength = 2 + (4 + 1);
        ByteBuf bytebufer = Unpooled.buffer(pkgLength);
        bytebufer.writeShort(1);

        bytebufer.writeByte(devType);
        bytebufer.writeBytes(devNo);
        return bytebufer.array();
    }

}
