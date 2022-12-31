package com.bank.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class TransferRequestDto implements Serializable {

    private String accountSource;
    private String accountDestination;
    private Float amount;
    private String description;
}
