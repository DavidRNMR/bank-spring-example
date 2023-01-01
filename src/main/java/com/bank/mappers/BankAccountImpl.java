package com.bank.mappers;

import com.bank.dtos.CurrentBankDto;
import com.bank.dtos.CustomerDto;
import com.bank.dtos.OperationDto;
import com.bank.dtos.SavingAccountDto;
import com.bank.entities.CurrentAccount;
import com.bank.entities.Customer;
import com.bank.entities.Operation;
import com.bank.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountImpl {

    public CustomerDto fromCustomer (Customer customer){

        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer,customerDto);

        return customerDto;
    }

    public Customer fromCustomerDto (CustomerDto customerDto){

        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDto,customer);

        return customer;
    }

    public SavingAccountDto fromSavingAccount (SavingAccount savingAccount){

        SavingAccountDto savingAccountDto = new SavingAccountDto();

        BeanUtils.copyProperties(savingAccount,savingAccountDto);
        savingAccountDto.setCustomerDto(fromCustomer(savingAccount.getCustomer()));
        savingAccountDto.setType(savingAccount.getClass().getSimpleName());

        return savingAccountDto;
    }

    public SavingAccount fromSavingAccountDto (SavingAccountDto savingAccountDto){

        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(savingAccountDto,savingAccount);
        savingAccount.setCustomer(fromCustomerDto(savingAccountDto.getCustomerDto()));

        return savingAccount;
    }

    public CurrentBankDto fromCurrentBankAccount (CurrentAccount currentAccount){

        CurrentBankDto currentBankDto = new CurrentBankDto();
        BeanUtils.copyProperties(currentAccount,currentBankDto);

        currentBankDto.setCustomerDto(fromCustomer(currentAccount.getCustomer()));
        currentBankDto.setType(currentAccount.getClass().getSimpleName());

        return currentBankDto;
    }

    public CurrentAccount fromCurrentBankDto (CurrentBankDto currentBankDto){

        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentBankDto,currentAccount);

        currentAccount.setCustomer(fromCustomerDto(currentBankDto.getCustomerDto()));

        return currentAccount;
    }

    public OperationDto fromOperation (Operation operation){

        OperationDto operationDto = new OperationDto();
        BeanUtils.copyProperties(operation,operationDto);

        return operationDto;

    }


}
