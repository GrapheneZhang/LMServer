package com.lm.busi.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lm.base.BaseDao;
import com.lm.busi.dao.SubjectMapper;
import com.lm.busi.model.Subject;

/**
 * Introduction this Type.
 * 
 * @author ZhangYaxu
 * @date 2015年8月30日
 */
@Repository
public class SubjectDaoImpl extends BaseDao implements SubjectMapper {
    
    private static final String NAMESPACE="com.lm.busi.dao.SubjectMapper.";//命名空间

    
    /*** 增加 ***/
    
    @Override
    public int insert(Subject record) {
        return getSqlSession().insert(NAMESPACE+"insert", record);
    }
    @Override
    public int insertSelective(Subject record) {
        return getSqlSession().insert(NAMESPACE+"insertSelective", record);
    }

    
    /*** 删除 ***/
    
    @Override
    public int deleteByPrimaryKey(Short id) {
        return getSqlSession().delete(NAMESPACE+"deleteByPrimaryKey", id);
    }
    @Override
    public int deleteByPrimaryKeys(Map<String, Object> map) {
        return getSqlSession().delete(NAMESPACE+"deleteByPrimaryKeys", map);
    }

    
    /*** 修改 ***/
    
    @Override
    public int updateByPrimaryKeySelective(Subject record) {
        return getSqlSession().update(NAMESPACE+"updateByPrimaryKeySelective", record);
    }
    @Override
    public int updateByPrimaryKey(Subject record) {
        return getSqlSession().update(NAMESPACE+"updateByPrimaryKey", record);
    }

    
    /*** 单个查询 ***/
    
    @Override
    public Subject selectByPrimaryKey(Short id) {
        return getSqlSession().selectOne(NAMESPACE+"selectByPrimaryKey", id);
    }
    @Override
    public int countSelectedProperty(Subject record) {
        return getSqlSession().selectOne(NAMESPACE+"countSelectedProperty", record);
    }

    
    /*** 列表查询 ***/
    
    @Override
    public List<Subject> listModels(Subject record) {
        return getSqlSession().selectList(NAMESPACE+"listModelsByModel", record);
    }
    @Override
    public List<Subject> listModels(Map<String, Object> map) {
        return getSqlSession().selectList(NAMESPACE+"listModelsByMap", map);
    }
    @Override
    public List<Map<String, Object>> listMaps(Subject record) {
        return getSqlSession().selectList(NAMESPACE+"listMapsByModel", record);
    }
    @Override
    public List<Map<String, Object>> listMaps(Map<String, Object> map) {
        return getSqlSession().selectList(NAMESPACE+"listMapsByMap", map);
    }

}
