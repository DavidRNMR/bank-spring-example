package com.bank.controller;

import com.bank.dtos.*;
import com.bank.exceptions.BalanceNotSufficientException;
import com.bank.exceptions.BankAccountNotFoundException;
import com.bank.service.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/accounts/{accountId}")
    public BankAccountDto getBankAccount (@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/accounts")
    public List<BankAccountDto> listsAccounts (){
       return bankAccountService.findAllBanks();
    }

    @GetMapping("/accounts/{accountId}/operations")
    public List<OperationDto> getHistory (@PathVariable String accountId){
        return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDto getAccountHistory (@PathVariable String accountId,
                                                @RequestParam (name="page",defaultValue = "0")Integer page,
                                                @RequestParam (name="size",defaultValue = "5")Integer size) throws BankAccountNotFoundException{

        return bankAccountService.getAccountHistory(accountId,page,size);
    }

    @PostMapping("/accounts/debit")
    public DebitDto debit (@RequestBody DebitDto debitDto) throws BankAccountNotFoundException, BalanceNotSufficientException{

        this.bankAccountService.debit(debitDto.getAccountId(),debitDto.getAmount(),debitDto.getDescription());
        return debitDto;
    }

    @PostMapping("/accounts/credit")
    public CreditDto credit (@RequestBody CreditDto creditDto)throws BankAccountNotFoundException{

        this.bankAccountService.credit(creditDto.getAccountId(),creditDto.getAmount(),creditDto.getDescription());
        return creditDto;
    }

    @PostMapping("/accounts/transfer")
    public void transfer (@RequestBody TransferRequestDto transferRequestDto) throws BankAccountNotFoundException, BalanceNotSufficientException{

        this.bankAccountService.transfer(transferRequestDto.getAccountSource(),transferRequestDto.getAccountDestination(),transferRequestDto.getAmount());

    }

}
