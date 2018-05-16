package com.expect.admin.service;

import com.expect.admin.data.dao.TransPerRecordRepository;
import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.TransPerRecord;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.data.dataobject.WFPoint;
import com.expect.admin.service.convertor.UserConvertor;
import com.expect.admin.utils.DateUtil;
import com.expect.admin.web.WFPointController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * description:
 * Created by gaoyw on 2018/5/6.
 */
@Service
public class TransPerRecordService {
    private final Logger log = LoggerFactory.getLogger(TransPerRecordService.class);
    @Autowired
    UserService userService;
    @Autowired
    TransPerRecordRepository tprRepository;
    @Autowired
    UserRepository userRepository;

    /**
     * 增加一个审批记录
     * @param message
     * @param cljg
     * @param wfPoint
     * @return
     */
    public TransPerRecord addBeginOne(String message,String cljg, WFPoint wfPoint){
        TransPerRecord transPerRecord = new TransPerRecord();
        User loginUsr = userRepository.findOne(userService.getLoginUser().getId());
        transPerRecord.setUser(loginUsr);
        transPerRecord.setClsj(DateUtil.format(new Date(),DateUtil.fullFormat));
        transPerRecord.setCljg(cljg);
        transPerRecord.setWfPoint(wfPoint);
        transPerRecord.setMessage(message);
        transPerRecord = tprRepository.save(transPerRecord);
        System.out.println(transPerRecord.getId());
        return transPerRecord;
    }

}
