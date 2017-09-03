package com.github.shu1jia1.common.exception;

public class MessageDecodeException extends ErrorCodeException {
    public static final String ERROR_FORMAT = "wrongformat";
    public static final String MISS_FIELD = "missfield";
    public static final String UNSUPPORTED = "unsupported";

    private static final long serialVersionUID = -3903713173765110656L;

    public MessageDecodeException(String debugMessage) {
        super(debugMessage);
    }

    public MessageDecodeException(String code, String[] arguments) {
        super(null, code, arguments);
    }

    public MessageDecodeException(Throwable e, String code, String[] arguments) {
        super(e, code, arguments);
    }

    public MessageDecodeException(String debugMessage, Throwable source, String code, String[] arguments) {
        super(debugMessage, source, "", code, arguments);
        this.categoryCode = getCategoryCode();
    }
}