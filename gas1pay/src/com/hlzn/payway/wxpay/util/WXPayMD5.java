package com.hlzn.payway.wxpay.util;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * User: rizenguo
 * Date: 2014/10/23
 * Time: 15:43
 */
public class WXPayMD5 {
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 转换字节数组为16进制字串
     * @param b 字节数组
     * @return 16进制字串
     */
    public static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte aB : b) {
            resultSb.append(byteToHexString(aB));
        }
        return resultSb.toString();
    }

    /**
     * 转换byte到16进制
     * @param b 要转换的byte
     * @return 16进制格式
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * MD5编码
     * @param origin 原始字符串
     * @return 经过MD5加密之后的结果
     */
    public static String MD5Encode(String origin, String charsetname) {  
        String resultString = null;  
        try {  
            resultString = new String(origin);  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            if (charsetname == null || "".equals(charsetname))  
                resultString = byteArrayToHexString(md.digest(resultString  
                        .getBytes()));  
            else  
                resultString = byteArrayToHexString(md.digest(resultString  
                        .getBytes(charsetname)));  
        } catch (Exception exception) {  
        }  
        return resultString;  
    }
    
    /**
     * 是否微信支付签名。规则：按参数名称a～z排序，遇到空值参数则不参与签名。
     * @return boolean
     */
    public static boolean isWxPaySign(String characterEncoding, SortedMap<String, String> returnParams, String wxKey) {
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String, String>> es = returnParams.entrySet();
        Iterator<Map.Entry<String, String>> it = es.iterator();
        while(it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String key = entry.getKey();
            String value = entry.getValue();
            if(!"sign".equals(key) && value!=null && !"".equals(value)) {
                sb.append(key + "=" + value + "&");
            }
        }
        sb.append("key=" + wxKey);
        //算出摘要
        String mysign = WXPayMD5.MD5Encode(sb.toString(), characterEncoding).toLowerCase();
        String wxPaySign = (returnParams.get("sign")).toLowerCase();
        
        return wxPaySign.equals(mysign);
    }
    
}
