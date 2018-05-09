package com.expect.admin.service.vo;

import com.expect.admin.data.dataobject.Department;
import com.expect.admin.data.dataobject.Lcrzb;
import com.expect.admin.data.dataobject.TransferPersonnel;
import com.expect.admin.data.dataobject.User;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * description:人员调用表的值对象
 * Created by gaoyw on 2018/5/6.
 */
public class TransferPersonnelVo {
    private String id;
    private User applicant;//借调申请人
    private Date applyTime;//申请发起时间
    private Date beginTime;//开始的时间
    private Date endTime;//结束的时间（开始到结束之间就是借调时间）
    private String reason;//借调事由
    private int number;//借调人数
    private DepartmentVo toDepartmentVo;//派遣前往的单位
    private String job;//派遣的工种

    public TransferPersonnelVo(){}

    /**
     * 将do转成vo
     * @param transferPersonnel
     * @return
     */
    public static TransferPersonnelVo convert(TransferPersonnel transferPersonnel){
        TransferPersonnelVo transferPersonnelVo = new TransferPersonnelVo();
        BeanUtils.copyProperties(transferPersonnel,transferPersonnelVo);
        return transferPersonnelVo;
    }

    /**
     * 将Vo转成do
     * @param transferPersonnelVo
     * @return
     */
    public static TransferPersonnel convert(TransferPersonnelVo transferPersonnelVo){
        TransferPersonnel transferPersonnel = new TransferPersonnel();
        BeanUtils.copyProperties(transferPersonnelVo,transferPersonnel);
        return transferPersonnel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getApplicant() {
        return applicant;
    }

    public void setApplicant(User applicant) {
        this.applicant = applicant;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public DepartmentVo getToDepartmentVo() {
        return toDepartmentVo;
    }

    public void setToDepartment(DepartmentVo toDepartmentVo) {
        this.toDepartmentVo = toDepartmentVo;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

}
