package com.hlzn.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.log4j.Logger;

/**
 * CXF封装的WSClient
 */
public class WebServiceUtil {
    
    private final static Logger logger = Logger.getLogger(WebServiceUtil.class);
    
    private final static int CONNECT_TIMEOUT = 10000;
    private final static JaxWsDynamicClientFactory clientFactory = JaxWsDynamicClientFactory.newInstance();
    
    /**
     * 调用WebService方法，返回业务对象数据
     * 
     * @param wsUrl     WebService地址
     * @param authUser  授权访问用户
     * @param authPass  授权访问密码
     * @param wsMethod  WebService方法名
     * @param paraMap   方法调用参数LinkedHashMap，必须按WSDL中定义的参数顺序存放
     * 
     * @return Object   业务对象数据
     * 
     * @throws Exception
     */
    public final static Object callWSmethod(String wsUrl, String authUser, String authPass, String wsMethod, LinkedHashMap<String, Object> paraMap) throws Exception {
        Client wsClient = null;
        
        try {
            logger.info("connecting GasGateway[" + wsUrl + "]...");
            wsClient = clientFactory.createClient(wsUrl + "?wsdl");
//            wsClient.getOutInterceptors().add(new LoggingOutInterceptor());
//            wsClient.getInInterceptors().add(new LoggingInInterceptor());
            wsClient.getOutInterceptors().add(new WebServiceAuth(authUser, authPass));
        } catch(Exception e) {
            int i = 1;
            logger.warn(e.getMessage() + "---> 尝试第 1 次重连……");
            while(i<3) {
                try {
                    Thread.sleep(3000);
                    wsClient = clientFactory.createClient(wsUrl + "?wsdl");
                    break;
                } catch(Exception x) {
                    logger.warn(x.getMessage() + "---> 尝试第 " + (++i) + " 次重连……");
                    continue;
                }
            }
            if(i==3) {
                logger.error("连接燃气网关[" + wsUrl + "]失败！");
            }
        }
        
        Object result = null;
        
        if(wsClient != null) {
            logger.info("connection success to[" + wsUrl + "]");
            HTTPConduit conduit = (HTTPConduit)wsClient.getConduit();
            conduit.getTarget().getAddress().setValue(wsUrl);
            HTTPClientPolicy policy = new HTTPClientPolicy();
            policy.setConnectionTimeout(CONNECT_TIMEOUT);
            policy.setReceiveTimeout(CONNECT_TIMEOUT * 2);
            conduit.setClient(policy);
            
            List<Object> params = new ArrayList<Object>();
            for(Entry<String, Object> entry : paraMap.entrySet()) {
                params.add(entry.getValue());
            }
            Object[] results = wsClient.invoke(wsMethod, params.toArray());
            if(results != null) {
                result = results[0];
            }
        }
        
        return result;
    }

}