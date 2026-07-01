package com.manage_and_book_badminton.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CourtDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal pricePerHour;
    private boolean active;
    private List<String> imageUrls;
}
