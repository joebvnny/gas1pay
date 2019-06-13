package com.hlzn.common;

/**
 * 业务返回对象
 */
public class RespBean extends BaseBean {

    public final static int RESPONSE_CODE_SUCCESS = 0;
    public final static int RESPONSE_CODE_FAILURE = 1;
    public final static String RESPONSE_MESSAGE_SUCCESS = "SUCCESS";
    public final static String RESPONSE_MESSAGE_FAILURE = "FAILURE";
    
    /**
     * 响应结果码，0为成功，1为失败，必须
     */
    protected int code;

    /**
     * 返回消息，可为成功码、错误码或错误具体消息，非必须
     */
    protected String message;

    /**
     * 返回的数据对象，可能为空
     */
    protected Object data;

    public RespBean() {
        super();
    }

    public RespBean(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }

    public RespBean setValues(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
        return this;
    }
    
}