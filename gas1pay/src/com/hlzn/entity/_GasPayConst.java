package com.hlzn.entity;

/**
 * 支付业务数据常量
 */
public final class _GasPayConst {

    /**
     * 燃气网关类型WebService方式
     */
    public final static String GATEWAY_TYPE_WSDL = "WSDL";
    
    /**
     * 燃气网关类型REST方式
     */
    public final static String GATEWAY_TYPE_REST = "REST";
    
    /**
     * 燃气网关类型DB方式
     */
    public final static String GATEWAY_TYPE_DB = "DB";
    
    /**
     * 常规网页应用端
     */
    public final static String APP_TYPE_WEB = "WEB";
    
    /**
     * 移动应用端
     */
    public final static String APP_TYPE_MOB = "MOB";
    
    /**
     * 微信公众号应用端
     */
    public final static String APP_TYPE_WXH = "WXH";
    
    /**
     * 阿里支付宝
     */
    public final static String PAY_TYPE_AL = "AL";
    
    /**
     * 微信支付
     */
    public final static String PAY_TYPE_WX = "WX";
    
    /**
     * 银联/网银支付
     */
    public final static String PAY_TYPE_UN = "UN";
    
    /**
     * 订单状态：新建
     */
    public final static String ORDER_STATUS_NEW = "0";
    
    /**
     * 订单状态：已支付
     */
    public final static String ORDER_STATUS_PAID = "1";
    
    /**
     * 订单状态：已完结
     */
    public final static String ORDER_STATUS_DONE = "2";
    
}