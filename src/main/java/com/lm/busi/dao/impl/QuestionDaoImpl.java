package com.lm.busi.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lm.base.BaseDao;
import com.lm.busi.dao.QuestionMapper;
import com.lm.busi.model.Question;

/**
 * Introduction this Type.
 * 
 * @author ZhangYaxu
 * @date 2015年8月30日
 */
@Repository
public class QuestionDaoImpl extends BaseDao implements QuestionMapper {
    
    private static final String NAMESPACE="com.lm.busi.dao.QuestionMapper.";//命名空间

    
    /*** 增加 ***/
    
    @Override
    public int insert(Question record) {
        return getSqlSession().insert(NAMESPACE+"insert", record);
    }
    @Override
    public int insertSelective(Question record) {
        return getSqlSession().insert(NAMESPACE+"insertSelective", record);
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

    
    /*** 修改 ***/
    
    @Override
    public int updateByPrimaryKeySelective(Question record) {
        return getSqlSession().update(NAMESPACE+"updateByPrimaryKeySelective", record);
    }
    @Override
    public int updateByPrimaryKey(Question record) {
        return getSqlSession().update(NAMESPACE+"updateByPrimaryKey", record);
    }

    
    /*** 单个查询 ***/
    
    @Override
    public Question selectByPrimaryKey(Long id) {
        return getSqlSession().selectOne(NAMESPACE+"selectByPrimaryKey", id);
    }
    @Override
    public int countSelectedProperty(Question record) {
        return getSqlSession().selectOne(NAMESPACE+"countSelectedProperty", record);
    }

    
    /*** 列表查询 ***/
    
    @Override
    public List<Question> listModels(Question record) {
        return getSqlSession().selectList(NAMESPACE+"listModelsByModel", record);
    }
    @Override
    public List<Question> listModels(Map<String, Object> map) {
        return getSqlSession().selectList(NAMESPACE+"listModelsByMap", map);
    }
    @Override
    public List<Map<String, Object>> listMaps(Question record) {
        return getSqlSession().selectList(NAMESPACE+"listMapsByModel", record);
    }
    @Override
    public List<Map<String, Object>> listMaps(Map<String, Object> map) {
        return getSqlSession().selectList(NAMESPACE+"listMapsByMap", map);
    }

}
