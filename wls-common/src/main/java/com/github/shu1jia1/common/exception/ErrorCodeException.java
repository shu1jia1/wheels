package com.github.shu1jia1.common.exception;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Locale;

import com.github.shu1jia1.common.i18n.I18nProvider;

/** 
 * <p>文件名称: ErrorCodeException.java</p>
 * <p>文件描述: 暂时定义的异常父类，后续增加根据code自动查找异常信息的方法</p>
 */
public class ErrorCodeException extends Exception {

    public static final String DEFAULT_ERROR_CODE = "default";

    private static final long serialVersionUID = 8127174489731045759L;
    protected String categoryCode;
    protected String errorCode;
    protected String[] arguments;
    protected String productVersion = "V1";
    protected String detailText;

    private transient I18nProvider i18nProvider;// = DefaultI18nProvider.getInstance();

    public ErrorCodeException() {
        super();
    }

    public ErrorCodeException(String debugMessage) {
        super(debugMessage);
    }

    public ErrorCodeException(Throwable source) {
        super(source);
    }

    public ErrorCodeException(String debugMessage, Throwable source) {
        this(debugMessage, source, "", "", null);
    }

    public ErrorCodeException(Throwable source, String code) {
        this("", source, code, null);
    }

    public ErrorCodeException(Throwable source, String code, String[] arguments) {
        this("", source, code, arguments);
    }

    public ErrorCodeException(String debugMessage, Throwable source, String code, String[] arguments) {
        this(debugMessage, source, "", code, arguments);
        this.categoryCode = getCategoryCode();
    }

    public ErrorCodeException(String debugMessage, Throwable source, String category, String code,
            String[] arguments) {
        super(debugMessage, source);
        this.productVersion = null;
        setProductVersion();
        this.categoryCode = category;
        this.errorCode = code;
        this.arguments = arguments;
    }

    public static ErrorCodeException newInstance() {
        return new ErrorCodeException();
    }

    public static ErrorCodeException newInstance(String debugMessage) {
        return new ErrorCodeException(debugMessage);
    }

    public static ErrorCodeException newInstance(Throwable source) {
        return new ErrorCodeException(source);
    }

    public static ErrorCodeException newInstance(String debugMessage, Throwable source) {
        return new ErrorCodeException(debugMessage, source);
    }

    public ErrorCodeException withCode(String code) {
        this.setErrorCode(code);
        return this;
    }

    public ErrorCodeException withArgumens(String[] arguments) {
        this.arguments = arguments;
        return this;
    }

    /**
     * 获取此异常的标示，根据此Key可以获取到国际化标签等数据
     * @return
     */
    public String getErrorKey() {
        return new StringBuilder(getCategoryCode()).append(".").append(getErrorCode()).toString();
    }

    public String[] getArguments() {
        return arguments;
    }

    public String getCategoryCode() {
        if (categoryCode == null || categoryCode.length() == 0) {
            categoryCode = this.getClass().getSimpleName();
        }
        return categoryCode;
    }

    public String getErrorCode() {
        if (errorCode == null || errorCode.length() == 0) {
            errorCode = DEFAULT_ERROR_CODE;
        }
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getDisplayText() {
        String displayMessage = "";
        //ErrorItem errorItem = getErrorItem();
        String errorKey = getErrorKey();
        if (errorKey != null) {
            //从国际化中获取
            displayMessage = i18nProvider.getI18nLabel(errorKey, getArguments());
        }
        return displayMessage;
    }

    public String getDisplayText(Locale locale) {
        String displayMessage = "";
        //ErrorItem errorItem = getErrorItem();
        String errorKey = getErrorKey();
        if (errorKey != null) {
            //从国际化中获取
            displayMessage = i18nProvider.getI18nLabel(errorKey, getArguments(), locale);
        }
        return displayMessage;
    }

    public int getErrorLevel() {
        return 1;
        //        ErrorCodeI18n.ErrorItem item = getErrorItem();
        //        if (item == null)
        //            return 0;

        //        else
        //            return item.getLevel();
    }

    private void setProductVersion() {
        //productVersion ;
    }

    /**
     * @return the defaultErrorCode
     */
    public static String getDefaultErrorCode() {
        return DEFAULT_ERROR_CODE;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /**
     * @return the productVersion
     */
    public String getProductVersion() {
        return productVersion;
    }

    /**
     * @return the detailText
     */
    public String getDetailText() {
        return detailText;
    }

    /**
     * @return the i18nProvider
     */
    public I18nProvider getI18nProvider() {
        return i18nProvider;
    }

    /**
     * @param categoryCode the categoryCode to set
     */
    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    /**
     * @param arguments the arguments to set
     */
    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

    /**
     * @param productVersion the productVersion to set
     */
    public void setProductVersion(String productVersion) {
        this.productVersion = productVersion;
    }

    /**
     * @param detailText the detailText to set
     */
    public void setDetailText(String detailText) {
        this.detailText = detailText;
    }

    /**
     * @param i18nProvider the i18nProvider to set
     */
    public void setI18nProvider(I18nProvider i18nProvider) {
        this.i18nProvider = i18nProvider;
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        setProductVersion();
    }

}
