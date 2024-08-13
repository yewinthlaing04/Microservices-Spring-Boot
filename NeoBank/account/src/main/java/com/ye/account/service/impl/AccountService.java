package com.ye.account.service.impl;

import com.ye.account.constants.AccountConstants;
import com.ye.account.dto.AccountsDto;
import com.ye.account.dto.CustomerDto;
import com.ye.account.entity.Accounts;
import com.ye.account.entity.Customer;
import com.ye.account.exception.CustomerAlreadyExistsException;
import com.ye.account.exception.ResourceNotFoundException;
import com.ye.account.mapper.AccountsMapper;
import com.ye.account.mapper.CustomerMapper;
import com.ye.account.repository.AccountsRepository;
import com.ye.account.repository.CustomerRepository;
import com.ye.account.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final CustomerRepository customerRepo;
    private final AccountsRepository accountRepo;

    @Override
    public void createAccount(CustomerDto customerDto) {

        // create customer
        Customer customer = CustomerMapper.mapToCustomer(customerDto , new Customer());

        Optional<Customer> optionalCustomer = customerRepo.findByMobileNumber(customer.getMobileNumber());

        if ( optionalCustomer.isPresent() ) {
            throw new CustomerAlreadyExistsException("Customer already exist with given mobile number: "
                    + customerDto.getMobileNumber());
        }
//        with the help of auditawareImpl , we can remove these setters
//        customer.setCreatedAt(LocalDateTime.now());
//        customer.setCreatedBy("ByUser");

        Customer savedCustomer =  customerRepo.save(customer);

        // set account
        var account = createNewAccount(savedCustomer);
        // save account with customer id
        accountRepo.save(account);

    }

    private Accounts createNewAccount(Customer customer){
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 10000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountConstants.SAVINGS);
        newAccount.setBranchAddress(AccountConstants.ADDRESS);
//        newAccount.setCreatedAt(LocalDateTime.now());
//        newAccount.setCreatedBy("ByUser");

        return newAccount;
    }

    @Override
    public CustomerDto fetchCustomer(String mobileNumber) {

        Customer fetchCustomer = customerRepo.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException( "Customer" , "MobileNumber" , mobileNumber)
        );

        Accounts fetchAccount = accountRepo.findByCustomerId(fetchCustomer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account" , "customerId" , fetchCustomer.getCustomerId().toString())
        );

        // set customer to customer dto to transfer data
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto( fetchCustomer , new CustomerDto() );
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(fetchAccount , new AccountsDto()));

        return customerDto;
    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {

        boolean isUpdated=false;
        // get accountdto from customerdto
        AccountsDto accountsDto = customerDto.getAccountsDto();

        // if accountdto is not null
        if ( accountsDto != null ) {
            // find account by repo
            Accounts accounts = accountRepo.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber" , accountsDto.getAccountNumber().toString())
            );

            // map to account
            AccountsMapper.mapToAccounts(accountsDto , accounts);
            // save account
            accounts = accountRepo.save(accounts);
            // find customer id
            Long customerId = accounts.getCustomerId();
            // find customer with customer id
            Customer customer = customerRepo.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerId" , customerId.toString())
            );
            //map to customer
            CustomerMapper.mapToCustomer(customerDto, customer);
            // save customer
            customerRepo.save(customer);
            // set true and return
            isUpdated=true;
        }
        return isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        // customer with mobile number
        Customer customer = customerRepo.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "CustomerId" , mobileNumber)
        );
        // get customerId
        Long customerId = customer.getCustomerId();
        // delete with customer id for account
        accountRepo.deleteByCustomerId(customerId);

        customerRepo.deleteById(customerId);

        return true;
    }

}
