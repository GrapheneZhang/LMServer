package com.lm.busi.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lm.busi.dao.QuestionMapper;
import com.lm.busi.model.Question;
import com.lm.busi.service.QuestionService;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Resource
    private QuestionMapper questionMapper;
    
    /*** 定义需要动态查询的列的key和value ****/
    /** key **/
    //列名原始：是放在第一个select部分的
    //private static final String COLUMNS_KEY1="columns_select";
    
    /** value **/
    //private static final String COLUMNS1="id,name";
    
    /*********************** PROCESS ***********************/
    /**
     * 增加
     */
    @Override
    public int insertSelective(Question record) {
        return questionMapper.insertSelective(record);
    }

    /**
     * 删除
     */
    @Override
    public void deleteByPrimaryKeys(Long... ids) {
        if (ids.length>0) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("ids", ids);
            questionMapper.deleteByPrimaryKeys(map);
        }
    }

    /**
     * 修改
     */
    @Override
    public int updateByPrimaryKeySelective(Question record) {
        return questionMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 单个查询
     */
    @Override
    public Question selectByPrimaryKey(Long id) {
        return questionMapper.selectByPrimaryKey(id);
    }

    /**
     * 唯一性查询
     */
    @Override
    public boolean checkUnique(Question record) {
        return questionMapper.countSelectedProperty(record)<1;
    }

    /**************************各种列表********************/
    
    /**
     * 专为CRUD列表时服务
     */
    @Override
    public List<Map<String, Object>> listForCRUD(Map<String, Object> map) {
        return questionMapper.listMaps(map);
    }

}
