package com.bank.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class DebitDto implements Serializable {

    private String accountId;
    private Float amount;
    private String description;

}
