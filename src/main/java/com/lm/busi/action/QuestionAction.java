package com.lm.busi.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lm.base.ToJSPException;
import com.lm.busi.model.Question;
import com.lm.busi.model.Subject;
import com.lm.busi.service.QuestionService;
import com.lm.busi.service.SubjectService;
import com.lm.utils.ProcessUtil;
import com.mysql.jdbc.PacketTooBigException;

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
    
    /**
     * 6
     * 跳转到Excel导入页面
     */
    @RequestMapping(URL_PREFIX+"/add/excelUI")
    public String excelimportUI() throws ToJSPException{
        try {
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("跳转到Excel导入页面");
            LOGGER.error(errorMsg, e);
            throw new ToJSPException(errorMsg);
        }
        return JSP_PREFIX+"/excelimport";
    }
    
    /**
     * 7
     * excel上传
     */
    @RequestMapping(URL_PREFIX+"/add/excel")
    @ResponseBody
    public JSONObject excelimport(MultipartFile excelFile) throws ToJSPException{
        resultJson=new JSONObject();
        HSSFWorkbook workbook=null;
        try {
            //1 获取excel
            workbook=new HSSFWorkbook(excelFile.getInputStream());
            HSSFSheet sheet=workbook.getSheetAt(0);
            //空表返回
            if (sheet.getLastRowNum()==0) {
                return ProcessUtil.returnError(resultJson, "Excel数据为空");
            }
            Iterator<Row> iterator=sheet.rowIterator();
            //2 迭代row
            List<Question> list=new ArrayList<Question>();
            Question question=null;//临时问题对象
            List<Subject> sList= subjectService.listModels(new HashMap<String,Object>());//合法的sId集合
            
            //获取sheet中显示指明的记录数
            long totalRow=(long)sheet.getRow(0).getCell(3).getNumericCellValue();
            iterator.next();//跳过第一行
            Row tempRow=null;
            for (long j = 0; j < totalRow; j++) {
                tempRow=iterator.next();
                //2.1 数据校验
                String content=tempRow.getCell(0).getStringCellValue();//内容
                String answer=tempRow.getCell(1).getStringCellValue();//答案
                short sId=(short)tempRow.getCell(2).getNumericCellValue();//科目
                //sId合法性校验
                boolean legal=false;
                sIdCheck:
                for (int i = 0; i < sList.size(); i++) {
                    if (sId==sList.get(i).getId()) {
                        legal=true;
                        break sIdCheck;
                    }
                }
                if (!legal) {
                    StringBuffer sb=new StringBuffer("第").append(tempRow.getRowNum()+1)
                            .append("行所属科目类型不存在或格式错误。");
                    return ProcessUtil.returnError(resultJson,sb.toString());
                }
                
                //2.2 操作
                question=new Question();
                question.setContent(content);
                question.setAnswer(answer);
                question.setSubject(sId);
                list.add(question);
            }
            
            //3 批量插入
            int count=questionService.insertListSelective(list);
            resultJson.put("correctMsg", "成功插入了"+count+"条记录");
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("请确保Excel格式正确或联系管理员，在操作Excel");
            if (e instanceof PacketTooBigException) {
                errorMsg=ProcessUtil.formatErrMsg("Excel文件");
            }
            
            LOGGER.error(errorMsg, e);
            return ProcessUtil.returnError(500, errorMsg);
        } finally{
            try {
                workbook.close();//关闭
            } catch (IOException e) {
                String errorMsg=ProcessUtil.formatErrMsg("关闭Excel");
                LOGGER.error(errorMsg, e);
                return ProcessUtil.returnError(500, errorMsg);
            }
        }
        return ProcessUtil.returnCorrect(resultJson);
    }
    
    /**
     * 8
     * 跳转到word导入页面
     */
    @RequestMapping(URL_PREFIX+"/add/wordUI")
    public ModelAndView wordImportUI() throws ToJSPException{
        ModelAndView mav=new ModelAndView(JSP_PREFIX+"/wordimport");
        try {
            mav.addObject("question", new Question());
            mav.addObject("list",subjectService.listModels(new HashMap<String,Object>()));
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("跳转到word导入页面");
            LOGGER.error(errorMsg, e);
            throw new ToJSPException(errorMsg);
        }
        return mav;
    }
    
    
    /**
     * 9
     * word上传
     */
    @RequestMapping(URL_PREFIX+"/add/word")
    @ResponseBody
    public JSONObject txtimport(MultipartFile wordFile,Short subject) throws ToJSPException{
        resultJson=new JSONObject();
        WordExtractor word=null;
        try {
            //1 获取Word
            word=new WordExtractor(wordFile.getInputStream());
            String text=word.getText();
            //空返回
            if (StringUtils.isBlank(text)) {
                return ProcessUtil.returnError(resultJson, "Word数据为空");
            }
            String[] questions=text.split("\\r\\n\\r\\n");//拿到每个题目
            
            //2 处理Word
            List<Question> list=new ArrayList<Question>();
            Question question=null;//临时问题对象
            for (int i = 0; i < questions.length; i++) {
                question=new Question();
                question.setSubject(subject);
                String[] stringQuestion=questions[i].split("&&");
                if (stringQuestion.length==2) {//正常记录
                    question.setContent(stringQuestion[0]);
                    question.setAnswer(stringQuestion[1]);
                }else if(stringQuestion.length==1){//只有问题可以
                    question.setContent(stringQuestion[0]);
                }else{//长度为3或者0之类的跳过本次循环
                    continue;
                }
                list.add(question);
            }
            
            //3 批量插入
            int count=questionService.insertListSelective(list);
            resultJson.put("correctMsg", "成功插入了"+count+"条记录");
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("请确保Word格式正确或联系管理员，在操作Word");
            if (e instanceof PacketTooBigException) {
                errorMsg=ProcessUtil.formatErrMsg("Word文件");
            }
            
            LOGGER.error(errorMsg, e);
            return ProcessUtil.returnError(500, errorMsg);
        } finally{
            try {
                word.close();
            } catch (IOException e) {
                String errorMsg=ProcessUtil.formatErrMsg("关闭Word");
                LOGGER.error(errorMsg, e);
                return ProcessUtil.returnError(500, errorMsg);
            }
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