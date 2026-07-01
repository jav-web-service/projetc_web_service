package com.manage_and_book_badminton.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CreateBookingRequest {
    @NotNull
    private Long courtId;
    
    @NotNull
    private Long timeSlotId;
    
    @NotNull
    private LocalDate bookingDate;
}
