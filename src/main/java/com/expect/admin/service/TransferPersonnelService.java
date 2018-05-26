package com.expect.admin.service;

import com.expect.admin.data.dao.TransPerRecordRepository;
import com.expect.admin.data.dao.TransferPersonnelRepository;
import com.expect.admin.data.dataobject.TransPerRecord;
import com.expect.admin.data.dataobject.TransferPersonnel;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.data.dataobject.WFPoint;
import com.expect.admin.service.vo.TransPerRecordVo;
import com.expect.admin.service.vo.TransferPersonnelVo;
import com.expect.admin.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public void add(TransferPersonnelVo transferPersonnelVo,String wkfid,String state){
        TransferPersonnel transferPersonnel = new TransferPersonnel();
        BeanUtils.copyProperties(transferPersonnelVo,transferPersonnel);
        transferPersonnel.setApplyTime(DateUtil.format(new Date(),DateUtil.fullFormat));
        WFPoint beginPoint = wfService.getBeginPoint(wkfid);
        transferPersonnel.setNxtPoint(beginPoint.getNxtPoint());
        transferPersonnel.setState(state);
        transferPersonnel = tpRepository.save(transferPersonnel);
        TransPerRecord tpr = tprService.addOne("保存",state,beginPoint, transferPersonnel, userService.getLogUsr());
    }

    /**
     * 提交已经保存的
     * @param tpVo
     * @param tpid
     */
    public void commitSaved(TransferPersonnelVo tpVo,String tpid){
        TransferPersonnel tp = tpRepository.findOne(tpid);
        tp.setApplyTime(tpVo.getApplyTime());
        tp.setBeginTime(tpVo.getBeginTime());
        tp.setEndTime(tpVo.getEndTime());
        tp.setNumber(tpVo.getNumber());
        tp.setJob(tpVo.getJob());
        tp.setStaffNum(tpVo.getStaffNum());
        tp.setReason(tpVo.getReason());
        tp.setToDepartment(tpVo.getToDepartment());
        tp.setState(TransferPersonnel.WAITING);
        tp = tpRepository.save(tp);
        tprService.addOne("提交",TransferPersonnel.WAITING,tp.getNxtPoint().getPrePoint(), tp, userService.getLogUsr());

    }

    /**
     * 根据用户以及对表单状态的要求，获取各种状态的表单
     * @param user
     * @param states
     * @return
     */
    public List<TransferPersonnelVo> getTransPerByState(User user,String[] states){
        List<TransferPersonnel> allLists = new ArrayList(user.getTransferPersonnels());
        List<TransferPersonnel> stateLists = new ArrayList<>();
        int length = states.length;
        int i;
        for (TransferPersonnel tfp : allLists){
            for (i = 0;i < length;i ++) {
                if (tfp.getState().equals(states[i])) {
                    stateLists.add(tfp);
                }
            }
        }
        return getTpVoFromList(stateLists);
    }


    /**
     * 根据登录用户，获取该用户所有需要审批的表单
     * @param user
     * @return
     */
    public List<TransferPersonnelVo> getCheckForms(User user){
        List<TransferPersonnel> tfps = tpRepository.findAll();
        List<TransferPersonnel> tfpsNeed = new ArrayList<>();
        Set<User> users;
        for (TransferPersonnel tfp : tfps){
            users = tfp.getNxtPoint().getRole().getUsers();
            for (User u : users){
                if (u.equals(user)){
                    tfpsNeed.add(tfp);
                }
            }
        }
        return getTpVoFromList(tfpsNeed);
    }

    /**
     * 根据登录用户，获取该用户所有的审批记录
     * @param usrid
     * @return
     */
    public List<TransferPersonnelVo> getCheckedForms(String usrid){
        List<TransPerRecord> tprs = tprRepository.findByUserId(usrid);
        List<TransferPersonnel> tps = new ArrayList<>();
        for (TransPerRecord tpr : tprs){
            Set<TransPerRecord> transPerRecords = tpr.getTransferPersonnel().getTransPerRecords();
            //根据记录的信息判断这个记录是提交保存记录，还是审批记录
            if (!(tpr.getMessage().equals("提交")||tpr.getMessage().equals("保存"))) {
                tps.add(tpr.getTransferPersonnel());
            }
        }
        return getTpVoFromList(tps);
    }


    /**
     * 获取某个用户对某个申请的审批详情
     * @param usrid
     * @param tpid
     * @return
     */
    public TransPerRecordVo getRcdBytpAndUsr(String usrid, String tpid){
        TransPerRecord tpr = tprRepository.findByUserAndTransferPersonnel(usrid,tpid);
        return TransPerRecordVo.convert(tpr);
    }

    /**
     * 根据申请表单的id获得该表单当前所有的审核记录
     * @param id
     * @return
     */
    public List<TransPerRecordVo> getRecdsByTransId(String id){
        TransferPersonnel tp = tpRepository.findOne(id);
        List<TransPerRecord> tprs = new ArrayList<>(tp.getTransPerRecords());
        Collections.sort(tprs, new Comparator<TransPerRecord>() {
            @Override
            public int compare(TransPerRecord o1, TransPerRecord o2) {
                return o1.getClsj().compareTo(o2.getClsj());
            }
        });
        return getTprVoFromList(tprs);
    }

    /**
     * 某个审批人审核的结果，存入表格
     * @param id
     * @param message
     * @param cljg
     */
    public void checkApply(String id,String message,String cljg){
        TransferPersonnel tp = tpRepository.findOne(id);
        WFPoint wfp = tp.getNxtPoint(); //当前流程
        tprService.addOne(message,cljg,wfp,tp,userService.getLogUsr());
        if (cljg.equals("rejected")){
            tp.setState(TransferPersonnel.REJECTED);
        } else if (cljg.equals("passed")){
            tp.setNxtPoint(wfp.getNxtPoint());
        }
        if (tp.getNxtPoint().getName().equals(WFPoint.ENDPOINT)){
            tp.setState(TransferPersonnel.PASSED);
        }
    }


    /**
     * 将TransferPersonnel对象list转变成TransferPersonnelVo对象list
     * @param lists
     * @return
     */
    public List<TransferPersonnelVo> getTpVoFromList(List<TransferPersonnel> lists){
        List<TransferPersonnelVo> tfpVos = new ArrayList<>();
        for (TransferPersonnel tp : lists){
            tfpVos.add(TransferPersonnelVo.convert(tp));
        }
        return tfpVos;
    }

    /**
     * 将TransPerRecord对象list转变成TransPerRecordVo对象list
     * @param tprs
     * @return
     */
    public List<TransPerRecordVo> getTprVoFromList(List<TransPerRecord> tprs){
        List<TransPerRecordVo> tprVos = new ArrayList<>();
        for (TransPerRecord tpr : tprs){
            tprVos.add(TransPerRecordVo.convert(tpr));
        }
        return tprVos;
    }
}
