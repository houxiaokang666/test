package com.houxiaokang.workbench.server.Impl;

import com.houxiaokang.workbench.domain.Customer;
import com.houxiaokang.workbench.mapper.CustomerMapper;
import com.houxiaokang.workbench.server.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public List<Customer> quertCustomerForAutoCompletionByname(String name) {
        return customerMapper.selectCustomerForAutoCompletionByname(name);
    }

    @Override
    public Customer queryCustomerForSaveByName(String name) {
        return customerMapper.selectCustomerForSaveByName(name);
    }
}
