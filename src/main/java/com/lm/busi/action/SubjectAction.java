package com.lm.busi.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lm.base.ToJSPException;
import com.lm.busi.model.Subject;
import com.lm.busi.service.SubjectService;
import com.lm.utils.ProcessUtil;

@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
@Controller
public class SubjectAction {
    
    private static final Logger LOGGER=Logger.getLogger(SubjectAction.class);//日志
    private static final String URL_PREFIX="/subject";
    protected static final String JSP_PREFIX="/WEB-INF/jsp/busi/subject";//通用的jsp前缀
    
    @Resource
    private SubjectService subjectService;

    private JSONObject resultJson=null;//结果JSON，使用时先new
    
    
    /*********************System Process***************************/

    /**
     * 1 代表此科目
     * 跳转到list页面
     */
    @RequestMapping(URL_PREFIX+"/query")
    public String list() throws ToJSPException{
        try {
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("跳转到科目页面");
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
            List<Map<String,Object>> list=null;//结果list
            //查询数据库
            if (null==page||null==rows) {//全量查询
                list=subjectService.listForCRUD(map);
            }else{//手写分页查询
                PageHelper.startPage(page, rows);
                list=subjectService.listForCRUD(map);
                PageInfo<Map<String,Object>> p = new PageInfo<Map<String,Object>>(list);//取出分页统计信息statistic
                resultJson.put("total", p.getTotal());
            }
            //结果处理
            ProcessUtil.formatPage2ArrayList(list);//将list(Page<E>)转为他的父类ArrayList(不是Page也无所谓),并格式化时间
            
            resultJson.put("rows", list);//结果返回
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("查询科目列表");
            LOGGER.error(errorMsg, e);
            return ProcessUtil.returnError(500, errorMsg);
        }
        return ProcessUtil.returnCorrect(resultJson);
    }
    
    /**
     * 3.1 跳转到add页面
     */
    @RequestMapping(URL_PREFIX+"/add/addUI")
    public ModelAndView addUI(HttpServletRequest request) throws ToJSPException{
        ModelAndView mav=new ModelAndView(JSP_PREFIX+"/add");
        try {
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("跳转到科目增加页面");
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
    public JSONObject add(Subject record){
        resultJson=new JSONObject();
        try {
            subjectService.insertSelective(record);
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("增加一个科目");
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
    public JSONObject delete(Short... ids){
        resultJson=new JSONObject();
        try {
            subjectService.deleteByPrimaryKeys(ids);
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("删除科目");
            LOGGER.error(errorMsg, e);
            return ProcessUtil.returnError(500, errorMsg);
        }
        return ProcessUtil.returnCorrect(resultJson);
    }
    
    /**
     * 5.1 跳转到update页面
     */
    @RequestMapping(URL_PREFIX+"/update/updateUI")
    public ModelAndView updateUI(Short id) throws ToJSPException{
        ModelAndView mav=new ModelAndView(JSP_PREFIX+"/update");
        try {
            //科目
            Subject record=subjectService.selectByPrimaryKey(id);
            mav.addObject("subject", record);//结果返回
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("跳转到科目修改页面");
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
    public JSONObject update(Subject record){
        resultJson=new JSONObject();
        try {
            subjectService.updateByPrimaryKeySelective(record);
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("修改一个科目");
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
    public boolean check(Subject record,Short id) throws Exception{
        //只能传递一个值过来
        String willBeOperatedPro=null;//the chosen one
        String originalOne=null;//the original one
        String enName=record.getEnName();
        String zhName=record.getZhName();
        
        int enCount=0;
        int zhCount=0;
        if (StringUtils.isNotBlank(enName)) {
            enCount=1;
            willBeOperatedPro=enName;
            if (null!=id && 0!=id) {//修改的情况
                originalOne=subjectService.selectByPrimaryKey(id).getEnName();
            }
        }
        if (StringUtils.isNotBlank(zhName)) {
            zhCount=1;
            willBeOperatedPro=zhName;
            if (null!=id && 0!=id) {//修改的情况
                originalOne=subjectService.selectByPrimaryKey(id).getZhName();
            }
        }
        
        //判断
        if (enCount+zhCount!=1) {
            throw new Exception("parameter is wrong,please check it");
        }
        
        if (null!=id && 0!=id) {//修改的情况
            if(willBeOperatedPro.equals(originalOne)){
                return true;
            }
        }
        return subjectService.checkUnique(record);
    }
    
    
    /*********************Business Process***************************/
}