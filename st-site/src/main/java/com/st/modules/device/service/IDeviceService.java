package com.st.modules.device.service;

import java.util.List;

import com.github.shu1jia1.site.base.entity.ResponseData;
import com.st.modules.device.entity.DeviceInfo;

public interface IDeviceService {

    ResponseData<List<DeviceInfo>> listByGenomativeId(String devId);

}
