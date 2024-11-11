package org.bancomaldaver.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bancomaldaver.models.core.Account;

@Getter
@Setter
@RequiredArgsConstructor
public class SavingsAccount extends Account {
    @NonNull
    private double interestRate;

    public double calculateInterest() {
        return getBalance() * interestRate;
    }
}