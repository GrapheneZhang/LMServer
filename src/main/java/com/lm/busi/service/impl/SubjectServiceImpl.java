package com.lm.busi.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lm.busi.dao.SubjectMapper;
import com.lm.busi.model.Subject;
import com.lm.busi.service.SubjectService;

@Service
public class SubjectServiceImpl implements SubjectService {
    
    @Resource
    private SubjectMapper subjectMapper;
    
    /*** 定义需要动态查询的列的key和value ****/
    /** key **/
    //列名原始：是放在第一个select部分的
    private static final String COLUMNS_KEY1="columns_select";
    
    /** value **/
    private static final String COLUMNS1="id,en_name as enName,zh_name as zhName";
    
    /*********************** PROCESS ***********************/
    /**
     * 增加
     */
    @Override
    public int insertSelective(Subject record) {
        return subjectMapper.insertSelective(record);
    }
    
    /**
     * 删除
     */
    @Override
    public void deleteByPrimaryKeys(Integer... ids) {
        if (ids.length>0) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("ids", ids);
            subjectMapper.deleteByPrimaryKeys(map);
        }
    }

    /**
     * 修改
     */
    @Override
    public int updateByPrimaryKeySelective(Subject record) {
        return subjectMapper.updateByPrimaryKeySelective(record);
    }
    
    /**
     * 单个查询
     */
    @Override
    public Subject selectByPrimaryKey(Short id) {
        return subjectMapper.selectByPrimaryKey(id);
    }

    /**
     * 唯一性查询
     */
    @Override
    public boolean checkUnique(Subject record) {
        return subjectMapper.countSelectedProperty(record)<1;
    }

    /**************************各种列表********************/

    /**
     * 专为CRUD列表时服务
     */
    @Override
    public List<Map<String, Object>> listForCRUD(Map<String, Object> map) {
        return subjectMapper.listMaps(map);
    }

    /**
     * 给用户管理时ZTREE用，字段少
     */
    @Override
    public List<Map<String, Object>> listForZtree() {
        Map<String,Object> map=new HashMap<String,Object>();
        map.put(COLUMNS_KEY1, COLUMNS1);
        return subjectMapper.listMaps(map);
    }

    @Override
    public Map<String, Object> listByUserId(Long userId) {
        Map<String,Object> map=new HashMap<String,Object>();
        map.put(COLUMNS_KEY1, COLUMNS1);
        List<Map<String,Object>> listAll=subjectMapper.listMaps(map);//获取所有
        map.put("userId", userId);
        List<Map<String,Object>> listSelected=subjectMapper.listMaps(map);//获取选中的
        
        //需要得到选中subject的ids和names
        String sIds="";
        String sZhNames="";
        String sEnNames="";
        
        //将选中的标识到全部中
        for (int i = 0; i < listSelected.size(); i++) {
            Map<String,Object> checked=listSelected.get(i);
            sIds+=checked.get("id")+",";
            sZhNames+=checked.get("zhName")+",";
            sEnNames+=checked.get("enName")+",";
            for (int j = 0; j < listAll.size(); j++) {
                Map<String ,Object> item=listAll.get(j);
                if((Integer)checked.get("id")==(Integer)item.get("id")){
                    item.put("checked", true);
                    listAll.set(j, item);
                    break;
                }
            }
        }
        if (listSelected.size()>0) {
            sIds=sIds.substring(0, sIds.length()-1);
            sZhNames=sZhNames.substring(0, sZhNames.length()-1);
            sEnNames=sEnNames.substring(0, sEnNames.length()-1);
        }
        
        //构造返回结果
        map.clear();
        map.put("listAllWithChkSign", listAll);
        map.put("sIds", sIds);
        map.put("sZhNames", sZhNames);
        map.put("sEnNames", sEnNames);
       
        return map;
    }
}