package com.hlzn.entity;

/**
 * 燃气支付对账单
 */
public class AccStatement {

    private String orderId;
    private String queryId;
    private String companyCode;
    private String customerId;
    private String meterId;
    private String cardId;
    private String payType;
    private String merchantId;
    private String amtFee;
    private String handleFee;
    private String settleFee;
    private String dealTime;
    private String createTime;
    
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
    public String getQueryId() {
        return queryId;
    }
    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }
    
    public String getCompanyCode() {
        return companyCode;
    }
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public String getMeterId() {
        return meterId;
    }
    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }
    
    public String getCardId() {
        return cardId;
    }
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
    
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    
    public String getMerchantId() {
        return merchantId;
    }
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
    
    public String getAmtFee() {
        return amtFee;
    }
    public void setAmtFee(String amtFee) {
        this.amtFee = amtFee;
    }
    
    public String getHandleFee() {
        return handleFee;
    }
    public void setHandleFee(String handleFee) {
        this.handleFee = handleFee;
    }
    
    public String getSettleFee() {
        return settleFee;
    }
    public void setSettleFee(String settleFee) {
        this.settleFee = settleFee;
    }
    
    public String getDealTime() {
        return dealTime;
    }
    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }
    
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    
}