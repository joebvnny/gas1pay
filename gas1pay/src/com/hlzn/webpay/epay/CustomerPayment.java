package com.hlzn.webpay.epay;

import java.util.List;

/**
 * 客户支付信息汇总
 */
public class CustomerPayment {

    private String companyName;
    private String companyCode;
    private String companyMerchantNumber;
	private List<CustomerInfo> customer;
	private List<HistoryAbInfo> historyAbstractDetail;
	private List<HistoryPayInfo> historyOne;
	private List<HistoryPayInfo> historyThree;
	private List<PayInfo> payInfo;
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public String getCompanyCode() {
        return companyCode;
    }
    
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
    
    public String getCompanyMerchantNumber() {
        return companyMerchantNumber;
    }
    
    public void setCompanyMerchantNumber(String companyMerchantNumber) {
        this.companyMerchantNumber = companyMerchantNumber;
    }
    
    public List<CustomerInfo> getCustomer() {
        return customer;
    }
    
    public void setCustomer(List<CustomerInfo> customer) {
        this.customer = customer;
    }
    
    public List<HistoryAbInfo> getHistoryAbstractDetail() {
        return historyAbstractDetail;
    }
    
    public void setHistoryAbstractDetail(List<HistoryAbInfo> historyAbstractDetail) {
        this.historyAbstractDetail = historyAbstractDetail;
    }

    public List<HistoryPayInfo> getHistoryOne() {
        return historyOne;
    }
    
    public void setHistoryOne(List<HistoryPayInfo> historyOne) {
        this.historyOne = historyOne;
    }
    
    public List<HistoryPayInfo> getHistoryThree() {
        return historyThree;
    }
    
    public void setHistoryThree(List<HistoryPayInfo> historyThree) {
        this.historyThree = historyThree;
    }
    
    public List<PayInfo> getPayInfo() {
        return payInfo;
    }
    
    public void setPayInfo(List<PayInfo> payInfo) {
        this.payInfo = payInfo;
    }
	
}