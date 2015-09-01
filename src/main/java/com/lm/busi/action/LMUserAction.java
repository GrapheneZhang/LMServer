package com.lm.busi.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lm.base.ToJSPException;
import com.lm.busi.model.LMUser;
import com.lm.busi.service.LMUserService;
import com.lm.busi.service.SubjectService;
import com.lm.busi.service.impl.LMUserServiceImpl;
import com.lm.utils.ProcessUtil;

@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
@Controller
public class LMUserAction {
    
    private static final Logger LOGGER=Logger.getLogger(LMUserAction.class);//日志
    private static final String URL_PREFIX="/lmuser";
    protected static final String JSP_PREFIX="/WEB-INF/jsp/busi/lmuser";//通用的jsp前缀
    
    @Resource
    private LMUserService lMUserService;
    @Resource
    private SubjectService subjectService;

    private JSONObject resultJson=null;//结果JSON，使用时先new
    
    
    /*********************System Process***************************/

    /**
     * 1 代表此雷鸣用户
     * 跳转到list页面
     */
    @RequestMapping(URL_PREFIX+"/query")
    public String list() throws ToJSPException{
        try {
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("跳转到雷鸣用户页面");
            LOGGER.error(errorMsg, e);
            throw new ToJSPException(errorMsg);
        }
        return JSP_PREFIX+"/list";
    }
    
    /**
     * 2.1 列表页面数据填充
     * 返回用于CRUD的json数据
     */
    @RequestMapping(value=URL_PREFIX+"/query/jsonlist")
    @ResponseBody
    public JSONObject jsonList(String fuzzyWord,Integer page,Integer rows
            ){
        resultJson=new JSONObject();
        try {
            //查询条件
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("fuzzyWord", fuzzyWord);
            List<LMUser> list=null;//结果list
            //查询数据库
            if (null==page||null==rows) {//全量查询
                list=lMUserService.listForCRUD(map);
            }else{//手写分页查询
                map.put("offset", (page-1)*rows);
                map.put("limit", rows);
                list=lMUserService.listForCRUD(map);
                resultJson.put("total", lMUserService.listForCRUDCount(map));
            }
            //结果处理
            List<Map<String, Object>> resultList=LMUserServiceImpl.formatLMUserList2ArrayList(list);
            
            resultJson.put("rows", resultList);//结果返回
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("查询雷鸣用户列表");
            LOGGER.error(errorMsg, e);
            return ProcessUtil.returnError(500, errorMsg);
        }
        return ProcessUtil.returnCorrect(resultJson);
    }
    
    /**
     * 3.1 跳转到add页面
     */
    @RequestMapping(URL_PREFIX+"/query/addUI")
    public ModelAndView addUI(HttpServletRequest request) throws ToJSPException{
        ModelAndView mav=new ModelAndView(JSP_PREFIX+"/add");
        try {
            JSONArray jsonArray=new JSONArray();
            jsonArray.addAll(subjectService.listForZtree());
            mav.addObject("list",jsonArray);
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("跳转到雷鸣用户增加页面");
            LOGGER.error(errorMsg, e);
            throw new ToJSPException(errorMsg);
        }
        return mav;
    } 
    
    
    /**
     * 3.2 用于增加一条记录
     */
    @RequestMapping(value=URL_PREFIX+"/add")
    @ResponseBody
    public JSONObject add(LMUser record,Short... sIds){
        resultJson=new JSONObject();
        try {
            lMUserService.insert(record,sIds);
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("增加一个雷鸣用户");
            LOGGER.error(errorMsg, e);
            return ProcessUtil.returnError(500, errorMsg);
        }
        return ProcessUtil.returnCorrect(resultJson);
    }
    
    /**
     * 4 delete
     * 测试：service方法加了事务，那么它调用mapper应该会exception，那么就会Rollback
     * 先在界面选择3条数据，然后手动从数据库删除其中1条，
     * 然后界面发送删除请求，看看是否回滚
     */
    @RequestMapping(value=URL_PREFIX+"/delete")
    @ResponseBody
    public JSONObject delete(Long... ids){
        resultJson=new JSONObject();
        try {
            lMUserService.deleteByPrimaryKeys(ids);
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("删除雷鸣用户");
            LOGGER.error(errorMsg, e);
            return ProcessUtil.returnError(500, errorMsg);
        }
        return ProcessUtil.returnCorrect(resultJson);
    }
    
