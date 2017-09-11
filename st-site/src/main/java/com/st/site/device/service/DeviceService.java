package com.st.site.device.service;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.shu1jia1.common.utils.string.PrintUtil;
import com.github.shu1jia1.site.base.entity.ResponseData;
import com.github.shu1jia1.site.base.entity.builder.ResponseBuilder;
import com.st.site.device.dao.DeviceDao;
import com.st.site.device.entity.DeviceInfo;

@Service("deviceService")
public class DeviceService implements IDeviceService, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(DeviceService.class);

    @Autowired
    private DeviceDao deviceDao;

    @Override
    public ResponseData<List<DeviceInfo>> listByGenomativeId(String devId) {
        return ResponseBuilder.newBuilder().success().data(deviceDao.listDevice(devId)).build();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(PrintUtil.toString(deviceDao.listDevice("geo1")));
    }

    public boolean deviceLogIn(DeviceInfo device, String ip) {
        device.setIp(ip);
        device.setCreateTime(new Timestamp(System.currentTimeMillis()));
        try {
            deviceDao.insertOrUpdate(device);
            return true;
        } catch (Exception e) {
            logger.info("device {} login failed", device);
            logger.error("details:", e);
            return false;
        }
    }

    public List<DeviceInfo> list(String devId) {
        List<DeviceInfo> deviceList = deviceDao.listDevice(devId);
        return deviceList;
    }

}
