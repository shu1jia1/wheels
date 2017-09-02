package com.github.shu1jia1.site.base.page.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.github.shu1jia1.site.base.annotation.PagedMethod;
import com.github.shu1jia1.site.base.entity.PageInfo;
import com.github.shu1jia1.site.base.entity.ResponseData;
import com.github.shu1jia1.site.base.page.PageHelper;

import static com.github.shu1jia1.site.base.page.PageHelper.*;

/**
 * <p>文件名称: SpringPagingInterceptor.java</p>
 * <p>文件描述: 分页拦截器，暂时还做了一些记录日志的工作</p>
 * from jeesite
 * @version 1.0
 * @author  shujia
 */
public class SpringPagingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(SpringPagingInterceptor.class);

    public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {
        Object retVal = pjp.proceed();
        PageInfo currentPageInfo = PageHelper.getCurrentPage();
        if (currentPageInfo != null && retVal instanceof ResponseData) {
            ResponseData<?> responseData = (ResponseData<?>) retVal;
            if (responseData.getData() != null && responseData.getPageInfo() == null) {
                logger.info("Modify responsData with pageInfo:" + currentPageInfo);
                responseData.setPageInfo(currentPageInfo);
                PageHelper.removePagedData();
            }
        }
        return retVal;
    }

    /**
     * preHandle方法是进行处理器拦截用的，顾名思义，该方法将在Controller处理之前进行调用，
     * SpringMVC中的Interceptor拦截器是链式的，可以同时存在
     * 多个Interceptor，然后SpringMVC会根据声明的前后顺序一个接一个的执行
     * ，而且所有的Interceptor中的preHandle方法都会在
     * Controller方法调用之前调用。SpringMVC的这种Interceptor链式结构也是可以进行中断的
     * ，这种中断方式是令preHandle的返 回值为false，当preHandle的返回值为false的时候整个请求就结束了。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        logger.info("intercepotr: {}.{}", request.getRequestURI(), request.getMethod());
        if ("GET".equals(request.getMethod()) && request.getParameter(PARAM_PAGE) != null) {
            String page = request.getParameter(PARAM_PAGE);
            logger.debug("preHandler page info,page: " + page);
            try {
                PageHelper.startPage(page, request.getParameter(PARAM_PER_PAGE), request.getParameter(PARAM_MAX_COUNT),
                        request.getParameter(PARAM_SORT_BY), request.getParameter(PARAM_ORDER));
                if (handler instanceof HandlerMethod) {
                    HandlerMethod method = (HandlerMethod) handler;
                    PagedMethod pagedAno = method.getMethodAnnotation(PagedMethod.class);

                    // 判断方法上注解的PagedMethod值，
                    if (pagedAno == null) {
                        return true;
                    }
                }
            } catch (Exception e) {
                logger.warn("start page failed.", e);
                return false;
            }
        }
        return true;
    }

    /**
     * 这个方法只会在当前这个Interceptor的preHandle方法返回值为true的时候才会执行。postHandle是进行处理器拦截用的，
     * 它的执行时间是在处理器进行处理之
     * 后，也就是在Controller的方法调用之后执行，但是它会在DispatcherServlet进行视图的渲染之前执行
     * ，也就是说在这个方法中你可以对ModelAndView进行操
     * 作。这个方法的链式结构跟正常访问的方向是相反的，也就是说先声明的Interceptor拦截器该方法反而会后调用
     * ，这跟Struts2里面的拦截器的执行过程有点像，
     * 只是Struts2里面的intercept方法中要手动的调用ActionInvocation的invoke方法
     * ，Struts2中调用ActionInvocation的invoke方法就是调用下一个Interceptor
     * 或者是调用action，然后要在Interceptor之前调用的内容都写在调用invoke之前
     * ，要在Interceptor之后调用的内容都写在调用invoke方法之后。
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // no comment
    }

    /**
     * 该方法也是需要当前对应的Interceptor的preHandle方法的返回值为true时才会执行。该方法将在整个请求完成之后，
     * 也就是DispatcherServlet渲染了视图执行，
     * 这个方法的主要作用是用于清理资源的，当然这个方法也只能在当前这个Interceptor的preHandle方法的返回值为true时才会执行。
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // todo 
    }

}