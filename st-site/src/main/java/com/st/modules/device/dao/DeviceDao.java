package com.st.modules.device.dao;

import java.util.List;

import com.st.modules.device.entity.DeviceInfo;

public interface DeviceDao {
    public List<DeviceInfo> listDevice(String genomativeID);

    public int insertOrUpdate(DeviceInfo device);
}
