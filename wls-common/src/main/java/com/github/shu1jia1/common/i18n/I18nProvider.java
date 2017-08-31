package com.github.shu1jia1.common.i18n;

import java.util.Locale;

/**
 * <p>文件名称: I18nProvider.java</p>
 * <p>文件描述: 国际化接口</p>
 * @version 1.0
 * @author  shujia
 */
public interface I18nProvider {
    String getI18nLabel(String code, String[] args, Locale locale);

    String getI18nLabel(String code, String[] args);

    String getI18nLabel(String code);
}
