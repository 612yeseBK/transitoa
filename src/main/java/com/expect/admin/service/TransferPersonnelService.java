package com.expect.admin.service;

import com.expect.admin.data.dao.TransPerRecordRepository;
import com.expect.admin.data.dao.TransferPersonnelRepository;
import com.expect.admin.data.dataobject.TransPerRecord;
import com.expect.admin.data.dataobject.TransferPersonnel;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.data.dataobject.WFPoint;
import com.expect.admin.service.vo.TransferPersonnelVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * description:人员借用业务处理
 * Created by gaoyw on 2018/5/6.
 */
@Service
public class TransferPersonnelService {
    private final Logger log = LoggerFactory.getLogger(TransferPersonnelService.class);
    @Autowired
    TransferPersonnelRepository tpRepository;
    @Autowired
    TransPerRecordRepository tprRepository;
    @Autowired
    TransPerRecordService tprService;
    @Autowired
    UserService userService;
    @Autowired
    WorkFlowService wfService;



    public void getAllowsForUser(User user){

    }

    /**
     * 根据id值获取人员借用表的对象
     * @param id
     * @return
     */
    @Transactional
    public TransferPersonnel findById(String id){
        return tpRepository.findById(id);
    }

    /**
     * 新增一个人员借调申请
     * @param transferPersonnelVo 填写的表单信息
     * @param wkfid 流程id
     */
    public void add(TransferPersonnelVo transferPersonnelVo,String wkfid){
        TransferPersonnel transferPersonnel = new TransferPersonnel();
        BeanUtils.copyProperties(transferPersonnelVo,transferPersonnel);
        transferPersonnel = tpRepository.save(transferPersonnel);
        WFPoint beginPoint = wfService.getBeginPoint(wkfid);
        TransPerRecord tpr = tprService.addBeginOne("新增","新增",beginPoint);
        System.out.println(tpr.getTransferPersonnel());
        //存完做个绑定,因为外键是在多的那一方，所以存多就行了
        tpr.setTransferPersonnel(transferPersonnel);
        tpr = tprRepository.save(tpr);
        System.out.println(tpr.getTransferPersonnel());
    }

}
