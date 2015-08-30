package com.lm.busi.dao;

import java.util.List;
import java.util.Map;

import com.lm.busi.model.Subject;

public interface SubjectMapper {

    int insert(Subject record);
    int insertSelective(Subject record);
    
    int deleteByPrimaryKey(Short id);

    int updateByPrimaryKeySelective(Subject record);
    int updateByPrimaryKey(Subject record);
    
    Subject selectByPrimaryKey(Short id);
    int countSelectedProperty(Subject record);
    
    List<Subject> listModels(Subject record);
    List<Subject> listModels(Map<String,Object> map);
    List<Map<String,Object>> listMaps(Subject record);
    List<Map<String,Object>> listMaps(Map<String,Object> map);
}