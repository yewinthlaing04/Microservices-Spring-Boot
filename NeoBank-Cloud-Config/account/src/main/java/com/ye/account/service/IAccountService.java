package com.ye.account.service;

import com.ye.account.dto.CustomerDto;

public interface IAccountService {

    void createAccount( CustomerDto customerDto);

    CustomerDto fetchCustomer(String mobileNumber);

    boolean updateAccount(CustomerDto customerDto);

    boolean deleteAccount(String mobileNumber);
}
