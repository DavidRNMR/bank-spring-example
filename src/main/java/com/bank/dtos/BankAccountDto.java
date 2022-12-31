package com.bank.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class BankAccountDto implements Serializable {

    private String type;
}
