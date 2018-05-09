package com.expect.admin.data.dataobject;

import jdk.nashorn.internal.scripts.JO;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * description:人员借调表，用于公司申请人员借调使用
 * Created by gaoyw on 2018/5/5.
 */
@Entity
@Table(name = "c_transfer_personnel")
public class TransferPersonnel {
    private String id;
    private User applicant;//借调申请人
    private String applyTime;//申请发起时间
    private String beginTime;//开始的时间
    private String endTime;//结束的时间（开始到结束之间就是借调时间）
    private String reason;//借调事由
    private int number;//借调人数
    private Department toDepartment;//派遣前往的单位
    private String job;//派遣的工种
    private WorkFlow workFlow;//该申请属于对应哪一个流程
    private Set<TransPerRecord> transPerRecords = new HashSet<>();//审批流程日志



    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "id", nullable = false, unique = true, length = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "applicant_id")
    //需要做多对一双向关联，在该表生成applicant_id外键，还需在user表生成一个对象
    public User getApplicant() {
        return applicant;
    }

    public void setApplicant(User applicant) {
        this.applicant = applicant;
    }

    @Column(name = "apply_time")
    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    @Column(name = "begin_time")
    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    @Column(name = "end_time")
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Column(name = "reason")
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Column(name = "number")
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @OneToOne
    @JoinColumn(name = "department_id")//做一对一单向关联，在该表生成department_id外键
    public Department getToDepartment() {
        return toDepartment;
    }

    public void setToDepartment(Department toDepartment) {
        this.toDepartment = toDepartment;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @ManyToOne
    @JoinColumn(name = "wf_id")
    public WorkFlow getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(WorkFlow workFlow) {
        this.workFlow = workFlow;
    }

    @OneToMany(mappedBy = "transferPersonnel", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    public Set<TransPerRecord> getTransPerRecords() {
        return transPerRecords;
    }

    public void setTransPerRecords(Set<TransPerRecord> transPerRecords) {
        this.transPerRecords = transPerRecords;
    }
}
