package com.houxiaokang.workbench.server;

import com.houxiaokang.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryService {
    List<TranHistory> queryTranHistoryForDetailByTransactionId(String transactionId);
}