package com.houxiaokang.workbench.server;

import com.houxiaokang.workbench.domain.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> quertCustomerForAutoCompletionByname(String name);

    Customer queryCustomerForSaveByName(String name);

}
