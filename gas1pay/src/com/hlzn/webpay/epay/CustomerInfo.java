package com.hlzn.webpay.epay;

/**
 * 客户信息
 */
public class CustomerInfo {

	private String yhcode;         //客户号
	private String yhname;         //姓名
	private String landPhone;      //联系电话
	private String adr;            //地址信息
	private String abstractTotalMoney;//欠费总额
	private String bcye;           //账户余额
    
    public String getYhcode() {
        return yhcode;
    }
    
    public void setYhcode(String yhcode) {
        this.yhcode = yhcode;
    }
    
    public String getYhname() {
        return yhname;
    }
    
    public void setYhname(String yhname) {
        this.yhname = yhname;
    }
    
    public String getLandPhone() {
        return landPhone;
    }
    
    public void setLandPhone(String landPhone) {
        this.landPhone = landPhone;
    }
    
    public String getAdr() {
        return adr;
    }
    
    public void setAdr(String adr) {
        this.adr = adr;
    }
    
    public String getAbstractTotalMoney() {
        return abstractTotalMoney;
    }
    
    public void setAbstractTotalMoney(String abstractTotalMoney) {
        this.abstractTotalMoney = abstractTotalMoney;
    }
    
    public String getBcye() {
        return bcye;
    }
    
    public void setBcye(String bcye) {
        this.bcye = bcye;
    }
	
}