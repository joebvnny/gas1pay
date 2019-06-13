package com.hlzn.test;

import java.util.Date;

import com.hlzn.entity.CompanyInfo;
import com.hlzn.entity.GasPayOrder;
import com.hlzn.paybiz.api.GasGateway;
import com.hlzn.paybiz.biz.PayConfig;

public class HLTest {
    
    public static void main(String[] args) {
        
        PayConfig conf = new PayConfig("0000000000");
        System.out.println(conf.getPayConfigAL());
        System.out.println(conf.getPayConfigWX());
        System.out.println(conf.getPayConfigUN());
        
        CompanyInfo company = new CompanyInfo();
        System.out.println(company);
        GasPayOrder order = new GasPayOrder();
        order.setOrderTime(new Date());
        System.out.println(order);
        String wsUrl = "http://223.85.248.171:8089/LocalService/WebServiceUnionPay.asmx";
        String authUser = "hlUsername";
        String authPass = "hlPassword";
        GasGateway gateway = new GasGateway(wsUrl, authUser, authPass);
        String result = gateway.queryCustomerInfoById("45004830");
        System.out.println(result);
    }

}
