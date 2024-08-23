package com.ye.account.service;

import com.ye.account.dto.CustomerDetailsDto;

public interface ICustomerService {

    CustomerDetailsDto fetchCustomerDetails(String mobileNumber , String correlationId);
}
