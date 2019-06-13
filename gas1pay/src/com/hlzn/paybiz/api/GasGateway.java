package com.hlzn.paybiz.api;

import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

import com.hlzn.common.WebServiceUtil;

/**
 * 燃气业务网关接口
 */
public class GasGateway {
    
    private final static Logger logger = Logger.getLogger(GasGateway.class);
    
    private String wsUrl;
    private String authUser;
    private String authPass;
    
    /**
     * 构造燃气业务网关
     * @param wsUrl         WS服务WSDL地址
     * @param authUser      授权访问名称
     * @param authPass      授权访问密码
     */
    public GasGateway(String wsUrl, String authUser, String authPass) {
        this.wsUrl = wsUrl;
        this.authUser = authUser;
        this.authPass = authPass;
    }
    
    /**
     * 从燃气网关查询特定燃气客户信息
     * 
     * @param customerId    燃气客户号
     * @return String       燃气客户信息
     */
    public String queryCustomerInfoById(String customerId) {
        String wsMethod = "QueryCustomerInfoById";
        LinkedHashMap<String, Object> paraMap = new LinkedHashMap<String, Object>();
        paraMap.put("customerId", customerId);
        String result = null;
        try {
            result = (String)WebServiceUtil.callWSmethod(this.wsUrl, this.authUser, this.authPass, wsMethod, paraMap);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }
    
    /**
     * 向燃气网关报送已支付订单信息
     * 
     */
    public Short generateChargeInfo(String customerId, String orderId, String totalFee, String createDate, String meterSerialNo, String fee, String payType) {
        String wsMethod = "GenerateChargeInfo";
        LinkedHashMap<String, Object> paraMap = new LinkedHashMap<String, Object>();
        paraMap.put("customerId", customerId);
        paraMap.put("orderId", orderId);
        paraMap.put("totalFee", totalFee);
        paraMap.put("createDate", createDate);
        paraMap.put("meterSerialNo", meterSerialNo);
        paraMap.put("fee", fee);
        paraMap.put("payType", payType);
        Short result = null;
        try {
            result = (Short)WebServiceUtil.callWSmethod(this.wsUrl, this.authUser, this.authPass, wsMethod, paraMap);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }
    
    
}