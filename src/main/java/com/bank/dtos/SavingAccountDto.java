package com.bank.dtos;

import com.bank.enums.AccountStatus;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SavingAccountDto implements Serializable {

    private String id;
    private Float balance;
    private Date createAt;
    private AccountStatus status;
    private CustomerDto customerDto;
    private Float interestRate;
}
