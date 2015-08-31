package com.lm.busi.dao;

import java.util.List;
import java.util.Map;

import com.lm.busi.model.Question;

public interface QuestionMapper {
    int insert(Question record);
    int insertSelective(Question record);
    
    int deleteByPrimaryKey(Long id);
    int deleteByPrimaryKeys(Map<String, Object> map);

    int updateByPrimaryKeySelective(Question record);
    int updateByPrimaryKey(Question record);
    
    Question selectByPrimaryKey(Long id);
    int countSelectedProperty(Question record);
    
    List<Question> listModels(Question record);
    List<Question> listModels(Map<String,Object> map);
    List<Map<String,Object>> listMaps(Question record);
    List<Map<String,Object>> listMaps(Map<String,Object> map);
}