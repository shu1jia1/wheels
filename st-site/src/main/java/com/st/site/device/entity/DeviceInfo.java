package com.st.site.device.entity;

import java.sql.Timestamp;

public class DeviceInfo {
    // private String id;
    private String devNo;
    private int devType;
    private String name;
    private String devVer;
    private String ip;
    private String remarks;
    private String positionId;
    private Timestamp createTime;
    private Timestamp updateTime;
    private Integer deviceState;

    // public String getId() {
    // return id;
    // }
    //
    // public void setId(String id) {
    // this.id = id;
    // }

    public DeviceInfo() {
        super();
    }

    public DeviceInfo(String devno, int type, String version) {
        this.devNo = devno;
        this.devType = type;
        this.devVer = version;
    }

    public String getDevNo() {
        return devNo;
    }

    public String getDevVer() {
        return devVer;
    }

    public void setDevVer(String devVer) {
        this.devVer = devVer;
    }

    public void setDevNo(String devNo) {
        this.devNo = devNo;
    }

    // public int getType() {
    // return type;
    // }
    //
    // public void setType(int type) {
    // this.type = type;
    // }

    public int getDevType() {
        return devType;
    }

    public void setDevType(int devType) {
        this.devType = devType;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
    
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(Integer deviceState) {
        this.deviceState = deviceState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DeviceInfo{devNo:");
        builder.append(devNo);
        builder.append(", devType:");
        builder.append(devType);
        builder.append(", name:");
        builder.append(name);
        builder.append(", devVer:");
        builder.append(devVer);
        builder.append(", ip:");
        builder.append(ip);
        builder.append(", remarks:");
        builder.append(remarks);
        builder.append(", positionId:");
        builder.append(positionId);
        builder.append(", updateTime:");
        builder.append(updateTime);
        builder.append(", deviceState:");
        builder.append(deviceState);
        builder.append("}");
        return builder.toString();
    }
}
