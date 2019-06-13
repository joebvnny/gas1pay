package com.hlzn.paybiz.biz;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hlzn.common.IdGenerator;
import com.hlzn.entity.CompanyInfo;
import com.hlzn.entity.GasPayOrder;
import com.hlzn.entity._GasPayConst;
import com.hlzn.paybiz.api.GasGateway;
import com.hlzn.paybiz.dao.CompanyInfoDao;
import com.hlzn.paybiz.dao.GasPayOrderDao;

/**
 * 支付相关业务逻辑
 */
@Service("payService")
public class PayService {
    
    @Autowired
    private CompanyInfoDao companyInfoDao;
    @Autowired
    private GasPayOrderDao gasPayOrderDao;
    
    /**
     * 商户信息列表
     */
    @Transactional(propagation=Propagation.REQUIRED, readOnly=true, rollbackFor=Exception.class)
    public List<CompanyInfo> listCompanies() {
        List<CompanyInfo> companies = companyInfoDao.list(true);
        return companies;
    }
    
    /**
     * 订单信息列表
     */
    @Transactional(propagation=Propagation.REQUIRED, readOnly=true, rollbackFor=Exception.class)
    public List<GasPayOrder> listOrders() {
        List<GasPayOrder> orders = gasPayOrderDao.list(true);
        return orders;
    }
    
    /**
     * 根据companyCode获取商户信息
     */
    @Transactional(propagation=Propagation.REQUIRED, readOnly=true, rollbackFor=Exception.class)
    public CompanyInfo getCompanyByCode(String companyCode) {
        CompanyInfo company = null;
        if(StringUtils.isNotEmpty(companyCode)) {
            company = companyInfoDao.select("companyCode", companyCode, true);
        }
        return company;
    }
    
    /**
     * 根据ID获取订单信息
     */
    @Transactional(propagation=Propagation.REQUIRED, readOnly=true, rollbackFor=Exception.class)
    public GasPayOrder getOrderById(Serializable orderId) {
        GasPayOrder order = null;
        if(orderId != null) {
            order = gasPayOrderDao.select(orderId);
        }
        return order;
    }
    
    /**
     * 根据公司编码获取其所有订单
     */
    @Transactional(propagation=Propagation.REQUIRED, readOnly=true, rollbackFor=Exception.class)
    public List<GasPayOrder> getOrdersByCompanyCode(String companyCode) {
        List<GasPayOrder> orders = null;
        if(companyCode != null) {
            orders = gasPayOrderDao.getOrdersByCompanyCode(companyCode);
        }
        return orders;
    }
    
    /**
     * 根据订单ID反查商户信息
     */
    @Transactional(propagation=Propagation.REQUIRED, readOnly=true, rollbackFor=Exception.class)
    public CompanyInfo getCompanyByOrderId(Serializable orderId) {
        CompanyInfo company = null;
        if(orderId != null) {
            company = companyInfoDao.getCompanyByOrderId(orderId);
        }
        return company;
    }
    
    /**
     * 创建订单
     */
    @Transactional(propagation=Propagation.REQUIRED, readOnly=false, rollbackFor=Exception.class)
    public void createOrder(GasPayOrder order) {
        if(order != null) {
            gasPayOrderDao.insert(order);
        }
    }
    
    /**
     * 根据订单参数来创建订单
     * 
     * @param appType       调用气付通的应用类型，目前分为"WEB"、"MOB"、"WXH"三类（必填）
     * @param companyCode   燃气公司编码（必填）
     * @param customerId    燃气客户号（必填）
     * @param meterId       气表编号
     * @param cardId        气卡编号
     * @param chargeFee     缴费金额（必填）
     * @param payType       支付类型，目前分为"AL"、"WX"、"UN"三类
     * 
     * @return orderId      订单号
     * 
     * @throws PayException
     */
    @Transactional(propagation=Propagation.REQUIRED, readOnly=false, rollbackFor=Exception.class)
    public String createOrder(String appType, String companyCode, String customerId, String meterId, String cardId, double chargeFee, String payType) throws PayException {
        if(StringUtils.isEmpty(appType) | StringUtils.isEmpty(companyCode) | StringUtils.isEmpty(customerId) | chargeFee < 0) {
            throw new PayException("必填字段不能为空！");
        }
        String orderId = IdGenerator.genPayOrderId();
        GasPayOrder order = new GasPayOrder(orderId, appType, companyCode, customerId, meterId, cardId, chargeFee, payType, new Date(), null, null, _GasPayConst.ORDER_STATUS_NEW);
        gasPayOrderDao.insert(order);
        
        return orderId;
    }
    
    /**
     * 根据ID删除订单
     */
    @Transactional(propagation=Propagation.REQUIRED, readOnly=false, rollbackFor=Exception.class)
    public void deleteOrder(Serializable orderId) {
        if(orderId != null) {
            gasPayOrderDao.delete(orderId);
        }
    }
    
    /**
     * 更新订单信息
     * 
     * @param order     订单对象
     */
    @Transactional(propagation=Propagation.REQUIRED, readOnly=false, rollbackFor=Exception.class)
    public void updateOrder(GasPayOrder order) {
        if(order != null) {
            gasPayOrderDao.update(order);
        }
    }
    
    /**
     * 更新订单支付类型
     * 
     * @param orderId   订单ID
     * @param payType   订单支付类型（AL/WX/UN）
     */
    @Transactional(propagation=Propagation.REQUIRED, readOnly=false, rollbackFor=Exception.class)
    public void updateOrderPayType(Serializable orderId, String payType) {
        if(StringUtils.isNotEmpty(payType)) {
            gasPayOrderDao.update(orderId, "payType", payType);
        }
    }
    
    /**
     * 更新订单状态为已支付
     * 
     * @param orderId   订单ID
     * @param tradeId   订单在第三方支付系统中的交易ID号
     */
    @Transactional(propagation=Propagation.REQUIRED, readOnly=false, rollbackFor=Exception.class)
    public void updateOrderPaid(Serializable orderId, String tradeId) {
        if(StringUtils.isNotEmpty(tradeId)) {
            Map<String, Object> paraMap = new HashMap<String, Object>();
            paraMap.put("paidTime", new Date());
            paraMap.put("tradeId", tradeId);
            paraMap.put("status", _GasPayConst.ORDER_STATUS_PAID);
            gasPayOrderDao.update(orderId, paraMap);
        }
    }
    
    /**
     * 更新订单状态为已完成
     * 
     * @param orderId   订单ID
     * @param tradeId   订单在第三方支付系统中的交易ID号
     */
    @Transactional(propagation=Propagation.REQUIRED, readOnly=false, rollbackFor=Exception.class)
    public void updateOrderDone(Serializable orderId) {
        gasPayOrderDao.update(orderId, "status", _GasPayConst.ORDER_STATUS_DONE);
    }
    
    /**
     * 通知燃气网关订单已支付信息
     * 
     * @param orderId
     * @param wsUrl
     */
    @Transactional(propagation=Propagation.REQUIRED, readOnly=false, rollbackFor=Exception.class)
    public void notifyGasGateway(Serializable orderId, GasGateway gateway) {
        GasPayOrder order = gasPayOrderDao.select(orderId);
        Short done = gateway.generateChargeInfo(order.getCustomerId(), order.getId(), String.valueOf(order.getChargeFee()), String.valueOf(order.getPaidTime()), order.getMeterId(), String.valueOf(order.getChargeFee()), order.getPayType());
        if(done == 0) {
            this.updateOrderDone(orderId);
        }
    }
    
}