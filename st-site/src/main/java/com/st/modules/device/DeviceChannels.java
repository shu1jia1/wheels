package com.st.modules.device;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st.common.message.entity.STCommon.Address;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class DeviceChannels {
    private static final Logger logger = LoggerFactory.getLogger(DeviceChannels.class);

    // 包括所有链接的channel
    ChannelGroup allChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    // private Map<ChannelId, String> channelIdUserIdMap = new
    // ConcurrentHashMap<>();

    // 包括所有注册了的channel
    private Map<String, ChannelId> devChannelMap = new ConcurrentHashMap<>();

    // public void updateChannel(Chanelcon){
    //
    // }

    public boolean add(Channel channel) {
        return allChannels.add(channel);
    }

    public boolean addChannel(int devType, String devNo, Channel channel) {
        logger.info("receive login, log {} channel {}", devNo, channel);
        devChannelMap.put(devNo, channel.id());
        return false;
    }

    public Channel findChannel(Address address) {
        String devno = address.getIdentify();
        return allChannels.find(devChannelMap.get(devno));
    }

    public Set<String> getAllOnlineDevice() {
        HashSet<String> devNos = new HashSet<>();
        for (Map.Entry<String, ChannelId> e : devChannelMap.entrySet()) {
            Channel channel = allChannels.find(e.getValue());
            if (channel.isOpen()) {
                devNos.add(e.getKey());
            }
        }
        return devNos;
    }

}
