package com.expect.admin.web;

import com.expect.admin.data.dataobject.WorkFlow;
import com.expect.admin.data.pojo.Addwf;
import com.expect.admin.data.pojo.WfDetail;
import com.expect.admin.data.pojo.WfInfo;
import com.expect.admin.service.WFPointService;
import com.expect.admin.service.WorkFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * description:流程增删改的操作
 * Created by gaoyw on 2018/5/6.
 */
@Controller
@RequestMapping("/workf")
public class WorkFlowController {
    private final Logger log = LoggerFactory.getLogger(WorkFlowController.class);
    @Autowired
    WorkFlowService workFlowService;
    @Autowired
    WFPointService wfPointService;


    /**
     * 新增流程以及它的节点
     * @param addwf
     */
    @RequestMapping("/addPoints")
    public void add(Addwf addwf){
        WorkFlow workFlow = workFlowService.addWF(addwf);
        wfPointService.addPoints(addwf,workFlow);
    }

    /**
     * 更新流程的名称和描述
     * @param id
     * @param name
     * @param description
     */
    @RequestMapping("/updateInfo")
    public void updateInfo(String id,String name,String description){
        workFlowService.updWfNamAndDes(id,name,description);
    }

    /**
     * 获取所有流程的信息
     */
    @RequestMapping("/getAllWfInfo")
    @ResponseBody
    public List<WfInfo> getAllWfInfo(){
        return workFlowService.getAllWfInfo();
    }

    @RequestMapping("/getOneWfInfo")
    @ResponseBody
    public WfDetail getOneWfInfo(String id){
        return workFlowService.getWfInfoById(id);
    }

    public void testadd(){
        Addwf addwf = new Addwf();
        List wfpn = Arrays.asList("测试名称1","测试名称2","测试名称3");
        addwf.setName("测试名称");
        addwf.setDescription("这是用于测试的");
        addwf.setType("测试类别");
        addwf.setWfpnames(wfpn);
        add(addwf);
    }

}
