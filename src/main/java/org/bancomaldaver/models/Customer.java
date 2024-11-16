package org.bancomaldaver.models;

import lombok.Getter;
import lombok.Setter;
import org.bancomaldaver.models.core.User;

@Getter
@Setter
public final class Customer extends User {
  private String password;
}
