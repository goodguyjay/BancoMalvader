package org.bancomaldaver.models.core;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bancomaldaver.models.Address;

import java.time.LocalDate;

@Setter
@Getter
public abstract class User {
    @NonNull
    private int id;
    @NonNull
    private String name;
    @NonNull
    private String cpf;
    private LocalDate birthDate;
    private String number;
    private Address address;

    abstract boolean login(String password);

    abstract void logout();

    abstract String checkData();

    abstract void changePassword(String password);
}