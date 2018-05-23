package com.expect.admin.service;

import com.expect.admin.data.dao.*;
import com.expect.admin.data.dataobject.Role;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.data.dataobject.WFPoint;
import com.expect.admin.data.dataobject.WorkFlow;
import com.expect.admin.data.pojo.Addwf;
import com.expect.admin.data.pojo.Addwfp;
import com.expect.admin.data.pojo.UpdwfpU;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * description:
 * Created by gaoyw on 2018/5/6.
 */
@Service
public class WFPointService {
    private final Logger log = LoggerFactory.getLogger(WFPointService.class);
    @Autowired
    WFPointRepository wfPointRepository;
    @Autowired
    WorkFlowService workFlowService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    WorkFlowRepository workFlowRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    FunctionRepository functionRepository;

    /**
     *  为流程增加它的节点，暂时不增加人员
     * “end_节点_end”这个节点的前置指向流程的尾部，后置指向流程的首部
     * @param addwf
     * @param workFlow
     */
    @Transactional
    public void addPoints(Addwf addwf,WorkFlow workFlow){
        WFPoint beforePoint = new WFPoint();
        beforePoint.setName(WFPoint.ENDPOINT);
        beforePoint = wfPointRepository.save(beforePoint);
        WFPoint temp = beforePoint;
        WFPoint wfPoint = new WFPoint();
        Role role = new Role();
        List<String> wfpnames = addwf.getWfpnames();
        List<String> funcIds = addwf.getFuncIds();
        for (int i = 0;i<wfpnames.size();i++){
            wfPoint.setWorkFlow(workFlow);
            wfPoint.setName(wfpnames.get(i));
            wfPoint.setPrePoint(beforePoint);
            //为每个节点新增一个角色，并将该角色与某个功能绑定，这个功能需要实现添加好
            role.setName(workFlow.getName()+"_"+i); // 此处自动新增角色，角色名流程名_{节点顺序}
            role.getFunctions().add(functionRepository.findOne(funcIds.get(i)));
            role = roleRepository.save(role);
            wfPoint.setRole(role);
            beforePoint = wfPointRepository.save(wfPoint);
            if (i == 0){
                //设置初始节点
                workFlow.setBeginPoint(beforePoint);
                workFlowRepository.save(workFlow);
            }
            if (i == wfpnames.size()-1){
                temp.setPrePoint(wfPoint);
                wfPointRepository.save(temp);
            }
            wfPoint = new WFPoint();
            role = new Role();
        }
    }

    /**
     * 给流程节点增加人员
     * @param addwfps
     */
    @Transactional
    public void addwfpUsers(List<Addwfp> addwfps){
        WFPoint wfPoint;
        for (int i = 0;i<addwfps.size();i++){
            wfPoint = wfPointRepository.findOne(addwfps.get(i).getId());
            wfPoint.getRole().setUsers((getUsersFromIds(addwfps.get(i).getIds())));
            wfPointRepository.save(wfPoint);
        }
    }

    /**
     * 从用户ids获取用户对象
     * @param ids
     * @return
     */
    public Set<User> getUsersFromIds(List<String> ids){
        return new HashSet<>(findByIdIn(ids));
    }

    @Transactional
    public void updatewfp(List<UpdwfpU> ups){
        String wfpid;
        WFPoint wfp;
        List<User> users;
        for (UpdwfpU u:ups){
            wfpid = u.getWfpid();
            wfp = wfPointRepository.findOne(wfpid);
            users = userRepository.findByIdIn(u.getDelUsrIds());
            delUsr(users,wfp);
            users = userRepository.findByIdIn(u.getAddUsrIds());
            addUsr(users,wfp);
        }
    }

    /**
     * 删除某一个节点里面的审批或者发起人员
     * 如果删除了某一个人员，并且该人员已经参与了某些审批，这时候，该审批的记录并不会受到影响，已经进入审批的记录也不会受到影响，
     * 某个人员需要参与审批的时候，需要重新获取相关的信息，我们这时候可以从对应的业务里面获取所有的流程，然后，将业务
     * @param users
     * @param wfp
     */
    public void delUsr(List<User> users,WFPoint wfp){
        Role role = wfp.getRole();
        role.getUsers().removeAll(users);
        roleRepository.save(role);
    }

    /**
     * 增加某一个节点里面的审批或者发起人员
     * @param users
     * @param wfp
     */
    public void addUsr(List<User> users,WFPoint wfp){
        Role role = wfp.getRole();
        role.getUsers().addAll(users);
        roleRepository.save(role);
    }

    @Transactional
    public WFPoint save(WFPoint wfPoint){
        return wfPointRepository.save(wfPoint);
    }

    @Transactional
    public WFPoint findOne(String id){
        return wfPointRepository.findOne(id);
    }

    @Transactional
    public List<User> findByIdIn(List list){
        return userRepository.findByIdIn(list);
    }
    /**
     * 根据id删除某一个流程节点，我决定不提供这个功能，
     * 如果要实现的话，可以更改cascade的值，也可以手动解除外键
     * @param id
     */
    public void deletePoint(String id){
        WFPoint wfPoint = wfPointRepository.findOne(id);
        deletePoint(wfPoint);
    }

    /**(不完善，废弃，仅供测试使用）
     * 根据传入的流程节点删除这个节点，并将它的前后节点解除与它的关联
     * 如果删除节点之后只余下了终止节点，那么需要删除这个终止节点，
     * 并且该节点必然是某流程的beginPoint，此时需要解除beginPoint关联；
     * 否则，就判断该节点是不是某流程的beginPoint，并解除beginPoint关联
     * @param wfPoint
     */
    public void deletePoint(WFPoint wfPoint){
        WFPoint pre = wfPoint.getPrePoint();
        WFPoint net = wfPoint.getNxtPoint();
        net.setPrePoint(pre);
        net = wfPointRepository.save(net);
        wfPoint.setPrePoint(null);
        WorkFlow workFlow = wfPoint.getWorkFlow();
        WFPoint beginPoint = workFlow.getBeginPoint();
        if (net.getName().equals(WFPoint.ENDPOINT) && net.getPrePoint().getName().equals(WFPoint.ENDPOINT)){
            net.setPrePoint(null);
            net = wfPointRepository.save(net);
            wfPointRepository.delete(net);
            workFlow.setBeginPoint(null);
        } else if (beginPoint.getId().equals(wfPoint.getId())){
            workFlow.setBeginPoint(net);
        }
        wfPointRepository.delete(wfPoint);
    }
}
