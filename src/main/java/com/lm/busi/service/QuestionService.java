package com.lm.busi.service;

import java.util.List;
import java.util.Map;

import com.lm.busi.model.Question;


public interface QuestionService {
    int insertSelective(Question record);
    
    void deleteByPrimaryKeys(Long... ids);
    
    int updateByPrimaryKeySelective(Question record);
    
    Question selectByPrimaryKey(Long id);
    boolean checkUnique(Question record);//唯一返回true
    
    List<Map<String,Object>> listForCRUD(Map<String,Object> map);//专为CRUD列表时服务
    
    /********************* 服务 ************************/
    List<Map<String,Object>> serviceList(Map<String,Object> map);//列表
    
}
