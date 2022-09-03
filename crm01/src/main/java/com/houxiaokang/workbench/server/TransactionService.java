package com.houxiaokang.workbench.server;

import com.houxiaokang.settings.domain.User;
import com.houxiaokang.workbench.domain.Transaction;

import java.util.Map;

public interface TransactionService {
    void saveCreatedTransaction(Map<String, Object> maps, User user);

    Transaction queryTransactionForDetailById(String id);
}
