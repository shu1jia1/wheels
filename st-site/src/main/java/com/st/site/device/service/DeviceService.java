package com.st.site.device.service;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.shu1jia1.common.utils.string.PrintUtil;
import com.github.shu1jia1.site.base.entity.ResponseData;
import com.github.shu1jia1.site.base.entity.builder.ResponseBuilder;
import com.st.modules.device.dao.DeviceDao;
import com.st.modules.device.entity.DeviceInfo;

@Service("deviceService")
public class DeviceService implements IDeviceService, InitializingBean {

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

}
