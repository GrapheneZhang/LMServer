package com.lm.busi.service;

import java.util.List;
import java.util.Map;

import com.lm.busi.model.LMUser;

public interface LMUserService {
    int insert(LMUser record, Short... sIds);
    
    void deleteByPrimaryKeys(Long... ids);
    
    int updateByPrimaryKeySelective(LMUser record,Short... sIds);
    
    LMUser selectByPrimaryKey(Long id);
    Integer listForCRUDCount(Map<String, Object> map);
    boolean checkUnique(LMUser record);//唯一返回true
    
    List<LMUser> listForCRUD(Map<String,Object> map);//专为CRUD列表时服务
    
    /********************* 服务 ************************/
    LMUser serviceLogin(LMUser record);
}
