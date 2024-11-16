package org.bancomaldaver.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public final class Address {
  private String zipCode;
  private String street;
  private int houseNumber;
  private String neighborhood;
  private String city;
  private String state;
}
