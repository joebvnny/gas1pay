package com.hlzn.paybiz.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 统一业务数据访问Hibernate实现
 */
@SuppressWarnings("unchecked")
public abstract class BaseDaoHibernate<T> implements BaseDao<T> {

    //具体业务数据类型
    protected Class<T> entityClass;
    public BaseDaoHibernate() {
        this.entityClass = ((Class<T>)(((ParameterizedType)(this.getClass().getGenericSuperclass())).getActualTypeArguments()[0]));
    }

    @Autowired
    private SessionFactory sessionFactory;
    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void insert(T bizObj) {
        this.getSession().save(bizObj);
    }

    @Override
    public void delete(T bizObj) {
        this.getSession().delete(bizObj);
    }
    
    @Override
    public void delete(Serializable id) {
        String hql = "delete from " + entityClass.getName() + " e where e.id=:id";
        Query query = this.getSession().createQuery(hql);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void update(T bizObj) {
        this.getSession().update(bizObj);
    }
    
    @Override
    public void update(Serializable id, String fieldName, Object fieldValue) {
        String hql = "update " + entityClass.getName() + " e set e." + fieldName + "=:" + fieldName + " where e.id=:id";
        Query query = this.getSession().createQuery(hql);
        query.setParameter(fieldName, fieldValue);
        query.setParameter("id", id);
        query.executeUpdate();
    }
    
    @Override
    public void update(Serializable id, Map<String, Object> paraMap) {
        String hql = "update " + entityClass.getName() + " e set ";
        for(Map.Entry<String, Object> paraEntry : paraMap.entrySet()) {
            hql += "e." + paraEntry.getKey() + "=:" + paraEntry.getKey() + ",";
        }
        hql = hql.substring(0, hql.lastIndexOf(","));
        hql = hql + " where e.id=:id";
        Query query = this.getSession().createQuery(hql);
        for(Map.Entry<String, Object> paraEntry : paraMap.entrySet()) {
            query.setParameter(paraEntry.getKey(), paraEntry.getValue());
        }
        query.setParameter("id", id);
        query.executeUpdate();
    }
    
    @Override
    public T select(Serializable id) {
        T obj = (T)this.getSession().get(entityClass, id);
        return obj;
    }

    @Override
    public T select(String fieldName, Object fieldValue, boolean cacheable) {
        String hql = "from " + entityClass.getName() + " e where e." + fieldName + "=:" + fieldName;
        Query query = this.getSession().createQuery(hql).setCacheable(cacheable);
        query.setParameter(fieldName, fieldValue);
        T obj = (T)query.uniqueResult();
        return obj;
    }

    @Override
    public List<T> list(boolean cacheable) {
        String hql = "from " + entityClass.getName();
        List<T> list = this.getSession().createQuery(hql).setCacheable(cacheable).list();
        return list;
    }

}