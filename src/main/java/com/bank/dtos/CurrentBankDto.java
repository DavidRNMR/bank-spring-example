package com.bank.dtos;

import com.bank.enums.AccountStatus;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CurrentBankDto implements Serializable {

    private String id;
    private Double balance;
    private Date createAt;
    private AccountStatus status;
    private CustomerDto customerDto;
    private Double overDraft;
}
