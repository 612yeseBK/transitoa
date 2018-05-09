package com.expect.admin.web;

import com.expect.admin.data.dataobject.WorkFlow;
import com.expect.admin.data.pojo.Addwf;
import com.expect.admin.service.WFPointService;
import com.expect.admin.service.WorkFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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


    @RequestMapping("/addPoints")
    public void add(Addwf addwf){
        WorkFlow workFlow = workFlowService.addWF(addwf);
        wfPointService.addPoints(addwf,workFlow);
    }


}
