package com.st.site.device.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.st.site.device.entity.DeviceInfo;

public interface DeviceDao {
    public List<DeviceInfo> listDevice(@Param("geoNo")String genomativeID);

    public int insertOrUpdate(DeviceInfo device);
}
