package com.lm.busi.dao;

import java.util.List;
import java.util.Map;

import com.lm.busi.model.LMUser;

public interface LMUserMapper {
    int insert(LMUser record);
    int insertSelective(LMUser record);
    int insertUserSubject(Map<String, Object> map);
    
    int deleteByPrimaryKey(Long id);
    int deleteByPrimaryKeys(Map<String, Object> map);
    int deleteUserSubject(Long userId);

    int updateByPrimaryKeySelective(LMUser record);
    int updateByPrimaryKey(LMUser record);
    
    LMUser selectByPrimaryKey(Long id);
    int countSelectedProperty(LMUser record);
    Integer listModelsCount(Map<String,Object> map);
    
    List<LMUser> listModels(LMUser record);
    List<LMUser> listModels(Map<String,Object> map);
    List<Map<String,Object>> listMaps(LMUser record);
    List<Map<String,Object>> listMaps(Map<String,Object> map);
    

}