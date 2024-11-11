package org.bancomaldaver.models;

import lombok.*;
import org.bancomaldaver.models.core.Account;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class CheckingAccount extends Account {
    @NonNull
    private double limit;
    @NonNull
    private LocalDate dueDate;
}