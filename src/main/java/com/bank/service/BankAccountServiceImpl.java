package com.bank.service;

import com.bank.dtos.*;
import com.bank.entities.*;
import com.bank.enums.OperationType;
import com.bank.exceptions.BalanceNotSufficientException;
import com.bank.exceptions.BankAccountNotFoundException;
import com.bank.exceptions.CustomerNotFoundException;
import com.bank.mappers.BankAccountImpl;
import com.bank.repositories.BankAccountRepository;
import com.bank.repositories.CustomerRepository;
import com.bank.repositories.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BankAccountServiceImpl implements BankAccountService{

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private BankAccountImpl mapper;


    @Override
    public CustomerDto saveCustomer(CustomerDto customerDto) {

        Customer customer = mapper.fromCustomerDto(customerDto);
        Customer saved = customerRepository.save(customer);
        return mapper.fromCustomer(saved);
    }

    @Override
    public CurrentBankDto saveCurrentBankDto(Float balance, Float overDraft, Long customerId) throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(customerId).orElse(null);

        if(customer==null){
            throw new CustomerNotFoundException("Customer not found");
        }

        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreateAt(new Date());
        currentAccount.setBalance(balance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);

        CurrentAccount saved = bankAccountRepository.save(currentAccount);

        return mapper.fromCurrentBankAccount(saved);
    }

    @Override
    public SavingAccountDto saveCurrentBank(Float balance, Float interestRate, Long customerId) throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(customerId).orElse(null);

        if(customer==null){
            throw  new CustomerNotFoundException("Customer not found");
        }

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setBalance(balance);
        savingAccount.setCreateAt(new Date());
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);

        SavingAccount saved = bankAccountRepository.save(savingAccount);

        return mapper.fromSavingAccount(saved);
    }

    @Override
    public List<CustomerDto> findAllCustomers() {

        List<Customer> customers = customerRepository.findAll();

        return customers.stream()
                .map(customer -> mapper.fromCustomer(customer)).collect(Collectors.toList());
    }

    @Override
    public BankAccountDto getBankAccount(String accountId) throws BankAccountNotFoundException {

        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()-> new BankAccountNotFoundException("Bank Account not found"));

        if(bankAccount instanceof SavingAccount){

            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return mapper.fromSavingAccount(savingAccount);
        }
        else{

            CurrentAccount currentAccount = (CurrentAccount)bankAccount;
            return mapper.fromCurrentBankAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accountId, Float amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {

        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));

        if(bankAccount.getBalance()<amount){

            throw new BalanceNotSufficientException("Balance not sufficient");
        }

        Operation operation = new Operation();

        operation.setOperationType(OperationType.DEBIT);
        operation.setAmount(amount);
        operation.setDescription(description);
        operation.setOperationDate(new Date());
        operation.setBankAccount(bankAccount);

        operationRepository.save(operation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);


    }

    @Override
    public void credit(String accountId, Float amount, String description) throws BankAccountNotFoundException {

        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()-> new BankAccountNotFoundException("BankAccount not found"));

        Operation operation = new Operation();

        operation.setOperationType(OperationType.CREDIT);
        operation.setAmount(amount);
        operation.setDescription(description);
        operation.setOperationDate(new Date());
        operation.setBankAccount(bankAccount);

        operationRepository.save(operation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, Float amount) throws BankAccountNotFoundException, BalanceNotSufficientException {

        debit(accountIdSource,amount,"Transfer to " +accountIdDestination);
        credit(accountIdSource,amount,"Transfer from " +accountIdSource);
    }

    @Override
    public List<BankAccountDto> findAllBanks() {

        List<BankAccount> bankAccounts = bankAccountRepository.findAll();

        return bankAccounts.stream()
                .map(bankAccount -> {

                    if(bankAccount instanceof SavingAccount){

                        SavingAccount savingAccount = (SavingAccount) bankAccount;
                        return mapper.fromSavingAccount(savingAccount);
                    }
                    else{
                        CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                        return mapper.fromCurrentBankAccount(currentAccount);
                    }
                }).collect(Collectors.toList());
    }

    @Override
    public CustomerDto getOneCustomer(Long id) throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(id).orElseThrow(()-> new CustomerNotFoundException("Customer Not Found"));

        return mapper.fromCustomer(customer);

    }

    @Override
    public CustomerDto updateCustomer(CustomerDto customerDto) {

        Customer customer = mapper.fromCustomerDto(customerDto);
        Customer saved = customerRepository.save(customer);

        return mapper.fromCustomer(saved);
    }

    @Override
    public void deleteCustomer(Long customerId) {

        customerRepository.deleteById(customerId);
    }

    @Override
    public List<OperationDto> accountHistory(String accountId) {

        List<Operation> operations = operationRepository.findByBankAccountId(accountId);

        return operations.stream().map(operation -> mapper.fromOperation(operation)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDto getAccountHistory(String accountId, Integer page, Integer size) throws BankAccountNotFoundException {

        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()-> new BankAccountNotFoundException("BankAccount not Found"));

        Page<Operation> operations = operationRepository.findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page,size));

        AccountHistoryDto accountHistoryDto = new AccountHistoryDto();

        List<OperationDto> operationsDtos = operations.getContent().stream().map(operation ->

            mapper.fromOperation(operation)).collect(Collectors.toList());

        accountHistoryDto.setOperationDto(operationsDtos);
        accountHistoryDto.setAccountId(bankAccount.getId());
        accountHistoryDto.setBalance(bankAccount.getBalance());
        accountHistoryDto.setCurrentPage(page);
        accountHistoryDto.setPageSize(size);
        accountHistoryDto.setTotalPages(operations.getTotalPages());

        return accountHistoryDto;



    }

    @Override
    public List<CustomerDto> findCustomers(String keyword) {

        List<Customer> customers = customerRepository.findAll();

        return customers.stream().map(customer -> mapper.fromCustomer(customer)).collect(Collectors.toList());
    }
}
