package com.github.shu1jia1.site.base.entity;

import java.util.ArrayList;
import java.util.List;

public class ListResponseData<T> extends ResponseData<List<T>> {
    public ListResponseData(boolean success, List<T> data) {
        super(success, data);
    }

    public ListResponseData(boolean success) {
        super(success);
    }

    @SuppressWarnings("unchecked")
    public static ListResponseData newFailedResponse() {
        return new ListResponseData(false, new ArrayList<>(0));
    }

}
