package com.github.shu1jia1.site.base.page;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.shu1jia1.site.base.entity.PageInfo;

/**
 * <p>文件名称: PageHelper.java</p>
 * <p>文件描述: 分页帮助类</p>
 * @version 1.0
 * @author  shujia
 */
public class PageHelper {
    private static ThreadLocal<PageInfo> page = new ThreadLocal<>();
    private static final Logger logger = LoggerFactory.getLogger(PageHelper.class);

    public static final String PARAM_PAGE = "page";
    public static final String PARAM_PER_PAGE = "per_page";
    public static final String PARAM_MAX_COUNT = "maxcount";
    public static final String PARAM_SORT_BY = "sortby";
    public static final String PARAM_ORDER = "order";
    private static final int DEFAULT_PER_PAGE = 20;

    private PageHelper() {
    }

    public static void setCurrentPage(PageInfo value) {
        page.set(value);
    }

    public static PageInfo getCurrentPage() {
        return page.get();
    }

    public static void removePagedData() {
        logger.info("clear pageinfo:" + getCurrentPage());
        page.remove();
    }

    public static boolean startPage(String pageStr, String perPageStr, String maxCountStr, String sortByStr,
            String order) {
        int page = Integer.parseInt(pageStr);
        int perPage = DEFAULT_PER_PAGE;
        int maxCount = 0;

        if (perPageStr != null) {
            perPage = Integer.parseInt(perPageStr);
        }

        if (maxCountStr != null) {
            maxCount = Integer.parseInt(maxCountStr);
        }

        if (page < 0 || perPage < 0 || maxCount < 0) {
            logger.warn("page info error: page {},per_page {},maxcount " + maxCount, page, perPage);
            return false;
        }

        PageInfo pageInfo = new PageInfo(page, perPage, maxCount, sortByStr);
        if (!StringUtils.isEmpty(order)) {
            pageInfo.setOrder(order);
        }
        //当已经执行过orderBy的时候
        //尝试处理已经排序的情况，后来发现太复杂，且需要服务端保存状态，违背rest原则.放弃.---
        //        PageInfo oldPage = getCurrentPage();
        //        if (oldPage != null && oldPage.hasOrderBy()) {
        //            page.setOrderBy(oldPage.getOrderBy());
        //        }
        logger.info("store pageinfo:" + pageInfo);
        setCurrentPage(pageInfo);
        return true;

    }
}
