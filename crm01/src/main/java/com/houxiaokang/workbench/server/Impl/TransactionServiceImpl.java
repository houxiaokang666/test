package com.houxiaokang.workbench.server.Impl;

import com.houxiaokang.common.Util.DatesimplDateFormat;
import com.houxiaokang.common.Util.UUIDUtil;
import com.houxiaokang.settings.domain.User;
import com.houxiaokang.workbench.domain.Customer;
import com.houxiaokang.workbench.domain.TranHistory;
import com.houxiaokang.workbench.domain.Transaction;
import com.houxiaokang.workbench.mapper.CustomerMapper;
import com.houxiaokang.workbench.mapper.TranHistoryMapper;
import com.houxiaokang.workbench.mapper.TransactionMapper;
import com.houxiaokang.workbench.server.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TranHistoryMapper tranHistoryMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private TransactionMapper transactionMapper;

    public void saveCreatedTransaction(Map<String, Object> maps, User user) {
        Transaction transaction = new Transaction();
        transaction.setId(UUIDUtil.getUUID());
        transaction.setCreateTime(DatesimplDateFormat.DatesimplDateFormatoString(new Date()));
        transaction.setOwner((String) maps.get("owner"));
        transaction.setCreateBy(user.getId());
        transaction.setMoney((String) maps.get("money"));
        transaction.setName((String) maps.get("name"));
        transaction.setExpectedDate((String) maps.get("expectedDate"));
        Customer customer = customerMapper.selectCustomerForSaveByName((String) maps.get("customerId"));
        if (customer == null) {
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setCreateTime(DatesimplDateFormat.DatesimplDateFormatoString(new Date()));
            customer.setName((String) maps.get("customerId"));
            customer.setOwner(user.getId());
            customer.setCreateBy(user.getId());
            customerMapper.insertCustomerForConvertByClue(customer);
        }
        transaction.setCustomerId(customer.getId());
        transaction.setStage((String) maps.get("stage"));
        transaction.setActivityId((String) maps.get("activityId"));
        transaction.setContactsId((String) maps.get("contactsId"));
        transaction.setDescription((String) maps.get("description"));
        transaction.setContactSummary((String) maps.get("contactSummary"));
        transaction.setNextContactTime((String) maps.get("nextContactTime"));
        transaction.setType((String) maps.get("type"));
        transaction.setSource((String) maps.get("source"));
        transactionMapper.insert(transaction);
        TranHistory tranHistory = new TranHistory();
        tranHistory.setCreateBy(user.getId());
        tranHistory.setCreateTime(DatesimplDateFormat.DatesimplDateFormatoString(new Date()));
        tranHistory.setTranId(transaction.getId());
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setStage(transaction.getStage());
        tranHistory.setMoney(transaction.getMoney());
        tranHistory.setExpectedDate(transaction.getExpectedDate());
        tranHistoryMapper.insert(tranHistory);
    }

    @Override
    public Transaction queryTransactionForDetailById(String id) {
        return transactionMapper.selectTransactionForDetailById(id);
    }
}
