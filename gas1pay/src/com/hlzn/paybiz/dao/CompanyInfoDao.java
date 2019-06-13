package com.hlzn.paybiz.dao;

import java.io.Serializable;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.hlzn.entity.CompanyInfo;

@Repository("companyInfoDao")
public class CompanyInfoDao extends BaseDaoHibernate<CompanyInfo> {

    /**
     * 通过订单号来查公司商户信息
     */
    public CompanyInfo getCompanyByOrderId(Serializable orderId) {
        CompanyInfo company = null;
        if(orderId != null) {
            String hql = "from CompanyInfo c where c.companyCode=(select o.companyCode from GasPayOrder o where o.id=:id)";
            Query query = this.getSession().createQuery(hql);
            query.setParameter("id", orderId);
            company = (CompanyInfo)query.uniqueResult();
        }
        return company;
    }
    
}