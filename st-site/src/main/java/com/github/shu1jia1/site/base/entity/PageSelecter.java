package com.github.shu1jia1.site.base.entity;

import java.util.ArrayList;
import java.util.List;
import com.github.shu1jia1.site.base.entity.PageInfo;

public class PageSelecter<T> {
    private int readed = 0;
    private PageInfo pageInfo;
    private List<T> selectedData;

    public PageSelecter(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
        selectedData = new ArrayList<>(pageInfo.getPer_page());
    }

    public void addData(List<T> dataList) {
        for (T status : dataList) {
            if (selectedData.size() <= pageInfo.getPer_page() && readed <= pageInfo.getPageEndIndex()
                    && readed >= pageInfo.getPageStartIndex()) {
                selectedData.add(status);
            }
            readed++;
        }
    }

    public boolean isFull() {
        return selectedData.size() == pageInfo.getPer_page();
    }

    public List<T> getSelectedPlaces() {
        return selectedData;
    }

    public int getReaded() {
        return readed;
    }
}