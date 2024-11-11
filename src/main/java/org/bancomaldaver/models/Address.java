package org.bancomaldaver.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class Address {
    @NonNull
    private String cep;
    @NonNull
    private String fullAddress;
    private int houseNumber;
    private String neighborhood;
    @NonNull
    private String city;
    @NonNull
    private String state;
}