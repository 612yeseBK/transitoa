package com.expect.admin.service;

import com.expect.admin.data.dao.WorkFlowRepository;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.data.dataobject.WFPoint;
import com.expect.admin.data.dataobject.WorkFlow;
import com.expect.admin.data.pojo.Addwf;
import com.expect.admin.data.pojo.WfDetail;
import com.expect.admin.data.pojo.WfInfo;
import com.expect.admin.data.pojo.WfpInfo;
import com.expect.admin.service.vo.WorkFlowVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 根据流程类别和申请者姓名找到该申请者所有允许的申请，并返回
     * @param type
     * @param user
     * @return
     */
    public List<WorkFlow> findAplt(String type,User user){
        List<WorkFlow> aplt = new ArrayList<>();
        List<WorkFlow> wfs = workFlowRepository.findAllByType(type);
        for (WorkFlow wf : wfs){
            if (wf.getBeginPoint().getRole().getUsers().contains(user)){
                aplt.add(wf);
            }
        }
        return aplt;
    }

    /**
     * 根据可以申请的流程获取返回前端的数据
     * @param wkfls
     * @return
     */
    public List<WorkFlowVo> getVoFromWfs(List<WorkFlow> wkfls){
        List<WorkFlowVo> wfvos = new ArrayList<>();
        for (WorkFlow wf : wkfls){
            wfvos.add(new WorkFlowVo(wf));
        }
        return wfvos;
    }


    /**
     * 根据流程id获取该流程的第一个节点
     * @param wkfid
     * @return
     */
    public WFPoint getBeginPoint(String wkfid){
        WorkFlow workFlow = findOne(wkfid);
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
        workFlow = save(workFlow);
        return workFlow;
    }

    /**
     * 对流程的名称和描述进行修改
     * @param id
     * @param name
     * @param descpt
     */
    @Transactional
    public void updWfNamAndDes(String id,String name,String descpt){
        WorkFlow workFlow = workFlowRepository.findOne(id);
        workFlow.setName(name);
        workFlow.setDescription(descpt);
        workFlowRepository.save(workFlow);
    }

    /**
     * 获取所有的流程简略信息
     * @return
     */
    public List<WfInfo> getAllWfInfo(){
        List<WorkFlow> workFlows = findAll();
        List<WfInfo> wfInfos = new ArrayList<>();
        WfInfo wfInfo = new WfInfo();
        for (WorkFlow wf : workFlows){
            wfInfo.setId(wf.getId());
            wfInfo.setName(wf.getName());
            wfInfo.setDescription(wf.getDescription());
            wfInfos.add(wfInfo);
            wfInfo = new WfInfo();
        }
        return wfInfos;
    }

    /**
     * 根据id获取该流程的详细信息（包括节点）
     * @param id
     * @return
     */
    public WfDetail getWfInfoById(String id){
        WfDetail wfDetail = new WfDetail();
        WorkFlow workFlow = findOne(id);
        wfDetail.setName(workFlow.getName());
        wfDetail.setDescription(workFlow.getDescription());
        wfDetail.setId(workFlow.getId());
        List<WfpInfo> wfpIs = new ArrayList<>();
        WfpInfo wfpInfo = new WfpInfo();
        List<Map> maps = new ArrayList<>();
        Map map = new HashMap();
        WFPoint wfPoint = workFlow.getBeginPoint();
        if (!wfPoint.getName().equals(WFPoint.ENDPOINT)){
            wfpInfo.setId(wfPoint.getId());
            wfpInfo.setName(wfPoint.getName());
            for (User user : wfPoint.getRole().getUsers()){
                map.put("id",user.getId());
                map.put("name",user.getUsername());
                maps.add(map);
                map = new HashMap();
            }
            wfpInfo.setUsersInfos(maps);
            wfpIs.add(wfpInfo);
            wfpInfo = new WfpInfo();
        }
        wfDetail.setWfpInfos(wfpIs);
        return wfDetail;
    }

    @Transactional
    public WorkFlow findOne(String id){
        return workFlowRepository.findOne(id);
    }

    @Transactional
    public List<WorkFlow> findAll(){
        return workFlowRepository.findAll();
    }

    @Transactional
    public WorkFlow save(WorkFlow workFlow){
        return workFlowRepository.save(workFlow);
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
    @Transactional
    public void delete(String id){
        WorkFlow workFlow = workFlowRepository.findOne(id);
        workFlow.setType(WorkFlow.DELETE);
        workFlowRepository.save(workFlow);
    }

    /**
     * 真正意义上删除
     * 先删除该流程的过去申请的审批记录
     * 再删除该流程过去的申请记录
     * 再删除该流程的节点
     * 再删除该流程，所以非常麻烦，事实上可以做一下cascade的级联，但是我就不做了，太麻烦了
     * @param id
     */
    public void realDelete(String id){

    }
}
