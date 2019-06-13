package com.hlzn.webpay.epay;

/**
 * 历史欠费信息
 */
public class HistoryAbInfo {

	private String month;          //月份
	private String abstractFee;    //欠费额
	private String addressDetail;  //地址信息
	private String beginVolume;    //起方量
	private String endVolume;      //止方量

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getAbstractFee() {
		return abstractFee;
	}

	public void setAbstractFee(String abstractFee) {
		this.abstractFee = abstractFee;
	}

	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	public String getBeginVolume() {
		return beginVolume;
	}

	public void setBeginVolume(String beginVolume) {
		this.beginVolume = beginVolume;
	}

	public String getEndVolume() {
		return endVolume;
	}

	public void setEndVolume(String endVolume) {
		this.endVolume = endVolume;
	}
	
}