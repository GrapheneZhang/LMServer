package com.lm.busi.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.lm.base.ToJSPException;
import com.lm.busi.model.LMUser;
import com.lm.busi.service.LMUserService;
import com.lm.busi.service.SubjectService;
import com.lm.busi.service.impl.LMUserServiceImpl;
import com.lm.utils.ProcessUtil;
import com.lm.utils.Validate;

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
    @RequestMapping(URL_PREFIX+"/add/addUI")
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
    @RequestMapping(URL_PREFIX+"/update/updateUI")
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
    
    /**
     * 7 
     * 跳转到Excel导入页面
     */
    @RequestMapping(URL_PREFIX+"/add/excelUI")
    public String excelimportUI() throws ToJSPException{
        try {
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("跳转到雷鸣用户Excel导入页面");
            LOGGER.error(errorMsg, e);
            throw new ToJSPException(errorMsg);
        }
        return JSP_PREFIX+"/excelimport";
    }
    
    /**
     * 8
     * excel上传
     */
    @RequestMapping(URL_PREFIX+"/add/excel")
    @ResponseBody
    public JSONObject excelimport(MultipartFile excelFile) throws ToJSPException{
        resultJson=new JSONObject();
        HSSFWorkbook workbook=null;
        try {
            String fileName=excelFile.getOriginalFilename();
            int fileNameLength=fileName.length();
            if(!".xls".equals(fileName.substring(fileNameLength-4, fileNameLength))){
                return ProcessUtil.returnError(resultJson, "文件格式错误,必须为.xls");
            }
            //1 获取excel
            workbook=new HSSFWorkbook(excelFile.getInputStream());
            HSSFSheet sheet=workbook.getSheetAt(0);
            //空表返回
            if (sheet.getLastRowNum()==0) {
                return ProcessUtil.returnError(resultJson, "Excel数据为空");
            }
            Iterator<Row> iterator=sheet.rowIterator();
            //2 迭代row
            List<Map<String,Object>> rowList=new ArrayList<Map<String,Object>>();
            Map<String,Object> map=null;//用来存放手机号和科目
            LMUser lmUser=null;//临时雷鸣用户对象
            
            //获取sheet中显示指明的记录数
            long totalRow=(long)sheet.getRow(0).getCell(2).getNumericCellValue();
            iterator.next();//跳过第一行
            Row tempRow=null;
            for (long j = 0; j < totalRow; j++) {
                tempRow=iterator.next();
                //2.1 数据校验
                //手机号码
                long userName=(long)tempRow.getCell(0).getNumericCellValue();//取String会出错，因为是手机号码
                if (userName<9999999999l||userName>19999999999l) {//不是手机号码
                    StringBuffer sb=new StringBuffer("第").append(tempRow.getRowNum()+1)
                            .append("行手机号码格式有误。");
                    return ProcessUtil.returnError(resultJson,sb.toString());
                }
                //科目类型
                Cell tempCell = tempRow.getCell(1);
                tempCell.setCellType(Cell.CELL_TYPE_STRING);
                String stringSids=tempCell.getStringCellValue();
                if (!Validate.isStringWithComma(stringSids)) {
                    StringBuffer sb=new StringBuffer("第").append(tempRow.getRowNum()+1)
                            .append("行科目类型格式有误。");
                    return ProcessUtil.returnError(resultJson,sb.toString());
                }
                //2.2 操作
                map=new HashMap<String,Object>();
                lmUser=new LMUser();
                lmUser.setUserName(String.valueOf(userName));
                map.put("lmUser", lmUser);//临时雷鸣用户对象
                map.put("sIds", stringSids.split(","));//科目
                rowList.add(map);
            }
            
            //3 批量插入
            int count=lMUserService.insertList(rowList);
            resultJson.put("correctMsg", "成功插入了"+count+"条记录");
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("请确保Excel格式正确或联系管理员，在操作Excel");
            if (e instanceof DuplicateKeyException) {
                String duplicateKey=((DuplicateKeyException)e).getLocalizedMessage();
                errorMsg=new StringBuffer("用户名(手机号)：").
                        append(Validate.findMobilePhone(duplicateKey)).append("已被使用，数据库插入")
                        .toString();
                errorMsg=ProcessUtil.formatErrMsg(errorMsg);
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
    
    
    /*********************Business Process***************************/
    /**
     * 登陆服务
     */
    @RequestMapping(value="/service/"+URL_PREFIX+"/login")
    @ResponseBody
    public JSONObject serviceLogin(LMUser record){
        resultJson=new JSONObject();
        try {
            //非空
            if (StringUtils.isBlank(record.getUserName())
                    ||StringUtils.isBlank(record.getUserMac())
                    ||StringUtils.isBlank(record.getUserProofRule())) {
                return ProcessUtil.returnError(3, "参数不能为空");
            }
            //操作
            LMUser lmUser=lMUserService.serviceLogin(record);
            if (null==lmUser) {
                return ProcessUtil.returnError(2, "您还没有注册");
            }
            Map<String, Object> map=subjectService.listByUserId(lmUser.getId());
            resultJson.put("userPermission", (String)map.get("sEnNames"));
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("登陆");
            LOGGER.error(errorMsg, e);
            return ProcessUtil.returnError(500, errorMsg);
        }
        return ProcessUtil.returnLMCorrect(resultJson);
    }
    
    /**
     * 注册服务
     */
    @RequestMapping(value="/service/"+URL_PREFIX+"/signup")
    @ResponseBody
    public JSONObject serviceSignUp(LMUser record){
        resultJson=new JSONObject();
        try {
            String userName=record.getUserName();
            String userMac=record.getUserMac();
            //非空
            if (StringUtils.isBlank(userName)
                    ||StringUtils.isBlank(userMac)) {
                return ProcessUtil.returnError(500, "参数不能为空");
            }
            //数据非法
            if (userName.length()!=11
                    ||userMac.length()<16) {
                return ProcessUtil.returnError(4, "参数格式不正确");
            }
            
            //已经注册
            LMUser paramUser=new LMUser();
            paramUser.setUserName(userName);
            if (null!=lMUserService.serviceLogin(paramUser)) {
                return ProcessUtil.returnError(3, "您已经注册");
            }
            //没有注册权限
            LMUser seletedOne=lMUserService.servicePermitSignUp(paramUser);
            if (null==seletedOne) {
                return ProcessUtil.returnError(2, "您还没有注册资格");
            }
            //注册
            StringBuilder proofRule = new StringBuilder(userName.substring(0, 5) + userMac.substring(5, 15));
            seletedOne.setUserMac(userMac);
            seletedOne.setUserProofRule(proofRule.toString());
            lMUserService.serviceSignUp(seletedOne);
        } catch (Exception e) {
            String errorMsg=ProcessUtil.formatErrMsg("注册");
            LOGGER.error(errorMsg, e);
            return ProcessUtil.returnError(500, errorMsg);
        }
        return ProcessUtil.returnLMCorrect(resultJson);
    }
}