package com.hlzn.webpay.epay;

public class OrderForm {

    private String customerId;
	private String meterId;
	private String chargeFee;
    
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
    
    public String getChargeFee() {
        return chargeFee;
    }
    
    public void setChargeFee(String chargeFee) {
        this.chargeFee = chargeFee;
    }
    
}