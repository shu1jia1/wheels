package com.github.shu1jia1.site.base.entity;

import java.io.Serializable;

public class TokenInfo implements Serializable{
	private static final long serialVersionUID = -1224607365892942202L;
	private String createTime;
    private String userName;
    private String userId;
    private String lang;
    private String token;
    private int userType;
    
    public int getUserType() {
        return userType;
    }
    public void setUserType(int userType) {
        this.userType = userType;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getLang() {
        return this.lang;
    }
    public void setLang(String lang) {
       this.lang = lang;
    }
    
    public int parseUserId2Int(){
        return Integer.parseInt(userId);
    }
}
