package org.bancomaldaver.models;

import lombok.Getter;
import lombok.Setter;
import org.bancomaldaver.models.core.User;

@Getter
@Setter
public final class Employee extends User {
  private String employeeCode;
  private String role;
  private String password;
}
