package com.expect.admin.data.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description:
 * Created by gaoyw on 2018/5/13.
 */
public class WfpInfo {
    String id;
    String name;
    List<Map> usersInfos;//Map里面有两个字段，分别为id，name，用以存储该节点的用户信息

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List getUsersInfos() {
        return usersInfos;
    }

    public void setUsersInfos(List usersInfos) {
        this.usersInfos = usersInfos;
    }
}

