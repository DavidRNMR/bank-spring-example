package com.bank.controller;

import com.bank.dtos.CustomerDto;
import com.bank.exceptions.CustomerNotFoundException;
import com.bank.service.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@AllArgsConstructor
public class CustomerController {

    private final BankAccountService bankAccountService;

    @GetMapping("/customers")
    public List<CustomerDto> findCustomers (){
        return bankAccountService.findAllCustomers();
    }

    @GetMapping("/customers/search")
    public List<CustomerDto> searchCustomers(@RequestParam(name = "*",defaultValue = "")String keyword){
        return bankAccountService.findCustomers("%"+keyword+"%");
    }

    @GetMapping("/customers/{id}")
    public CustomerDto getOne (@PathVariable Long id) throws CustomerNotFoundException{
        return bankAccountService.getOneCustomer(id);
    }

    @PostMapping("/customers")
    public CustomerDto addOne (@RequestBody CustomerDto customerDto){
        return bankAccountService.saveCustomer(customerDto);
    }

    @PutMapping("/customers/{id}")
    public CustomerDto update (@PathVariable Long id, @RequestBody CustomerDto customerDto){

        customerDto.setId(id);
        return bankAccountService.updateCustomer(customerDto);
    }

    @DeleteMapping("/customers/{id}")
    public void delete (@PathVariable Long id){
        bankAccountService.deleteCustomer(id);
    }
}
