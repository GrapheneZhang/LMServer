package com.lm.busi.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lm.base.BaseDao;
import com.lm.busi.dao.LMUserMapper;
import com.lm.busi.model.LMUser;

/**
 * Introduction this Type.
 * 
 * @author ZhangYaxu
 * @date 2015年8月30日
 */
@Repository
public class LMUserDaoImpl extends BaseDao implements LMUserMapper {
    
    private static final String NAMESPACE="com.lm.busi.dao.LMUserMapper.";//命名空间

    
    /*** 增加 ***/
    
    @Override
    public int insert(LMUser record) {
        return getSqlSession().insert(NAMESPACE+"insert", record);
    }
    @Override
    public int insertSelective(LMUser record) {
        return getSqlSession().insert(NAMESPACE+"insertSelective", record);
    }
    @Override
    public int insertUserSubject(Map<String, Object> map) {
        return getSqlSession().insert(NAMESPACE+"insertUserSubject", map);
    }

    
    /*** 删除 ***/
    
    @Override
    public int deleteByPrimaryKey(Long id) {
        return getSqlSession().delete(NAMESPACE+"deleteByPrimaryKey", id);
    }
    @Override
    public int deleteByPrimaryKeys(Map<String, Object> map) {
        return getSqlSession().delete(NAMESPACE+"deleteByPrimaryKeys", map);
    }
    @Override
    public int deleteUserSubject(Long userId) {
        return getSqlSession().delete(NAMESPACE+"deleteUserSubject", userId);
    }

    
    /*** 修改 ***/
    
    @Override
    public int updateByPrimaryKeySelective(LMUser record) {
        return getSqlSession().update(NAMESPACE+"updateByPrimaryKeySelective", record);
    }
    @Override
    public int updateByPrimaryKey(LMUser record) {
        return getSqlSession().update(NAMESPACE+"updateByPrimaryKey", record);
    }

    
    /*** 单个查询 ***/
    
    @Override
    public LMUser selectByPrimaryKey(Long id) {
        return getSqlSession().selectOne(NAMESPACE+"selectByPrimaryKey", id);
    }
    @Override
    public int countSelectedProperty(LMUser record) {
        return getSqlSession().selectOne(NAMESPACE+"countSelectedProperty", record);
    }
    @Override
    public Integer listModelsCount(Map<String, Object> map) {
        return getSqlSession().selectOne(NAMESPACE+"listModelsByMap_count", map);
    }

    
    /*** 列表查询 ***/
    
    @Override
    public List<LMUser> listModels(LMUser record) {
        return getSqlSession().selectList(NAMESPACE+"listModelsByModel", record);
    }
    @Override
    public List<LMUser> listModels(Map<String, Object> map) {
        return getSqlSession().selectList(NAMESPACE+"listModelsByMap", map);
    }
    @Override
    public List<Map<String, Object>> listMaps(LMUser record) {
        return getSqlSession().selectList(NAMESPACE+"listMapsByModel", record);
    }
    @Override
    public List<Map<String, Object>> listMaps(Map<String, Object> map) {
        return getSqlSession().selectList(NAMESPACE+"listMapsByMap", map);
    }
    
    
    /********************* 服务 ************************/
    @Override
    public LMUser selectBy4Property(LMUser record) {
        return getSqlSession().selectOne(NAMESPACE+"selectBy4Property", record);
    }
}
