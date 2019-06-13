package com.hlzn.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * ID生成器
 */
public class IdGenerator {
	
    /**
     * 生成平台统一支付订单号
     */
    public static final String genPayOrderId() {
        String machineId = "HL"; //集群环境前缀
        String timeStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String uuidStr = "X" + genRandomStr(13);
//      int hashCodeV = UUID.randomUUID().toString().hashCode();
//      if(hashCodeV < 0) {
//          hashCodeV = - hashCodeV;
//      }
//      String uuidStr = String.format("%016d", hashCodeV);
        
        return (machineId + timeStr + uuidStr);
    }
	
    /**
     * 生成随机字符串
     * @param length    字符串长度
     * @return String
     */
    public static final String genRandomStr(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String result = "";
        if(length <= 0) {
            return result;
        }
        for(int i=0; i<length; i++) {
            Random rd = new Random();
            result += chars.charAt(rd.nextInt(chars.length() - 1));
        }
        return result;
    }
    
}