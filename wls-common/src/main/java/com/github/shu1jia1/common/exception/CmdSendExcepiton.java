package com.github.shu1jia1.common.exception;

public class CmdSendExcepiton extends ErrorCodeException {

    private static final long serialVersionUID = -3903713173765110656L;

    public CmdSendExcepiton(String debugMessage) {
        super(debugMessage);
    }

    public CmdSendExcepiton(String code, String[] arguments) {
        super(null, code, arguments);
    }

    public CmdSendExcepiton(Throwable e, String code, String[] arguments) {
        super(e, code, arguments);
    }

    public CmdSendExcepiton(String debugMessage, Throwable source, String code, String[] arguments) {
        super(debugMessage, source, "", code, arguments);
        this.categoryCode = getCategoryCode();
    }
}
