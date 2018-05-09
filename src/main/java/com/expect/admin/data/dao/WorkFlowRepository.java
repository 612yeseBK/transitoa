package com.expect.admin.data.dao;

import com.expect.admin.data.dataobject.WorkFlow;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * description:
 * Created by gaoyw on 2018/5/6.
 */
public interface WorkFlowRepository extends JpaRepository<WorkFlow, String> {
}
