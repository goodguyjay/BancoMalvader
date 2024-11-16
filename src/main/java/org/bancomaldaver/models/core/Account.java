package org.bancomaldaver.models.core;

import lombok.Getter;
import lombok.Setter;
import org.bancomaldaver.models.Customer;

@Getter
@Setter
public abstract class Account {
  private int number;
  private String branch;
  private double balance;
  private Customer customer;
}
