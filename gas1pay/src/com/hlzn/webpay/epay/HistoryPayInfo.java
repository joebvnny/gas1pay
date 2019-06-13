package com.hlzn.webpay.epay;

/**
 * 历史缴费信息
 */
public class HistoryPayInfo {
	
	private String createTime;// 交易时间
    private String payFee;    // 交易金额
    private String payVolume; // 方量    
    
    
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getPayFee() {
		return payFee;
	}
	public void setPayFee(String payFee) {
		this.payFee = payFee;
	}
	public String getPayVolume() {
		return payVolume;
	}
	public void setPayVolume(String payVolume) {
		this.payVolume = payVolume;
	}
    
}