package com.hlzn.paybiz.biz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.hlzn.common.CommonUtil;

/**
 * 支付配置类（多商户以文件目录进行配置管理）
 */
public final class PayConfig {
    
//    private static final Logger logger = Logger.getLogger(PayConfig.class);
    
    private String companyCode;
    private String payConfigPath;
    private Properties payConfigAL;
    private Properties payConfigWX;
    private Properties payConfigUN;
    
    /**
     * 根据公司编码来加载其相关支付配置
     * 
     * @param companyCode   公司编码
     */
    public PayConfig(String companyCode) {
        if(StringUtils.isNotEmpty(companyCode)) {
            this.companyCode = companyCode;
            String thePath = getPayConfigPath(companyCode);
            if(StringUtils.isNotEmpty(thePath)) {
                this.payConfigPath = thePath;
                this.payConfigAL = loadPayConfig(thePath, "_PayConfigAL");
                this.payConfigWX = loadPayConfig(thePath, "_PayConfigWX");
                this.payConfigUN = loadPayConfig(thePath, "_PayConfigUN");
            }
        }
    }
    
    public String getCompanyCode() {
        return companyCode;
    }
    public String getPayConfigPath() {
        return payConfigPath;
    }
    public Properties getPayConfigAL() {
        return payConfigAL;
    }
    public Properties getPayConfigWX() {
        return payConfigWX;
    }
    public Properties getPayConfigUN() {
        return payConfigUN;
    }
    
    /**
     * 获取特定商户的支付配置路径
     * 
     * @param companyCode   公司编码
     * 
     * @return pathString   绝对路径
     */
    public static String getPayConfigPath(String companyCode) {
        String basePath = CommonUtil.class.getResource("/").getPath();
        if(basePath.contains(":")) {
            basePath = basePath.substring(basePath.indexOf(":")-1);
        }
        return basePath+"paybase/"+companyCode+"/";
    }
    
    /**
     * 加载支付配置文件
     */
    private Properties loadPayConfig(String basePath, String configFile) {
        Properties prop = null;
        if(StringUtils.isNotBlank(basePath)) {
            String filePath = basePath + configFile +".properties";
            File file = new File(filePath);
            InputStream in = null;
            if(file.exists()) {
                prop = new Properties();
                try {
                    in = new FileInputStream(file);
                    BufferedReader bf = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    prop.load(bf);
                } catch(IOException e) {
                    e.printStackTrace();
                } finally {
                    if(in != null) {
                        try {
                            in.close();
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return prop;
    }
    
}