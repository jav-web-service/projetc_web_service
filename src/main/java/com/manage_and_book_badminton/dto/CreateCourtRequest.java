package com.manage_and_book_badminton.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateCourtRequest {
    @NotBlank
    private String name;

    private String description;

    @NotNull
    private BigDecimal pricePerHour;

    private List<String> imageUrls;
}
