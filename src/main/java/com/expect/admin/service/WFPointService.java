package com.expect.admin.service;

import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dao.WFPointRepository;
import com.expect.admin.data.dao.WorkFlowRepository;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.data.dataobject.WFPoint;
import com.expect.admin.data.dataobject.WorkFlow;
import com.expect.admin.data.pojo.Addwf;
import com.expect.admin.data.pojo.Addwfp;
import com.expect.admin.web.WFPointController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * 为流程增加它的节点，暂时不增加人员
     * @param addwf
     */
    public void addPoints(Addwf addwf,WorkFlow workFlow){
        WFPoint beforePoint = wfPointRepository.findOne("000000");//这被手动添加在数据库里面做一个终止节点
        WFPoint wfPoint = new WFPoint();
        List<String> wfpnames = addwf.getWfpnames();
        for (int i = 0;i<wfpnames.size();i++){
            wfPoint.setWorkFlow(workFlow);
            wfPoint.setName(wfpnames.get(i));
            wfPoint.setPrePoint(beforePoint);
            beforePoint = wfPointRepository.save(wfPoint);
            System.out.println(beforePoint==wfPoint);
            if (i==1){
                workFlow.setBeginPoint(beforePoint);
                workFlowRepository.save(workFlow);
            }
            wfPoint = new WFPoint();
        }
//        WFPoint afterPoint = beforePoint;
//        WFPoint be;
//        while(!beforePoint.getPrePoint().getId().equals("000000")){
//            be = beforePoint.getPrePoint();
//            be.setNxtPoint(beforePoint);
//            beforePoint = wfPointRepository.save(be);
//        }

    }

    /**
     * 给流程节点增加人员
     * @param addwfps
     */
    public void addwfpUsers(List<Addwfp> addwfps){
        WFPoint wfPoint;
        for (int i = 0;i<addwfps.size();i++){
            wfPoint = wfPointRepository.findOne(addwfps.get(i).getId());
            wfPoint.setUsers(getUsersFromIds(addwfps.get(i).getIds()));
            wfPointRepository.save(wfPoint);
        }
    }

    /**
     * 从用户ids获取用户对象
     * @param ids
     * @return
     */
    public Set<User> getUsersFromIds(List<String> ids){
        Set<User> users = new HashSet<>();
        for (String id : ids) {
            users.add(userRepository.findOne(id));
        }
        return users;
    }


}
