package com.hlzn.paybiz.biz;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.hlzn.paybiz.api.GasGateway;

/**
 * 支付回调异步通知任务
 */
public class PayThread implements Runnable {
    
    private static final Logger logger = Logger.getLogger(PayThread.class);
    
    private static ExecutorService payTaskRunner;
    static {
        if(payTaskRunner == null) {
            payTaskRunner = Executors.newFixedThreadPool(8);
        }
    }
    
    private PayService payService;
    private String outTradeNo;
    private String tradeNo;
    private String gwUrl;
    private String gwUser;
    private String gwPass;
    
    public PayThread(PayService payService, String outTradeNo, String tradeNo, String gwUrl, String gwUser, String gwPass) {
        this.payService = payService;
        this.outTradeNo = outTradeNo;
        this.tradeNo = tradeNo;
        this.gwUrl = gwUrl;
        this.gwUser = gwUser;
        this.gwPass = gwPass;
    }
    
    @Override
    public void run() {
        try {
            payService.updateOrderPaid(outTradeNo, tradeNo);
            GasGateway gateway = new GasGateway(gwUrl, gwUser, gwPass);
            payService.notifyGasGateway(outTradeNo, gateway);
        } catch(Exception e) {
            logger.error(e);
        }
    }
    
    public void start() {
        payTaskRunner.execute(this);
    }
    
    public void stop() {
        payTaskRunner.shutdownNow();
    }
    
}