    /**
     * 5.1 跳转到update页面
     */
    @RequestMapping(URL_PREFIX+"/query/updateUI")
    public ModelAndView updateUI(Long id) throws ToJSPException{
        ModelAndView mav=new ModelAndView(JSP_PREFIX+"/update");
        try {
            //雷鸣用户
            LMUser record=lMUserService.selectByPrimaryKey(id);
            Map<String,Object> resultMap=subjectService.listByUserId(id);
            //查询出角色的权限列表
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> sList=(List<Map<String, Object>>)resultMap.get("listAllWithChkSign");
            
            
            //将pList放入放回结果
            JSONArray jsonArray=new JSONArray();
            jsonArray.addAll(sList);
            
            mav.addObject("sIds", resultMap.get("sIds"));
            mav.addObject("sZhNames", resultMap.get("sZhNames"));
            mav.addObject("list", jsonArray);
            mav.addObject("lmUser", record);//结果返回
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("跳转到雷鸣用户修改页面");
            LOGGER.error(errorMsg, e);
            throw new ToJSPException(errorMsg);
        }
        return mav;
    } 

    /**
     * 5.2 update
     */
    @RequestMapping(value=URL_PREFIX+"/update")
    @ResponseBody
    public JSONObject update(LMUser record,Short... sIds){
        resultJson=new JSONObject();
        try {
            lMUserService.updateByPrimaryKeySelective(record,sIds);
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("修改一个雷鸣用户");
            LOGGER.error(errorMsg, e);
            return ProcessUtil.returnError(500, errorMsg);
        }
        return ProcessUtil.returnCorrect(resultJson);
    }
    
    /**
     * 6 唯一性检查
     */
    @RequestMapping(value=URL_PREFIX+"/query/check")
    @ResponseBody
    public boolean check(LMUser record,Long id) throws Exception{
        //只能传递一个值过来
        String willBeOperatedPro=null;//the chosen one
        String originalOne=null;//the original one
        String userName=record.getUserName();
        String userMac=record.getUserMac();
        String userProofRule=record.getUserProofRule();
        
        int nameCount=0;
        int macCount=0;
        int ruleCount=0;
        if (StringUtils.isNotBlank(userName)) {
            nameCount=1;
            willBeOperatedPro=userName;
            if (null!=id && 0!=id) {//修改的情况
                originalOne=lMUserService.selectByPrimaryKey(id).getUserName();
            }
        }
        if (StringUtils.isNotBlank(userMac)) {
            macCount=1;
            willBeOperatedPro=userMac;
            if (null!=id && 0!=id) {//修改的情况
                originalOne=lMUserService.selectByPrimaryKey(id).getUserMac();
            }
        }
        if (StringUtils.isNotBlank(userProofRule)) {
            ruleCount=1;
            willBeOperatedPro=userProofRule;
            if (null!=id && 0!=id) {//修改的情况
                originalOne=lMUserService.selectByPrimaryKey(id).getUserProofRule();
            }
        }
        
        //判断
        if (nameCount+macCount+ruleCount!=1) {
            throw new Exception("parameter is wrong,please check it");
        }
        
        if (null!=id && 0!=id) {//修改的情况
            if(willBeOperatedPro.equals(originalOne)){
                return true;
            }
        }
        return lMUserService.checkUnique(record);
    }
    
    
    /*********************Business Process***************************/
    /**
     * 登陆服务
     */
    @RequestMapping(value="/service/"+URL_PREFIX+"/login")
    @ResponseBody
    public JSONObject serviceLogin(LMUser record){
        resultJson=new JSONObject();
        try {
            LMUser lmUser=lMUserService.serviceLogin(record);
            if (null==lmUser) {
                return ProcessUtil.returnError(403, "您还没有登陆");
            }
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("登陆时");
            LOGGER.error(errorMsg, e);
            return ProcessUtil.returnError(500, errorMsg);
        }
        return ProcessUtil.returnCorrect(resultJson);
    }
    
}