package com.expect.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * description:
 * Created by gaoyw on 2018/5/14.
 */
@Controller
@RequestMapping("/transper")
public class TransferPersonnelController {

    /**
     * 根据登录用户获取所有的他可以申请的人员借调流程
     */
    @RequestMapping("/getAllowForms")
    public void getAllowForms(){

    }

    /**
     * 根据流程id，将申请表发送给他
     * @param id
     */
    @RequestMapping("/getOneFrom")
    public void getOneFrom(String id){

    }

    /**
     * 接受用户提交的表格
     */
    @RequestMapping("/addForm")
    public void addForm(){

    }

    /**
     * 获取登录用户未提交的记录
     */
    @RequestMapping("/getNotCom")
    public void getNotCom(){

    }

    /**
     * 获取登录用户所有申请中处于审批流程中的那些
     */
    @RequestMapping("/getWaitings")
    public void getWaitings(){

    }

    /**
     * 获取登录用户所有申请中已经审核完成的那些
     * 包括审批通过和未通过的（PASSED和REJECTED)
     */
    @RequestMapping("/getFinished")
    public void getFinished(){

    }

    /**
     * 获取登录用户需要审批的表单
     */
    @RequestMapping("/getCheckForms")
    public void getCheckForms(){

    }

    /**
     * 根据id获取用户选中的审核表格
     * @param id
     */
    @RequestMapping("/getOneCheck")
    public void getOneCheck(String id){

    }

    /**
     * 提交审核结果
     */
    @RequestMapping("/commitCheck")
    public void commitCheck(){

    }

    /**
     * 获取登录用户已经审批的表单
     */
    @RequestMapping("/getCheckedForms")
    public void getCheckedForms(){

    }

    /**
     * 根据id获取用户选中的已审核表格
     * @param id
     */
    @RequestMapping("/getOneChecked")
    public void getOneChecked(String id){

    }
}
