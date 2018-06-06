package com.github.shu1jia1.site.base.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.validation.ObjectError;

/** 
 * <p>文件名称: ResponseData.java</p>
 * <p>文件描述: Web数据传输通用对象</p>
 * 考虑是否要加上T,ListT,
 * @version 1.0
 * @author  shujia
 */
public class ResponseData<T> implements Serializable{
	private static final long serialVersionUID = 2446977013614170903L;
	private boolean success = true;
    private String displayMessage;
    private String messageCode;
    private String debugMessage;
    private PageInfo pageInfo;

    private T data;
    private List<ObjectError> errors;

    public ResponseData() {
        super();
    }

    public ResponseData(boolean success) {
        super();
        this.success = success;
    }

    public ResponseData(T data) {
        super();
        this.data = data;
    }

    public ResponseData(boolean success, T data) {
        super();
        this.success = success;
        this.data = data;
    }

    public ResponseData(boolean success, List<ObjectError> errors) {
        super();
        this.success = success;
        this.errors = errors;
    }

    public ResponseData(boolean success, String displayMessage, T data) {
        super();
        this.success = success;
        this.displayMessage = displayMessage;
        this.data = data;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<ObjectError> getErrors() {
        return errors;
    }

    public void setErrors(List<ObjectError> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "<ResponseData\n success=" + success + "\n displayMessage=" + displayMessage + "\n data=" + data
                + " \"/>";
    }

}
