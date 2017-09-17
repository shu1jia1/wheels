package com.github.shu1jia1.site.base.entity.builder;

import java.util.List;

import com.github.shu1jia1.site.base.entity.PageInfo;
import com.github.shu1jia1.site.base.entity.ResponseData;

public class ResponseBuilder<T> {
    private boolean success = true;
    private String displayMessage;
    private String messageCode;
    private String debugMessage;
    private PageInfo pageInfo;
    private T data;

    // private Class clazz;

    private ResponseBuilder() {
    }

    public static ResponseBuilder newBuilder() {
        return new ResponseBuilder();
    }

    public ResponseBuilder success() {
        this.success = true;
        return this;
    }

    public ResponseBuilder fail() {
        this.success = false;
        return this;
    }

    public ResponseBuilder messageCode(String messageCode) {
        this.messageCode = messageCode;
        return this;
    }

    public ResponseBuilder debugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
        return this;
    }

    public ResponseBuilder data(Object data) {
        this.data = (T) data;
        return this;
    }

    public ResponseData<T> build() {
        ResponseData resp = new ResponseData<>(success);
        resp.setData(data);
        resp.setMessageCode(messageCode);
        resp.setDebugMessage(debugMessage);
        return resp;
    }

}
