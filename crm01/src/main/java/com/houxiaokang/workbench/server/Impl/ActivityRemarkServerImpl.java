package com.houxiaokang.workbench.server.Impl;

import com.houxiaokang.workbench.domain.Remark;
import com.houxiaokang.workbench.mapper.RemarkMapper;
import com.houxiaokang.workbench.server.RemarkServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("remarkServerImpl")
public class ActivityRemarkServerImpl implements RemarkServer {
    @Autowired
    private RemarkMapper remarkMapper;

    @Override
    public List<Remark> queryRemarkByActivityId(String id) {
        return remarkMapper.selectRemarkByActivityId(id);
    }

    @Override
    public int saveRemarkByUser(Remark remark) {
        return remarkMapper.insertByUser(remark);
    }

    @Override
    public int deleteRemarkById(String id) {
        return remarkMapper.deleteById(id);
    }

    @Override
    public int saveEditeRemarkById(Remark remark) {
        return remarkMapper.updateByRemarkId(remark);
    }
}
