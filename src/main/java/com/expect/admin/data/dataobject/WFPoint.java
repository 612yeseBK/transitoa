package com.expect.admin.data.dataobject;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * description:workflowpoint,属于可增加和删除的流程节点，仅用于需求变更后的情况，让用户可以修改流程
 * Created by gaoyw on 2018/5/6.
 */
@Entity
@Table(name = "wf_point")
public class WFPoint {
    private String id;
    private String name;//流程节点名称
    private WorkFlow workFlow;//该节点对应的流程
    private WFPoint prePoint;//该节点的前一个节点,需要为开始节点在数据库里加入一个终结点
    private WFPoint nxtPoint;//该节点的后一个节点，需要为结束节点在数据库里加入一个终结点，这两个节点可以变成一个
    private Set<User> users = new HashSet<>();//该节点的审批人员

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

    @Column(name = "name", length = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    @JoinColumn(name = "wf_id")
    public WorkFlow getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(WorkFlow workFlow) {
        this.workFlow = workFlow;
    }

    @OneToOne
    @JoinColumn(name = "prp_id")
    public WFPoint getPrePoint() {
        return prePoint;
    }

    public void setPrePoint(WFPoint prePoint) {
        this.prePoint = prePoint;
    }

    @OneToOne(mappedBy = "prePoint")
    public WFPoint getNxtPoint() {
        return nxtPoint;
    }

    public void setNxtPoint(WFPoint nxtPoint) {
        this.nxtPoint = nxtPoint;
    }

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(name = "wfpoint_user", joinColumns = @JoinColumn(name = "wfp_id"),
            inverseJoinColumns = @JoinColumn(name = "usr_id"))
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
