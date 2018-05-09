package com.expect.admin.service;

import com.expect.admin.data.dao.WorkFlowRepository;
import com.expect.admin.data.dataobject.WFPoint;
import com.expect.admin.data.dataobject.WorkFlow;
import com.expect.admin.data.pojo.Addwf;
import com.expect.admin.web.WFPointController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * description:更新需求后的流程，不包括合同，收发文，会议
 * Created by gaoyw on 2018/5/6.
 */
@Service
public class WorkFlowService {
    private final Logger log = LoggerFactory.getLogger(WorkFlowService.class);
    @Autowired
    WorkFlowRepository workFlowRepository;

    /**
     * 根据流程id获取该流程的第一个节点
     * @param wkfid
     * @return
     */
    public WFPoint getBeginPoint(String wkfid){
        WorkFlow workFlow = workFlowRepository.findOne(wkfid);
        WFPoint wfPoint = workFlow.getBeginPoint();
        return wfPoint;
    }

    /**
     * 建立新的流程
     */
    public WorkFlow addWF(Addwf addwf){
        WorkFlow workFlow = new WorkFlow();
        workFlow.setName(addwf.getName());
        workFlow.setType(addwf.getType());
        workFlow.setDescription(addwf.getDescription());
        workFlow = workFlowRepository.save(workFlow);
        return workFlow;
    }

    /**
     * 根据id删除这个流程
     * 删除流程要考虑到存在一些已经在流程里面的申请审批
     * 这时候我们可以审查，如果有，就进行提示
     * 如果删除了，那么正在审批中的申请会全部作废，
     * 删除只是不让这个流程再被申请，流程依然存在，考虑到流程存在一个字段type，我这里直接将这个字段修改为delete
     * 这时候再考虑历史记录的问题，我给user表格加了一个属性，可以直接查找到用户的调用申请记录，通过申请记录可以直接找到审批记录
     * 所以，删除流程并不影响到我的查找，查找之后展示记录需要由申请记录找到流程，通过流程的type去判断这个申请是否已经弃用，弃用的话
     * 就不再加入原有的体系，可以额外新建一个新的表格，专门存放
     * @param id
     */
    public void delete(String id){

    }
}
