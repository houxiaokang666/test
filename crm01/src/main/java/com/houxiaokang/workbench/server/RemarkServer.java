package com.houxiaokang.workbench.server;

import com.houxiaokang.workbench.domain.Remark;

import java.util.List;

public interface RemarkServer {
    List<Remark> queryRemarkByActivityId(String id);

    int saveRemarkByUser(Remark remark);

    int deleteRemarkById(String di);

    int saveEditeRemarkById(Remark remark);
}
