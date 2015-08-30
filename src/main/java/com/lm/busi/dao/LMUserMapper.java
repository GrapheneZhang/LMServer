package com.lm.busi.dao;

import com.lm.busi.model.LMUser;

public interface LMUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(LMUser record);

    int insertSelective(LMUser record);

    LMUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LMUser record);

    int updateByPrimaryKey(LMUser record);
}