package com.expect.admin.web;

import com.expect.admin.data.dataobject.TransPerRecord;
import com.expect.admin.data.dataobject.TransferPersonnel;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.data.dataobject.WorkFlow;
import com.expect.admin.service.TransferPersonnelService;
import com.expect.admin.service.UserService;
import com.expect.admin.service.WorkFlowService;
import com.expect.admin.service.vo.*;
import com.expect.admin.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * Created by gaoyw on 2018/5/14.
 */
@Controller
@RequestMapping("/transper")
public class TransferPersonnelController {

    @Autowired
    WorkFlowService wfs;
    @Autowired
    UserService userService;
    @Autowired
    TransferPersonnelService tpService;
    /**
     * 根据登录用户获取所有的他可以申请的人员借调流程
     */
    @RequestMapping("/getAllowForms")
    @ResponseBody
    public List<WorkFlowVo> getAllowForms(){
        User logUser = userService.getLogUsr();
        List<WorkFlow> wkfls = wfs.findAplt(WorkFlow.TRANS_PERSON,logUser);
        List<WorkFlowVo> wfvos = wfs.getVoFromWfs(wkfls);
        return wfvos;
    }

    /**
     * 根据流程id，将申请表发送给他,同时还有该登录用户的相关信息，填到表中合适的地方
     * @param id
     */
    @RequestMapping("/getOneFrom")
    public void getOneFrom(String id){
        WorkFlow workFlow = wfs.findOne(id);
        UserVo userVo = userService.getLoginUser();
    }

    /**
     * 添加但是未提交 ,我决定不提供保存功能了，太累了
     * @param wfid 流程id
     * @param tpVo 用户填写的信息
     */
    @RequestMapping("/addNotCom")
    public void addNotCom(String wfid,TransferPersonnelVo tpVo){
       tpService.add(tpVo,wfid,TransferPersonnel.NOTCOM);
    }

    /**
     * 提交
     * @param wfid
     * @param tpVo
     */
    @RequestMapping("/addAndCom")
    public void addAndCom(String wfid, TransferPersonnelVo tpVo){
        tpService.add(tpVo, wfid, TransferPersonnel.WAITING);
    }

    @RequestMapping("/commitSaved")
    public  void commitSaved(String tpid,TransferPersonnelVo tpVo){
        tpService.commitSaved(tpVo,tpid);
    }

    /**
     * 获取登录用户未提交的记录
     */
    @RequestMapping("/getNotCom")
    @ResponseBody
    public List<TransferPersonnelVo> getNotCom(){
        User user = userService.getLogUsr();
        return tpService.getTransPerByState(user,new String[]{TransferPersonnel.NOTCOM});
    }

    /**
     * 获取登录用户所有申请中处于审批流程中的那些
     */
    @RequestMapping("/getWaitings")
    public List<TransferPersonnelVo> getWaitings(){
        User user = userService.getLogUsr();
        return tpService.getTransPerByState(user,new String[]{TransferPersonnel.WAITING});
    }

    /**
     * 获取登录用户所有申请中已经审核完成的那些
     * 包括审批通过和未通过的（PASSED和REJECTED)
     */
    @RequestMapping("/getFinished")
    @ResponseBody
    public List<TransferPersonnelVo> getFinished(){
        User user = userService.getLogUsr();
        return tpService.getTransPerByState(user,new String[]{TransferPersonnel.PASSED,TransferPersonnel.REJECTED});
    }

    /**
     * 获取登录用户所有需要审批的表单
     */
    @RequestMapping("/getCheckForms")
    public List<TransferPersonnelVo> getCheckForms(){
        User user = userService.getLogUsr();
        return tpService.getCheckForms(user);
    }

    /**
     * 根据id获取用户选中的审核表格,这里需要返回的是表单信息和已经审核的记录信息
     * @param id transper的id
     */
    @RequestMapping("/getOneCheck")
    @ResponseBody
    public List<TransPerRecordVo> getOneCheck(String id){
        TransferPersonnel tp = tpService.findById(id);
        // todo
        return tpService.getRecdsByTransId(id);
    }

    /**
     *
     * @param id 人员调用表的id
     * @param message 处理意见
     * @param cljg 处理结果
     */
    @RequestMapping("/commitCheck")
    public void commitCheck(String id,String message,String cljg){
        tpService.checkApply(id,message,cljg);
    }

    /**
     * 获取登录用户已经审批的表单
     */
    @RequestMapping("/getCheckedForms")
    @ResponseBody
    public List<TransferPersonnelVo> getCheckedForms(){
        User user = userService.getLogUsr();
        return tpService.getCheckedForms(user.getId());
    }

    /**
     * 根据id获取用户选中的已审核表格
     * 返回的内容包括，所有的审批记录，该用户的审批记录
     * @param tpid 借调表的id
     */
    @RequestMapping("/getOneChecked")
    public void getOneChecked(String tpid){
        User user = userService.getLogUsr();
        TransPerRecordVo tprVo = tpService.getRcdBytpAndUsr(user.getId(),tpid);
        List<TransPerRecordVo> tprVos = tpService.getRecdsByTransId(tpid);
    }
}
