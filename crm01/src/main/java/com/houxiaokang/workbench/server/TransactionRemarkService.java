package com.houxiaokang.workbench.server;

import com.houxiaokang.workbench.domain.TransactionRemark;

import java.util.List;

public interface TransactionRemarkService {
    List<TransactionRemark> queeryTransactionRemarkForDetailByTransactionId(String transactionId);

}
