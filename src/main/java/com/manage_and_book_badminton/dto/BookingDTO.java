package com.manage_and_book_badminton.dto;

import com.manage_and_book_badminton.entity.BookingStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class BookingDTO {
    private Long id;
    private Long courtId;
    private String courtName;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private BookingStatus status;
    private BigDecimal totalAmount;
}
