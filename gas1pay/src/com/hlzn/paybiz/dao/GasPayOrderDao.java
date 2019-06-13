package com.hlzn.paybiz.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.hlzn.entity.GasPayOrder;

@Repository("gasPayOrderDao")
public class GasPayOrderDao extends BaseDaoHibernate<GasPayOrder> {
    
    /**
     * 根据公司编码查询其所有订单
     */
    @SuppressWarnings("unchecked")
    public List<GasPayOrder> getOrdersByCompanyCode(String companyCode) {
        List<GasPayOrder> orders = null;
        if(companyCode != null) {
            String hql = "from GasPayOrder o where o.companyCode=:companyCode)";
            Query query = this.getSession().createQuery(hql);
            query.setParameter("companyCode", companyCode);
            orders = query.list();
        }
        return orders;
    }
    
    
}