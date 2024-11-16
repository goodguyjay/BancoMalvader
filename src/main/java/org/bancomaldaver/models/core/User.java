package org.bancomaldaver.models.core;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.bancomaldaver.models.Address;

@Setter
@Getter
public abstract class User {
  private int id;
  private String name;
  private String cpf;
  private LocalDate birthDate;
  private String phone;
  private Address address;
}
