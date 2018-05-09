package com.expect.admin.service.vo;

import com.expect.admin.data.dataobject.TransPerRecord;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * description:
 * Created by gaoyw on 2018/5/6.
 */
public class TransPerRecordVo {
    private String id;
    private UserVo userVo;//处理人
    private Date clsj;//处理时间
    private String message;//处理意见
    private String cljg;//处理结果
    private String clnrid;//处理内容的id（所处理的东西的id）
    private String dyjd;//对应的流程节点

    public TransPerRecordVo(){}

    /**
     * 将do转成vo
     * @param transPerRecord
     * @return
     */
    public static TransPerRecordVo convert(TransPerRecord transPerRecord){
        TransPerRecordVo transPerRecordVo = new TransPerRecordVo();
        BeanUtils.copyProperties(transPerRecord,transPerRecordVo);
        return transPerRecordVo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserVo getUserVo() {
        return userVo;
    }

    public void setUserVo(UserVo userVo) {
        this.userVo = userVo;
    }

    public Date getClsj() {
        return clsj;
    }

    public void setClsj(Date clsj) {
        this.clsj = clsj;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCljg() {
        return cljg;
    }

    public void setCljg(String cljg) {
        this.cljg = cljg;
    }

    public String getClnrid() {
        return clnrid;
    }

    public void setClnrid(String clnrid) {
        this.clnrid = clnrid;
    }

    public String getDyjd() {
        return dyjd;
    }
    public void setDyjd(String dyjd) {
        this.dyjd = dyjd;
    }
}
