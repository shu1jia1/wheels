package com.github.shu1jia1.site.base.entity;

import java.io.Serializable;

public class PageInfo implements Serializable {
    private static final long serialVersionUID = 4332370307564592686L;
    private static final String ORDER_ASC = "asc";
    //    private static final String ORDER_DESC = "desc";

    private int page;
    private int perPage;
    private int maxCount;
    private int total;
    private String sortby;
    private String order; //asc desc

    public PageInfo(int page, int perPage, int maxCount, String sortby, String order) {
        super();
        this.page = page;
        this.perPage = perPage;
        this.maxCount = maxCount;
        this.sortby = sortby;
        this.order = order;
    }

    public PageInfo(int page, int perPage, String sortby, String order) {
        super();
        this.page = page;
        this.perPage = perPage;
        this.sortby = sortby;
        this.order = order;
    }

    public PageInfo(int page, int perPage, String sortby) {
        super();
        this.page = page;
        this.perPage = perPage;
        this.sortby = sortby;
        this.order = ORDER_ASC;
    }

    public PageInfo(int page, int perPage, int maxCount, String sortby) {
        super();
        this.page = page;
        this.perPage = perPage;
        this.maxCount = maxCount;
        this.sortby = sortby;
        this.order = ORDER_ASC;
    }

    public PageInfo() {
        //non param constructor
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PagedData [page=");
        builder.append(page);
        builder.append(", per_page=");
        builder.append(perPage);
        builder.append(", per_page=");
        builder.append(perPage);
        builder.append(", maxcount=");
        builder.append(maxCount);
        builder.append(", total=");
        builder.append(total);
        builder.append(", sortby=");
        builder.append(sortby);
        builder.append("]");
        return builder.toString();
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPer_page() {
        return perPage;
    }

    public void setPer_page(int perPage) {
        this.perPage = perPage;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getSortby() {
        return sortby;
    }

    public void setSortby(String sortby) {
        this.sortby = sortby;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getPageStartIndex() {
        return (page - 1) * perPage;
    }

    public int getPageEndIndex() {
        return perPage * page - 1;
    }
}
