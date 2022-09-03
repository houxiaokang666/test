package com.houxiaokang.workbench.server.Impl;

import com.houxiaokang.workbench.domain.ClueRemark;
import com.houxiaokang.workbench.mapper.ClueRemarkMapper;
import com.houxiaokang.workbench.server.ClueRemarkServere;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClueRemarkServerImpl implements ClueRemarkServere {
    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    @Override
    public List<ClueRemark> queryClueRemarkForDetailByClueId(String clueId) {
        return clueRemarkMapper.selectClueRemarkForDetailByClueId(clueId);
    }
}
