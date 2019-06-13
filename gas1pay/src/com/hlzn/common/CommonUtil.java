package com.hlzn.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * 通用工具类
 */
public final class CommonUtil {

    /**
     * 根据HttpServletRequest获取来访IP
     */
    public static final String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if(StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if(StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    /**
     * Base64编码 
     */
    public static final String Base64Encode(String str) { 
        if(str == null) return null;
        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        return encoder.encode(str.getBytes());
    } 
    
    /**
     * Base64解码
     */
    public static final String Base64Decode(String str) { 
        if(str == null) return null; 
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder(); 
        try { 
            byte[] bytes = decoder.decodeBuffer(str); 
            return new String(bytes); 
        } catch(Exception e) { 
            return null; 
        } 
    }
    
    /**
     * MAP转XML
     * @param map
     * @return xml
     */
    public static final String map2Xml(HashMap<String, String> map) {
        String xml = "<xml>";
        Iterator<Entry<String, String>> iter = map.entrySet().iterator();
        while(iter.hasNext()) {
            Entry<String, String> entry = iter.next();
            String key = entry.getKey();
            String val = entry.getValue();
            xml += "<" + key + ">" + val + "</" + key + ">";
            //xml += "<" + key + "><![CDATA[" + val + "]]></" + key + ">";
        }
        xml += "</xml>";
        return xml;
    }
    
    /**
     * 通过建立URL连接进行文本请求和文本响应
     * 
     * @param reqUrl    请求的url
     * @param reqData   请求的文本数据
     * @return respData 响应的文本数据
     */
    public static final String postUrlData(String reqUrl, String reqData) {
        OutputStreamWriter writer = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(reqUrl);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("content-type", "UTF-8");
            
            writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            if(reqData == null) {
                reqData = "";
            }
            writer.write(reqData);
            writer.flush();
            writer.close();
            
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line=reader.readLine()) != null) {
                sb.append(line);
                sb.append("\r\n");
            }
            return sb.toString();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer != null) {
                    writer.close();
                }
                if(reader != null) {
                    reader.close();
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    /**
     * 解析XML（完全XML：元素不重复，不带属性），返回第一级元素的键值对。
     * 如果第一级元素有子节点，则此节点的值是其子节点的XML串数据。
     * 
     * @param xmlStr
     * @return map
     * @throws JDOMException
     * @throws IOException
     */
    public static final Map<String, String> parseL1XML(String xmlStr) throws JDOMException, IOException {
        xmlStr = xmlStr.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");

        if(StringUtils.isEmpty(xmlStr)) {
            return null;
        }
        
        Map<String, String> map = new HashMap<String, String>();
        InputStream in = new ByteArrayInputStream(xmlStr.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List<Element> list = root.getChildren();
        Iterator<Element> it = list.iterator();
        while(it.hasNext()) {
            Element ele = (Element) it.next();
            String key = ele.getName();
            String value = "";
            List<Element> children = ele.getChildren();
            if(children.isEmpty()) {
                value = ele.getTextNormalize();
            } else {
                value = CommonUtil.getChildrenXML(children);
            }
            map.put(key, value);
        }
        
        in.close();
        return map;
    }
    
    /**
     * 获取所有子节点的XML
     * @param eleList
     * @return xml
     */
    private static final String getChildrenXML(List<Element> children) {
        StringBuffer sb = new StringBuffer();
        if(!children.isEmpty()) {
            Iterator<Element> it = children.iterator();
            while(it.hasNext()) {
                Element e = (Element)it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List<Element> list = e.getChildren();
                sb.append("<" + name + ">");
                if(!list.isEmpty()) {
                    sb.append(CommonUtil.getChildrenXML(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }
        return sb.toString();
    }
    
}