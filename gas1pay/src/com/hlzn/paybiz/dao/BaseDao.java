package com.hlzn.paybiz.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 基础业务数据访问接口
 */
public interface BaseDao<T> {

    /**
     * 新增业务数据
     */
    public void insert(T obj);
    
    /**
     * 删除业务数据
     */
    public void delete(T obj);
    
    /**
     * 根据ID删除业务数据
     */
    public void delete(Serializable id);

    /**
     * 更新业务数据
     */
    public void update(T obj);
    
    /**
     * 根据指定字段更新业务数据
     */
    public void update(Serializable id, String fieldName, Object fieldValue);
    
    /**
     * 根据指定字段Map更新业务数据
     */
    public void update(Serializable id, Map<String, Object> paraMap);

    /**
     * 根据ID查找业务数据
     */
    public T select(Serializable id);
    
    /**
     * 根据指定字段查找业务数据
     */
    public T select(String fieldName, Object fieldValue, boolean cacheable);

    /**
     * 返回某类所有业务数据
     */
    public List<T> list(boolean cacheable);
    
}