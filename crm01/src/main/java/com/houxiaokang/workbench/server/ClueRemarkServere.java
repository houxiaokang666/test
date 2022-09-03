package com.houxiaokang.workbench.server;

import com.houxiaokang.workbench.domain.ClueRemark;

import java.util.List;


public interface ClueRemarkServere {
    List<ClueRemark> queryClueRemarkForDetailByClueId(String clueId);
}
