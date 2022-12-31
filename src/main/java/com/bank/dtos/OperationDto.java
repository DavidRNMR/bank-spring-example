package com.bank.dtos;


import com.bank.enums.OperationType;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class OperationDto implements Serializable {

    private Long id;
    private Date operationDate;
    private Float amount;
    private OperationType operationType;
    private String description;
}
