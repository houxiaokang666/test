package com.houxiaokang.workbench.server.Impl;

import com.houxiaokang.workbench.domain.TranHistory;
import com.houxiaokang.workbench.mapper.TranHistoryMapper;
import com.houxiaokang.workbench.server.TranHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TranHistoryServiceImpl implements TranHistoryService {
    @Autowired
    private TranHistoryMapper tranHistoryMapper;

    @Override
    public List<TranHistory> queryTranHistoryForDetailByTransactionId(String transactionId) {
        return tranHistoryMapper.selectTranHistoryForDetailByTransactionId(transactionId);
    }
}