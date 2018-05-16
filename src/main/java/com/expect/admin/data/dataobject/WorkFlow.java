package com.expect.admin.data.dataobject;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * description:流程表，这里是后面需求更改后的流程设计，流程被认为是可以由用户修改的
 * 流程的删除使用软删除，将type属性改成delete_{原type}
 * Created by gaoyw on 2018/5/6.
 */
@Entity
@Table(name = "work_flow")
public class WorkFlow {
    public static final String TRANS_PERSON = "transPerson";//人员借调类标记
    public static final String DELETE = "delete_transPerson";//人员借调类删除后标记
    private String id;
    private String type;//流程的类别，是属于借调，还是出差
    private String name;//流程的名称
    private WFPoint beginPoint;//流程的开始节点
    private String description;//流程描述

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

    @Column(name = "type", length = 20)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "name", length = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToOne
    @JoinColumn(name = "begin_id")
    public WFPoint getBeginPoint() {
        return beginPoint;
    }

    public void setBeginPoint(WFPoint beginPoint) {
        this.beginPoint = beginPoint;
    }

    @Column(name = "description", length = 200)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
