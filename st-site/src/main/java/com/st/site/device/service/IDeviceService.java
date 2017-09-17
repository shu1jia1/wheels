package com.st.site.device.service;

import java.util.List;

import com.github.shu1jia1.site.base.entity.ResponseData;
import com.st.site.device.entity.DeviceInfo;

public interface IDeviceService {
    /**
     * 根据geoId来获取有权限设备
     * @param devId
     * @return
     */
    ResponseData<List<DeviceInfo>> listByGenomativeId(String devId);

    List<String> getManagedGenomativeId(String devNo);

}
