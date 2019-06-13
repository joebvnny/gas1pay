package com.hlzn.webpay.epay;

/**
 * 待支付信息
 */
public class PayInfo  {

	private String addressID;          //地址序号
	private String meterSerialNo;      //表序号
	private String currentMeterFee;    //表充值金额
	private String userTotalChargeFee; //用户充值总金额
	private String meterType;          //表类型
	private String meterName;          //表类型中文名 
	private String addressDetail;      //详细地址信息
    
    public String getAddressID() {
        return addressID;
    }
    
    public void setAddressID(String addressID) {
        this.addressID = addressID;
    }
    
    public String getMeterSerialNo() {
        return meterSerialNo;
    }
    
    public void setMeterSerialNo(String meterSerialNo) {
        this.meterSerialNo = meterSerialNo;
    }
    
    public String getCurrentMeterFee() {
        return currentMeterFee;
    }
    
    public void setCurrentMeterFee(String currentMeterFee) {
        this.currentMeterFee = currentMeterFee;
    }
    
    public String getUserTotalChargeFee() {
        return userTotalChargeFee;
    }
    
    public void setUserTotalChargeFee(String userTotalChargeFee) {
        this.userTotalChargeFee = userTotalChargeFee;
    }
    
    public String getMeterType() {
        return meterType;
    }
    
    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }
    
    public String getMeterName() {
        return meterName;
    }
    
    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }
    
    public String getAddressDetail() {
        return addressDetail;
    }
    
    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }
	
}