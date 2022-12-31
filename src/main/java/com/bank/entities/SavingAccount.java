package com.bank.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="saving_account")
public class SavingAccount extends BankAccount{

    private Float interestRate;
}
