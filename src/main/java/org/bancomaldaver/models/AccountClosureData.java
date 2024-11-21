package org.bancomaldaver.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public final class AccountClosureData {
  private final String cpf;
  private final int userId;
  private final int customerId;
  private final String accountNumber;
}
