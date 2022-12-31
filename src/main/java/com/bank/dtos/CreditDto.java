package com.bank.dtos;

import lombok.Data;

import java.io.Serializable;
@Data
public class CreditDto implements Serializable {

    private String accountId;
    private Float amount;
    private String description;
}
