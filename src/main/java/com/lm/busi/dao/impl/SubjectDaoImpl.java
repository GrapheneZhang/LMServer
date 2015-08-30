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
    
    private static final String NAMESPACE="com.lm.busi.model.Subject.";//命名空间

    @Override
    public int insert(Subject record) {
        return getSqlSession().insert(NAMESPACE+"insert", record);
    }
    @Override
    public int insertSelective(Subject record) {
        return getSqlSession().insert(NAMESPACE+"insertSelective", record);
    }

    @Override
    public int deleteByPrimaryKey(Short id) {
        return getSqlSession().delete(NAMESPACE+"deleteByPrimaryKey", id);
    }

    @Override
    public int updateByPrimaryKeySelective(Subject record) {
        return getSqlSession().update(NAMESPACE+"updateByPrimaryKeySelective", record);
    }
    @Override
    public int updateByPrimaryKey(Subject record) {
        return getSqlSession().update(NAMESPACE+"updateByPrimaryKey", record);
    }

    @Override
    public Subject selectByPrimaryKey(Short id) {
        return getSqlSession().selectOne(NAMESPACE+"selectByPrimaryKey", id);
    }
    @Override
    public int countSelectedProperty(Subject record) {
        return getSqlSession().selectOne(NAMESPACE+"countSelectedProperty", record);
    }

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
