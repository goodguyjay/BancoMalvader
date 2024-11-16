package org.bancomaldaver.models;

import lombok.Getter;
import lombok.Setter;
import org.bancomaldaver.models.core.Account;

@Getter
@Setter
public final class SavingsAccount extends Account {
  private double interestRate;
}
