package org.bancomaldaver.models;

import java.time.LocalDate;
import lombok.*;
import org.bancomaldaver.models.core.Account;

@Getter
@Setter
public final class CheckingAccount extends Account {
  private double limit;
  private LocalDate dueDate;
}
