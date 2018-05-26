package com.expect.admin.web;

import com.expect.admin.data.dao.FunctionRepository;
import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.data.dataobject.WorkFlow;
import com.expect.admin.data.pojo.*;
import com.expect.admin.service.WFPointService;
import com.expect.admin.service.WorkFlowService;
import com.expect.admin.utils.JsonResult;
import com.expect.admin.utils.JsonUtil;
import com.expect.admin.utils.MyResponseBuilder;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * description:流程增删改的操作
 * Created by gaoyw on 2018/5/6.
 */
@Controller
@RequestMapping("/admin/workf")
public class WorkFlowController {
    private final Logger log = LoggerFactory.getLogger(WorkFlowController.class);
    private final String viewName = "admin/system/workflow/";

    @Autowired
    WorkFlowService workFlowService;
    @Autowired
    WFPointService wfPointService;
    @Autowired
    UserRepository userRepository;


    @RequestMapping("/getAdd")
    public ModelAndView getAdd(){
        ModelAndView mv = new ModelAndView(viewName+"workflow_add");
        return mv;
    }


    @RequestMapping("/getTypes")
    @ResponseBody
    public Map<String, String> getTypes(HttpServletRequest request, HttpServletResponse response){
        Map map1 = new HashMap();
        for (Map.Entry<String, String> entry : WorkFlow.map.entrySet()) {
            map1.put(entry.getKey(),entry.getValue());
        }
        System.out.println(JsonUtil.getInstance().toJson(map1));
        return WorkFlow.map;
    }


    @RequestMapping("/getFucs")
    @ResponseBody
    public List<IdName> getFucs(HttpServletRequest request, HttpServletResponse response){
        return workFlowService.getFunctions();
    }

    /**
     * 新增流程以及它的节点
     * @param wfDetail
     */
    @RequestMapping("/addOrUpd")
    public void add(String wfDetail,HttpServletRequest request, HttpServletResponse response){
        WfDetail wfDetail1 = JsonUtil.getInstance().fromJson(wfDetail,WfDetail.class);
        //前端传来的id是0时，就代表新增
        if (wfDetail1.getId().equals("0")) {
            WorkFlow workFlow = workFlowService.addWF(wfDetail1);
            wfPointService.addPoints(wfDetail1, workFlow);
        } else{
            //如果不是0，那么就是对它的修改，只能改流程名称，流程描述，节点名称，节点人员，基本上重新载入就行了
            workFlowService.updWfNamAndDes(wfDetail1.getId(),wfDetail1.getName(),wfDetail1.getDescription());
            System.out.println(JsonUtil.getInstance().toJson(wfDetail1));
            List<WfpInfo> wfIs = wfDetail1.getWfpInfos();
            wfPointService.updateWfp(wfIs);
        }
        try {
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "添加成功").build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/getOneWfInfo")
    @ResponseBody
    public WfDetail getOneWfInfo(String id){
        WfDetail wfDetail = workFlowService.getWfInfoById(id);
        return wfDetail;
    }


    @RequestMapping("/abortWf")
    public void abortWf(String id,HttpServletRequest request, HttpServletResponse response){
        workFlowService.sorftDelete(id);
        try {
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "添加成功").build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/reuse")
    public void reuse(String id,HttpServletRequest request, HttpServletResponse response){
        workFlowService.reuse(id);
        try {
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(true, "添加成功").build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有流程的信息
     */
    @RequestMapping("/getAllWfInfo")
    @ResponseBody
    public List<WfInfo> getAllWfInfo(HttpServletRequest request, HttpServletResponse response){
        return workFlowService.getAllWfInfo();
    }


    @RequestMapping("/getUserFromUserName")
    @ResponseBody
    public Map getUserFromUserName(String username, HttpServletRequest request, HttpServletResponse response){
        System.out.println(username);
        User user ;
        IdName idName = new IdName();
        Map<String,Object> map = new HashMap();
        try {
            user = userRepository.findByUsername(username);
            idName.setName(user.getFullName()+"("+user.getUsername()+")");
            idName.setId(user.getId());
            map.put("idName", idName);
            map.put("result", true);
            return map;
        } catch (NullPointerException e) {
            map.put("result", false);
        }
        return map;

    }




}

