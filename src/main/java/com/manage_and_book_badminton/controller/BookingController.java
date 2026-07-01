package com.manage_and_book_badminton.controller;

import com.manage_and_book_badminton.dto.BookingDTO;
import com.manage_and_book_badminton.dto.CreateBookingRequest;
import com.manage_and_book_badminton.dto.ResponseDTO;
import com.manage_and_book_badminton.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/customer/bookings")
    public ResponseEntity<ResponseDTO<BookingDTO>> createBooking(@Valid @RequestBody CreateBookingRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        BookingDTO bookingDTO = bookingService.createBooking(request, username);
        
        return new ResponseEntity<>(new ResponseDTO<>(true, "Booking created successfully", bookingDTO, 201, LocalDateTime.now().toString()), HttpStatus.CREATED);
    }
    
    @GetMapping("/customer/bookings")
    public ResponseEntity<ResponseDTO<List<BookingDTO>>> getMyBookings() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<BookingDTO> bookings = bookingService.getCustomerBookings(username);
        
        return ResponseEntity.ok(new ResponseDTO<>(true, "Success", bookings, 200, LocalDateTime.now().toString()));
    }
    
    @PutMapping("/manager/bookings/{id}/approve")
    public ResponseEntity<ResponseDTO<BookingDTO>> approveBooking(@PathVariable Long id) {
        BookingDTO bookingDTO = bookingService.approveBooking(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Booking approved", bookingDTO, 200, LocalDateTime.now().toString()));
    }

    @PutMapping("/manager/bookings/{id}/reject")
    public ResponseEntity<ResponseDTO<BookingDTO>> rejectBooking(@PathVariable Long id) {
        BookingDTO bookingDTO = bookingService.rejectBooking(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Booking rejected", bookingDTO, 200, LocalDateTime.now().toString()));
    }
}
