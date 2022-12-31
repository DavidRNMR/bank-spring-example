package com.bank.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AccountHistoryDto implements Serializable {

    private String accountId;
    private Float balance;
    private Integer currentPage;
    private Integer totalPages;
    private Integer pageSize;
    private List<OperationDto> operationDto;
}
