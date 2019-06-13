package com.hlzn.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

import com.hlzn.common.BaseBean;

/**
 * 燃气缴费订单信息
 */
@Entity
@Table(name="GASPAY_ORDER")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class GasPayOrder extends BaseBean {

    @Id 
    @Column(name="id", unique=true, nullable=false, length=32)
    private String id;
    
    @Column(name="app_type", length=8)
    private String appType;
    
    @Column(name="company_code", length=10)
    private String companyCode;
    
    @Column(name="customer_id", length=32)
    private String customerId;
    
    @Column(name="meter_id", length=32)
    private String meterId;
    
    @Column(name="card_id", length=32)
    private String cardId;
    
    @Column(name="charge_fee", precision=10, scale=2)
    private double chargeFee;
    
    @Column(name="pay_type", length=8)
    private String payType;
    
    @Column(name="order_time")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date orderTime;
    
    @Column(name="paid_time")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date paidTime;
    
    @Column(name="trade_id", length=64)
    private String tradeId;
    
    @Column(name="status", length=8)
    private String status;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
    public String getAppType() {
        return appType;
    }
    public void setAppType(String appType) {
        this.appType = appType;
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
    
    public double getChargeFee() {
        return chargeFee;
    }
    public void setChargeFee(double chargeFee) {
        this.chargeFee = chargeFee;
    }
    
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    
    public Date getOrderTime() {
        return orderTime;
    }
    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }
    
    public Date getPaidTime() {
        return paidTime;
    }
    public void setPaidTime(Date paidTime) {
        this.paidTime = paidTime;
    }
    
    public String getTradeId() {
        return tradeId;
    }
    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }
    
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public GasPayOrder() {
        super();
    }
    public GasPayOrder(String id, String appType, String companyCode, String customerId, String meterId, String cardId, double chargeFee, String payType, Date orderTime, Date paidTime, String tradeId, String status) {
        this.id = id;
        this.appType = appType;
        this.companyCode = companyCode;
        this.customerId = customerId;
        this.meterId = meterId;
        this.cardId = cardId;
        this.chargeFee = chargeFee;
        this.payType = payType;
        this.orderTime = orderTime;
        this.paidTime = paidTime;
        this.tradeId = tradeId;
        this.status = status;
    }
    
}