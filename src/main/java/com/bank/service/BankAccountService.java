package com.bank.service;

import com.bank.dtos.*;
import com.bank.exceptions.BalanceNotSufficientException;
import com.bank.exceptions.BankAccountNotFoundException;
import com.bank.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

    CustomerDto saveCustomer(CustomerDto customerDto);
    CurrentBankDto saveCurrentBankDto (Float balance, Float overDraft, Long id) throws CustomerNotFoundException;
    SavingAccountDto saveCurrentBank(Float balance, Float overDraft , Long id) throws CustomerNotFoundException;
    List<CustomerDto> findAllCustomers();
    BankAccountDto getBankAccount (String accountId) throws BankAccountNotFoundException;
    void debit (String accountId, Float amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit (String accountId, Float amount, String description) throws BankAccountNotFoundException;
    void transfer (String accountIdSource, String accountIdDestination, Float amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccountDto> findAllBanks();

    CustomerDto getOneCustoner (Long id) throws CustomerNotFoundException;
    CustomerDto updateCustomer (CustomerDto customerDto);
    void deleteCustomer (Long id);

    List<OperationDto> findAllOperations();

    AccountHistoryDto getAccountHistory (String accountId, Integer page, Integer size) throws BankAccountNotFoundException;

    List<CustomerDto> findCustomers (String keyword);

}
