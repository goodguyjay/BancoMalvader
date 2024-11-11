package org.bancomaldaver.models;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Reports {
    @NonNull
    private String type;
    private LocalDateTime generationDate;
    private List<String> data;
}