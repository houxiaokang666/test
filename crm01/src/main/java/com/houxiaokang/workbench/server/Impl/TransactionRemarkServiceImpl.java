package com.houxiaokang.workbench.server.Impl;

import com.houxiaokang.workbench.domain.TransactionRemark;
import com.houxiaokang.workbench.mapper.TransactionRemarkMapper;
import com.houxiaokang.workbench.server.TransactionRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionRemarkServiceImpl implements TransactionRemarkService {
    @Autowired
    private TransactionRemarkMapper transactionRemarkMapper;

    @Override
    public List<TransactionRemark> queeryTransactionRemarkForDetailByTransactionId(String transactionId) {
        return transactionRemarkMapper.selectTransactionRemarkForDetailByTransactionId(transactionId);
    }
}
