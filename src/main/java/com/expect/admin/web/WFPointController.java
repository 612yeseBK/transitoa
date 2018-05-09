package com.expect.admin.web;

import com.expect.admin.data.dataobject.WorkFlow;
import com.expect.admin.data.pojo.Addwf;
import com.expect.admin.data.pojo.Addwfp;
import com.expect.admin.service.WFPointService;
import com.expect.admin.service.WorkFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * description:
 * Created by gaoyw on 2018/5/6.
 */
@Controller
@RequestMapping("/wfp")
public class WFPointController {
    private final Logger log = LoggerFactory.getLogger(WFPointController.class);
    @Autowired
    WorkFlowService workFlowService;
    @Autowired
    WFPointService wfPointService;

    /**
     * 为各个流程点增加人员
     * @param addwfps
     */
    @RequestMapping("/addUsers")
    public void addUsers(List<Addwfp> addwfps){
       wfPointService.addwfpUsers(addwfps);
    }

}
