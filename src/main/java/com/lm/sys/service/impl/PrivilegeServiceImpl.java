package com.lm.sys.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lm.sys.dao.PrivilegeMapper;
import com.lm.sys.model.Privilege;
import com.lm.sys.service.PrivilegeService;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {
    
    @Resource
    private PrivilegeMapper privilegeMapper;
    
    /*** 定义需要动态查询的列的key和value ****/
    /** key **/
    //列名原始：是放在第一个select部分的
    private static final String COLUMNS_KEY1="columns_select";
    //用来group消除重复
    private static final String COLUMNS_KEY2="columns_gb";
    
    
    /** value **/
    //有userId的情况下，groupby所用列
    private static final String COLUMNS1="id,name,action_url,parent_id,icon";
    //多一个父Id
    private static final String COLUMNS2="s.id,s.name,s.action_url as actionUrl,s.parent_id as parentId,rp.name as parentName,s.is_menu as isMenu,s.icon,s.description,s.create_time as createTime,s.update_time as updateTime";
    //菜单json为目的的列名
    private static final String COLUMNS3="id,name,action_url as actionUrl,parent_id as parentId,icon";
    //Ztree用，字段少
    private static final String COLUMNS4="id,name,parent_id as parentId";
    //只取ID和Name
    private static final String COLUMNS5="id,name";

    
    /*********************** PROCESS ***********************/
    /**
     * 增加
     */
    @Override
    public int insertSelective(Privilege record) {
        return privilegeMapper.insertSelective(record);
    }
    /**
     * 删除
     */
    @Transactional
    @Override
    public void deleteByPrimaryKeys(Integer... ids) {
        for (Integer id : ids) {
            privilegeMapper.deleteByPrimaryKey(id);
        }
    }
    /**
     * 修改
     */
    @Override
    public int updateByPrimaryKeySelective(Privilege record) {
        return privilegeMapper.updateByPrimaryKeySelective(record);
    }
    /**
     * 单个查询
     */
    @Override
    public Privilege selectByPrimaryKey(Integer id) {
        return privilegeMapper.selectByPrimaryKey(id);
    }
    /**
     * 唯一性检查
     */
    @Override
    public boolean checkUnique(Privilege record) {
        return privilegeMapper.countSelectedProperty(record)<1;
    }
    
    /**************************各种列表********************/
    
    /**
     * 专为CRUD列表时服务
     */
    @Override
    public List<Map<String,Object>> listForCRUD(Map<String,Object> map) {
        //获取选中的
        map.put(COLUMNS_KEY1, COLUMNS2);
        map.put("extraPName", 1);//需要parentName
        return privilegeMapper.listMaps(map);
    }
    
    /**
     * 用户的权限列表
     */
    @Override
    public List<Map<String, Object>> listByUserId(Integer userId) {
        Map<String,Object> map=new HashMap<String,Object>();
        map.put(COLUMNS_KEY1, COLUMNS3);
        map.put(COLUMNS_KEY2, COLUMNS1);
        map.put("userId", userId);
        return privilegeMapper.listMaps(map);
    }
    
    /**
     * 用户的(权限菜单)权限列表
     */
    @Override
    public List<Map<String, Object>> listMenusByUserId(Integer userId) {
        Map<String,Object> map=new HashMap<String,Object>();
        map.put(COLUMNS_KEY1, COLUMNS3);
        map.put(COLUMNS_KEY2, COLUMNS1);
        map.put("userId", userId);
        map.put("extraMenu", 1);
        return privilegeMapper.listMaps(map);
    }
    
    /**
     * 角色的权限列表，checked角色所
     */
    @Override
    public Map<String,Object> listByRoleId(Integer roleId) {
        Map<String,Object> map=new HashMap<String,Object>();
        map.put(COLUMNS_KEY1, COLUMNS3);
        List<Map<String,Object>> listAll=privilegeMapper.listMaps(map);//获取所有
        map.put("roleId", roleId);
        List<Map<String,Object>> listSelected=privilegeMapper.listMaps(map);//获取选中的
        
        //需要得到选中权限的ids和names
        String pIds="";
        String pNames="";
        
        //将选中的标识到全部中
        for (int i = 0; i < listSelected.size(); i++) {
            Map<String,Object> checked=listSelected.get(i);
            pIds+=checked.get("id")+",";
            pNames+=checked.get("name")+",";
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
            pIds=pIds.substring(0, pIds.length()-1);
            pNames=pNames.substring(0, pNames.length()-1);
        }
        
        //构造返回结果
        map.clear();
        map.put("listAllWithChkSign", listAll);
        map.put("pIds", pIds);
        map.put("pNames", pNames);
        return map;
    }
    
    @Override
    public List<Map<String, Object>> listForZtree() {
        Map<String,Object> map=new HashMap<String,Object>();
        map.put(COLUMNS_KEY1, COLUMNS4);
        return privilegeMapper.listMaps(map);
    }
    @Override
    public List<Map<String, Object>> listIdsAndNames() {
        Map<String,Object> map=new HashMap<String,Object>();
        map.put(COLUMNS_KEY1, COLUMNS5);
        return privilegeMapper.listMaps(map);
    }

}
