package com.lm.busi.service;

import java.util.List;
import java.util.Map;

import com.lm.busi.model.Subject;

public interface SubjectService {
    int insertSelective(Subject record);
    
    void deleteByPrimaryKeys(Short... ids);
    
    int updateByPrimaryKeySelective(Subject record);
    Subject selectByPrimaryKey(Short id);
    boolean checkUnique(Subject record);//唯一返回true
    
    List<Map<String,Object>> listForCRUD(Map<String,Object> map);//专为CRUD列表时服务
    List<Map<String,Object>> listForZtree();//权限列表，给ZTREE用，字段少
    List<Subject> listModels(Map<String,Object> map);//给选择标签用的
    Map<String, Object> listByUserId(Long userId);//用户的科目列表
}
