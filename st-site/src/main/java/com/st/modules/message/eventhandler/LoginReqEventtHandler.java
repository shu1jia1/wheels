package com.st.modules.message.eventhandler;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.shu1jia1.common.utils.string.PrintUtil;
import com.google.common.eventbus.Subscribe;
import com.st.common.message.STProtoMessage;
import com.st.common.message.entity.CHeaderMessageV2;
import com.st.common.message.entity.STCommon.AddressType;
import com.st.common.message.entity.StMessage.LoginResponse;
import com.st.modules.device.DeviceChannels;
import com.st.modules.message.event.LoginRequetReceiveEvent;
import com.st.site.device.entity.DeviceInfo;
import com.st.site.device.service.DeviceService;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

@Service("loginReqEventtHandler")
public class LoginReqEventtHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoginReqEventtHandler.class);

    @Resource(name = "deviceService")
    private DeviceService deviceService;

    @Resource(name = "deviceChannels")
    private DeviceChannels devChannels;

    @Subscribe
    public void handleLoginRequetReceiveEvent(final LoginRequetReceiveEvent loginEvent) {
        // final STProtoMessage msg = loginEvent.getStMessage();
        // Address src = msg.getSrc();
        final CHeaderMessageV2 loginMessage = loginEvent.getcMessage();
        String devNo = loginMessage.getSrcStr();
        devChannels.addChannel(loginMessage.getDstType(), devNo, loginEvent.getChannel());

        logger.info("recevie Login from {},details {}.", devNo.toString(), loginMessage.getSimpleInfo());
        // final boolean success =
        // deviceService.deviceLogIn(loginEvent.getInnerPayload(),loginEvent.getChannel().remoteAddress().toString());
        String version = "";
        DeviceInfo device = new DeviceInfo(devNo, loginMessage.getSrcType(), version);
        device.setName(devNo);
        SocketAddress remoteAddress = loginEvent.getChannel().remoteAddress();
        final boolean success = deviceService.deviceLogIn(device, remoteAddress.toString());
        Set<String> onlineDevice = devChannels.getAllOnlineDevice();
        logger.info("Current online device {}.", PrintUtil.toString(onlineDevice));

        byte[] respData = new byte[] { 0x03 }; // 给dtu和主机的
        if (success && loginMessage.getSrcAddr().getAddrType() == AddressType.GEOMATIVE) {
            respData = makeLoginResp(deviceService.list(devNo), onlineDevice);
        }
        final CHeaderMessageV2 resp = loginMessage.makeResponse(success, respData).build();
        loginEvent.getChannel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                loginEvent.getChannel().writeAndFlush(resp);
            }
        });
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
    private byte[] makeLoginResp(List<DeviceInfo> deviceList, Set<String> onlineDeviceNos) {
        List<DeviceInfo> onlineDevices = new ArrayList<>();
        for (DeviceInfo device : deviceList) {
            if (onlineDeviceNos.contains(device.getDevNo())) {
                onlineDevices.add(device);
            }
        }
        try {
            int pkgLength = 2 + (4 + 1) * onlineDevices.size();
            ByteBuf bytebufer = Unpooled.buffer(pkgLength);
            bytebufer.writeShort(onlineDevices.size());
            for (DeviceInfo onlineDev : onlineDevices) {
                bytebufer.writeByte(onlineDev.getDevType());
                bytebufer.writeBytes(onlineDev.getDevNoBytes());
            }
            return bytebufer.array();
        } catch (DecoderException e) {
            logger.error("when response login faild. ", e);
        }
        return new byte[0];
    }

    private STProtoMessage makeLoginResponse(STProtoMessage request, boolean success) {
        LoginResponse resp = LoginResponse.newBuilder().setResult(success).build();
        return STProtoMessage.newBuilder().withRequestMessage(request).withPayload(resp).build();
    }

}
