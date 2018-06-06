package com.github.shu1jia1.site.base.annotation;

public @interface PagedMethod {
    /**
     * 返回值类型
     * @return
     */
    public abstract Class type();
    
    /**
     * 分页数量
     * @return
     */
    public abstract int page();
}
