package com.lm.busi.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lm.busi.dao.LMUserMapper;
import com.lm.busi.model.LMUser;
import com.lm.busi.model.Subject;
import com.lm.busi.service.LMUserService;
import com.lm.utils.ProcessUtil;

/**
 * Introduction this Type.
 * 
 * @author ZhangYaxu
 * @date 2015年8月18日
 */
@Service
public class LMUserServiceImpl implements LMUserService {

    @Resource
    private LMUserMapper lMUserMapper;
    
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
    @Transactional
    @Override
    public int insert(LMUser record, Short... sIds) {
        int effectCount=lMUserMapper.insertSelective(record);
        if (sIds.length>0) {
            Long id=record.getId();
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("userId", id);
            map.put("sIds", sIds);
            lMUserMapper.insertUserSubject(map);
        }
        return effectCount;
    }
    
    /**
     * 批量增加
     */
    @Transactional
    @Override
    public int insertList(List<Map<String, Object>> rowList) {
        int countCorrect=0;//暂时正确插入的数（说暂时是因为如果出现Exception事务将Rollback）
        for (int i = 0; i < rowList.size(); i++) {
            Map<String, Object> tempMap=rowList.get(i);
            LMUser lmUser=(LMUser)tempMap.get("lmUser");
            //插入主LMUser
            countCorrect+=lMUserMapper.insertSelective(lmUser);
            //插入关系表
            String[] sIds=(String[])tempMap.get("sIds");//权限数组，可能为空
            if (sIds.length>0) {
                Long id=lmUser.getId();
                Map<String, Object> map=new HashMap<String, Object>();
                map.put("userId", id);
                map.put("sIds", sIds);
                lMUserMapper.insertUserSubject(map);
            }
        }
        return countCorrect;
    }

    /**
     * 删除
     */
    @Override
    public void deleteByPrimaryKeys(Long... ids) {
        if (ids.length>0) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("ids", ids);
            lMUserMapper.deleteByPrimaryKeys(map);
        }
    }

    /**
     * 修改
     */
    @Transactional
    @Override
    public int updateByPrimaryKeySelective(LMUser record,Short... sIds) {
        int effectCount=lMUserMapper.updateByPrimaryKeySelective(record);
        if (sIds.length>0) {
            //先清空原记录
            lMUserMapper.deleteUserSubject(record.getId());
            //再插入现在关系
            Long id=record.getId();
            
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("userId", id);
            map.put("sIds", sIds);
            lMUserMapper.insertUserSubject(map);
        }
        return effectCount;
    }

    /**
     * 单个查询
     */
    @Override
    public LMUser selectByPrimaryKey(Long id) {
        return lMUserMapper.selectByPrimaryKey(id);
    }

    /**
     * 唯一性查询
     */
    @Override
    public boolean checkUnique(LMUser record) {
        return lMUserMapper.countSelectedProperty(record)<1;
    }

    /**************************各种列表********************/

    /**
     * 专为CRUD列表时服务
     */
    @Override
    public List<LMUser> listForCRUD(Map<String, Object> map) {
        return lMUserMapper.listModels(map);
    }
    /**
     * 计数
     */
    @Override
    public Integer listForCRUDCount(Map<String, Object> map) {
        return lMUserMapper.listModelsCount(map);
    }
    
    
    
    /*********************工具方法******************************/
    /**
     * @Title: formatLMUserList2ArrayList 
     * @Description: 将list(Page<E>)转为他的父类ArrayList
     * @param list
     * @throws
     */
    public static List<Map<String, Object>> formatLMUserList2ArrayList(List<LMUser> list) {
        //返回的结果
        List<Map<String,Object>> resultList=new ArrayList<Map<String,Object>>();
        
        Map<String,Object> tempMap=null;
        for (int i = 0; i < list.size(); i++) {
            LMUser item=list.get(i);//源Model
            tempMap=new HashMap<String,Object>(0);//目标Map
            ProcessUtil.beanToMap(item, tempMap);//转Map
            
            //对权限进行字符串化处理
            String sIds="";
            String sEnNames="";
            String sZhNames="";
            next:
            if(tempMap.containsKey("subjectList")){
                @SuppressWarnings("unchecked")
                List<Subject> sList=(List<Subject>)tempMap.get("subjectList");
                if(sList.size()<1){
                    tempMap.remove("subjectList");
                    break next;
                }
                for (int j = 0; j < sList.size(); j++) {
                    Subject sub=sList.get(j);
                    sIds+=sub.getId()+",";
                    sEnNames+=sub.getEnName()+",";
                    sZhNames+=sub.getZhName()+",";
                }
                sIds=sIds.substring(0, sIds.length()-1);
                sEnNames=sEnNames.substring(0, sEnNames.length()-1);
                sZhNames=sZhNames.substring(0, sZhNames.length()-1);
                
                //清除subjectList列
                tempMap.remove("subjectList");
            }
            //设置上面生成的字符串描述
            tempMap.put("sIds",sIds);
            tempMap.put("sEnNames",sEnNames);
            tempMap.put("sZhNames",sZhNames);
            
            //返回
            resultList.add(tempMap);
        }
        return resultList;
    }

    
    /*************************** 服务 ******************************/
    @Override
    public LMUser serviceLogin(LMUser record) {
        record.setIsActive(true);
        return lMUserMapper.selectBy4Property(record);
    }
    
    @Override
    public LMUser servicePermitSignUp(LMUser record) {
        record.setIsActive(null);
        return lMUserMapper.selectBy4Property(record);
    }

    @Override
    public int serviceSignUp(LMUser record) {
        record.setIsActive(true);
        return lMUserMapper.updateByPrimaryKeySelective(record);
    }

}