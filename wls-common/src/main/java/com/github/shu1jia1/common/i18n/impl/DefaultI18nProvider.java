package com.github.shu1jia1.common.i18n.impl;

import java.util.Arrays;
import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.github.shu1jia1.common.i18n.I18nProvider;
import com.github.shu1jia1.common.utils.spring.SpringContextUtil;

/**
 * <p>文件名称: DefaultI18nProvider.java</p>
 * <p>文件描述: 默认国际化实现</p>
 * @version 1.0
 * @author  shujia
 */
@Component
public class DefaultI18nProvider implements I18nProvider, ApplicationContextAware {
    private static ThreadLocal<Locale> localei18n = new ThreadLocal<>();
    private ApplicationContext applicationContext;
    private static DefaultI18nProvider i18nProvider;

    @Override
    public String getI18nLabel(String code, String[] args, Locale locale) {
        if (getApplicationContext() != null) {
            String message = getApplicationContext().getMessage(code, args, locale);
            return message == null ? code : message;
        }
        return code + "-" + Arrays.toString(args);
    }

    @Override
    public String getI18nLabel(String code, String[] args) {
        Locale locale = localei18n.get();
        return getI18nLabel(code, args, locale);
    }

    public static void setCurrentLocale(Locale locale) {
        localei18n.set(locale);
    }

    public static Locale getLocale() {
        return localei18n.get();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            applicationContext = SpringContextUtil.getApplicationContext();
        }
        return applicationContext;
    }

    public static I18nProvider getInstance() {
        if (i18nProvider == null) {
            i18nProvider = new DefaultI18nProvider();
        }
        return i18nProvider;
    }

    @Override
    public String getI18nLabel(String code) {
        return getI18nLabel(code, null);
    }
}
