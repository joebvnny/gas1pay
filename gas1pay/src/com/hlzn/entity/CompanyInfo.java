package com.hlzn.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hlzn.common.BaseBean;

/**
 * 公司及其商户信息
 */
@Entity
@Table(name="COMPANY_INFO")
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
public class CompanyInfo extends BaseBean {
    
    @Id 
    @Column(name="id", unique=true, nullable=false)
    private int id;
    
    @Column(name="company_name", nullable=false, length=64)
    private String companyName;
    
    @Column(name="company_code", nullable=false, length=10)
    private String companyCode;
    
    @Column(name="gateway_url", length=255)
    private String gatewayUrl;
    
    @Column(name="gateway_type", length=8)
    private String gatewayType;
    
    @Column(name="al_pid", length=32)
    private String alPid;
    
    @Column(name="al_key", length=32)
    private String alKey;
    
    @Column(name="al_appid", length=32)
    private String alAppId;
    
    @Column(name="al_app_prikey", length=1000)
    private String alAppPriKey;
    
    @Column(name="al_app_pubkey", length=255)
    private String alAppPubKey;
    
    @Column(name="wx_pid", length=32)
    private String wxPid;
    
    @Column(name="wx_key", length=32)
    private String wxKey;
    
    @Column(name="wx_appid", length=32)
    private String wxAppId;
    
    @Column(name="wx_app_secret", length=255)
    private String wxAppSecret;
    
    @Column(name="wx_app_token", length=255)
    private String wxAppToken;
    
    @Column(name="wx_cert_file", length=32)
    private String wxCertFile;
    
    @Column(name="un_pid", length=32)
    private String unPid;
    
    @Column(name="un_cert_file", length=32)
    private String unCertFile;
    
    @Column(name="un_cert_key", length=32)
    private String unCertKey;
    
    @Column(name="flag", length=8)
    private String flag;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

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

    public String getGatewayUrl() {
        return gatewayUrl;
    }
    public void setGatewayUrl(String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }
    
    public String getGatewayType() {
        return gatewayType;
    }
    public void setGatewayType(String gatewayType) {
        this.gatewayType = gatewayType;
    }
    
    public String getAlPid() {
        return alPid;
    }
    public void setAlPid(String alPid) {
        this.alPid = alPid;
    }
    
    public String getAlKey() {
        return alKey;
    }
    public void setAlKey(String alKey) {
        this.alKey = alKey;
    }

    public String getAlAppId() {
        return alAppId;
    }
    public void setAlAppId(String alAppId) {
        this.alAppId = alAppId;
    }
    
    public String getAlAppPriKey() {
        return alAppPriKey;
    }
    public void setAlAppPriKey(String alAppPriKey) {
        this.alAppPriKey = alAppPriKey;
    }

    public String getAlAppPubKey() {
        return alAppPubKey;
    }
    public void setAlAppPubKey(String alAppPubKey) {
        this.alAppPubKey = alAppPubKey;
    }
    
    public String getWxPid() {
        return wxPid;
    }
    public void setWxPid(String wxPid) {
        this.wxPid = wxPid;
    }
    
    public String getWxKey() {
        return wxKey;
    }
    public void setWxKey(String wxKey) {
        this.wxKey = wxKey;
    }
    
    public String getWxAppId() {
        return wxAppId;
    }
    public void setWxAppId(String wxAppId) {
        this.wxAppId = wxAppId;
    }

    public String getWxAppSecret() {
        return wxAppSecret;
    }
    public void setWxAppSecret(String wxAppSecret) {
        this.wxAppSecret = wxAppSecret;
    }

    public String getWxAppToken() {
        return wxAppToken;
    }
    public void setWxAppToken(String wxAppToken) {
        this.wxAppToken = wxAppToken;
    }

    public String getWxCertFile() {
        return wxCertFile;
    }
    public void setWxCertFile(String wxCertFile) {
        this.wxCertFile = wxCertFile;
    }

    public String getUnPid() {
        return unPid;
    }
    public void setUnPid(String unPid) {
        this.unPid = unPid;
    }

    public String getUnCertFile() {
        return unCertFile;
    }
    public void setUnCertFile(String unCertFile) {
        this.unCertFile = unCertFile;
    }

    public String getUnCertKey() {
        return unCertKey;
    }
    public void setUnCertKey(String unCertKey) {
        this.unCertKey = unCertKey;
    }
    
    public String getFlag() {
        return flag;
    }
    public void setFlag(String flag) {
        this.flag = flag;
    }
    
    public CompanyInfo() {
        super();
    }
    public CompanyInfo(int id, String companyName, String companyCode, String gatewayUrl, String gatewayType, String alPid, String alKey, String alAppId, String alAppPriKey, String alAppPubKey, String wxPid, String wxKey, String wxAppId, String wxAppSecret, String wxAppToken, String wxCertFile, String unPid, String unCertFile, String unCertKey, String flag) {
        this.id = id;
        this.companyName = companyName;
        this.companyCode = companyCode;
        this.gatewayUrl = gatewayUrl;
        this.gatewayType = gatewayType;
        this.alPid = alPid;
        this.alKey = alKey;
        this.alAppId = alAppId;
        this.alAppPriKey = alAppPriKey;
        this.alAppPubKey = alAppPubKey;
        this.wxPid = wxPid;
        this.wxKey = wxKey;
        this.wxAppId = wxAppId;
        this.wxAppSecret = wxAppSecret;
        this.wxAppToken = wxAppToken;
        this.wxCertFile = wxCertFile;
        this.unPid = unPid;
        this.unCertFile = unCertFile;
        this.unCertKey = unCertKey;
        this.flag = flag;
    }

}