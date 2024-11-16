package org.bancomaldaver.models;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Getter
@Setter
public final class Reports {
  private String type;
  private LocalDateTime generationDate;
  private List<String> data;
}
