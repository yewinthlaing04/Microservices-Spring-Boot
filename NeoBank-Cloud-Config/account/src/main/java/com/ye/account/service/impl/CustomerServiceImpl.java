package com.ye.account.service.impl;

import com.ye.account.dto.AccountsDto;
import com.ye.account.dto.CardDto;
import com.ye.account.dto.CustomerDetailsDto;
import com.ye.account.dto.LoansDto;
import com.ye.account.entity.Accounts;
import com.ye.account.entity.Customer;
import com.ye.account.exception.ResourceNotFoundException;
import com.ye.account.mapper.AccountsMapper;
import com.ye.account.mapper.CustomerMapper;
import com.ye.account.repository.AccountsRepository;
import com.ye.account.repository.CustomerRepository;
import com.ye.account.service.ICustomerService;
import com.ye.account.service.client.CardsFeignClient;
import com.ye.account.service.client.LoanFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private  CustomerRepository customerRepository;
    private AccountsRepository accountsRepository ;
    private CardsFeignClient cardsFeignClient;
    private LoanFeignClient loanFeignClient;


    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {

        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer" , "mobileNumber", mobileNumber)
        );

        Accounts account = accountsRepository.findByCustomerId( customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account" , "customerId" ,customer.getCustomerId().toString()));


        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer , new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(account , new AccountsDto()));

        ResponseEntity<CardDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber);
        customerDetailsDto.setCardDto(cardsDtoResponseEntity.getBody());

        ResponseEntity<LoansDto> loansDtoResponseEntity = loanFeignClient.fetchLoanDetails(mobileNumber);
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());


        return customerDetailsDto;
    }
}
