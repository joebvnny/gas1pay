package com.hlzn.paybiz.biz;

import java.io.PrintWriter;

/**
 * 支付相关异常
 */
@SuppressWarnings("serial")
public class PayException extends Exception {

    public PayException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
    }

}