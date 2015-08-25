package com.ddup.sys.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ddup.base.BaseDao;
import com.ddup.sys.dao.PrivilegeMapper;
import com.ddup.sys.model.Privilege;

/**
 * Introduction this Type.
 * 
 * @author ZhangYaxu
 * @date 2015年8月18日
 */
@Repository
public class PrivilegeDaoImpl extends BaseDao implements PrivilegeMapper{
    
    /**
     * 命名空间
     */
    private static final String NAMESPACE="com.ddup.sys.model.Privilege.";
    
    @Override
    public int deleteByPrimaryKey(Integer id) {
        return getSqlSession().delete(NAMESPACE+"deleteByPrimaryKey", id);
    }

    @Override
    public int insert(Privilege record) {
        return getSqlSession().insert(NAMESPACE+"insert", record);
    }

    @Override
    public int insertSelective(Privilege record) {
        return getSqlSession().insert(NAMESPACE+"insertSelective", record);
    }

    @Override
    public Privilege selectByPrimaryKey(Integer id) {
        return getSqlSession().selectOne(NAMESPACE+"selectByPrimaryKey", id);
    }

    @Override
    public int updateByPrimaryKeySelective(Privilege record) {
        return getSqlSession().update(NAMESPACE+"updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKey(Privilege record) {
        return getSqlSession().update(NAMESPACE+"updateByPrimaryKey", record);
    }

    @Override
    public int countSelectedProperty(Privilege record) {
        return getSqlSession().selectOne(NAMESPACE+"countSelectedProperty", record);
    }
    
    @Override
    public List<Privilege> listModels(Privilege record) {
        return getSqlSession().selectList(NAMESPACE+"listModelsByModel", record);
    }

    @Override
    public List<Privilege> listModels(Map<String, Object> map) {
        return getSqlSession().selectList(NAMESPACE+"listModelsByMap", map);
    }

    @Override
    public List<Map<String, Object>> listMaps(Privilege record) {
        return getSqlSession().selectList(NAMESPACE+"listMapsByModel", record);
    }

    @Override
    public List<Map<String, Object>> listMaps(Map<String, Object> map) {
        return getSqlSession().selectList(NAMESPACE+"listMapsByMap", map);
    }
    
}
