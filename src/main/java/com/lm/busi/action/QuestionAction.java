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
import com.lm.busi.model.Question;
import com.lm.busi.service.QuestionService;
import com.lm.busi.service.SubjectService;
import com.lm.utils.ProcessUtil;

@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
@Controller
public class QuestionAction {
    
    private static final Logger LOGGER=Logger.getLogger(QuestionAction.class);//日志
    private static final String URL_PREFIX="/question";
    protected static final String JSP_PREFIX="/WEB-INF/jsp/busi/question";//通用的jsp前缀
    
    @Resource
    private QuestionService questionService;
    @Resource
    private SubjectService subjectService;

    private JSONObject resultJson=null;//结果JSON，使用时先new
    
    
    /*********************System Process***************************/

    /**
     * 1 代表此题目
     * 跳转到list页面
     */
    @RequestMapping(URL_PREFIX+"/query")
    public String list() throws ToJSPException{
        try {
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("跳转到题目页面");
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
                list=questionService.listForCRUD(map);
            }else{//手写分页查询
                PageHelper.startPage(page, rows);
                list=questionService.listForCRUD(map);
                PageInfo<Map<String,Object>> p = new PageInfo<Map<String,Object>>(list);//取出分页统计信息statistic
                resultJson.put("total", p.getTotal());
            }
            //结果处理
            ProcessUtil.formatPage2ArrayList(list);//将list(Page<E>)转为他的父类ArrayList(不是Page也无所谓),并格式化时间
            
            resultJson.put("rows", list);//结果返回
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("查询题目列表");
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
            mav.addObject("question", new Question());
            mav.addObject("list",subjectService.listModels(new HashMap<String,Object>()));
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("跳转到题目增加页面");
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
    public JSONObject add(Question record){
        resultJson=new JSONObject();
        try {
            questionService.insertSelective(record);
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("增加一个题目");
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
            questionService.deleteByPrimaryKeys(ids);
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("删除题目");
            LOGGER.error(errorMsg, e);
            return ProcessUtil.returnError(500, errorMsg);
        }
        return ProcessUtil.returnCorrect(resultJson);
    }
    
    /**
     * 5.1 跳转到update页面
     */
    @RequestMapping(URL_PREFIX+"/update/updateUI")
    public ModelAndView updateUI(Long id) throws ToJSPException{
        ModelAndView mav=new ModelAndView(JSP_PREFIX+"/update");
        try {
            //题目
            Question record=questionService.selectByPrimaryKey(id);
            mav.addObject("list", subjectService.listModels(new HashMap<String,Object>()));
            mav.addObject("question", record);//结果返回
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("跳转到题目修改页面");
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
    public JSONObject update(Question record){
        resultJson=new JSONObject();
        try {
            questionService.updateByPrimaryKeySelective(record);
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("修改一个题目");
            LOGGER.error(errorMsg, e);
            return ProcessUtil.returnError(500, errorMsg);
        }
        return ProcessUtil.returnCorrect(resultJson);
    }
    
    /*********************Business Process***************************/
    /**
     * 登陆服务
     */
    @RequestMapping(value="/service/"+URL_PREFIX+"/list")
    @ResponseBody
    public JSONObject serviceList(String type){
        resultJson=new JSONObject();
        try {
            //非空
            if (StringUtils.isBlank(type)) {
                return ProcessUtil.returnError(4, "type参数不能为空");
            }
            //操作
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("subjectEnName", type);
            List<Map<String, Object>> list=questionService.serviceList(map);
            //没有数据
            if (list.size()<1) {
                return ProcessUtil.returnError(3, "没有数据");
            }
            resultJson.put("titles", list);
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("服务：查询题目");
            LOGGER.error(errorMsg, e);
            return ProcessUtil.returnError(500, errorMsg);
        }
        return ProcessUtil.returnLMCorrect(resultJson);
    }
    
}