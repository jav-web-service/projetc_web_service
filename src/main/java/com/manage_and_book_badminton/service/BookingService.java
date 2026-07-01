package com.manage_and_book_badminton.service;

import com.manage_and_book_badminton.dto.BookingDTO;
import com.manage_and_book_badminton.dto.CreateBookingRequest;
import com.manage_and_book_badminton.entity.Booking;
import com.manage_and_book_badminton.entity.BookingStatus;
import com.manage_and_book_badminton.entity.Court;
import com.manage_and_book_badminton.entity.TimeSlot;
import com.manage_and_book_badminton.entity.User;
import com.manage_and_book_badminton.repository.BookingRepository;
import com.manage_and_book_badminton.repository.CourtRepository;
import com.manage_and_book_badminton.repository.TimeSlotRepository;
import com.manage_and_book_badminton.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CourtRepository courtRepository;
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    @Autowired
    private UserRepository userRepository;

    public BookingDTO createBooking(CreateBookingRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
                
        Court court = courtRepository.findById(request.getCourtId())
                .orElseThrow(() -> new RuntimeException("Court not found"));
                
        TimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(() -> new RuntimeException("TimeSlot not found"));

        // Check if there is already a PENDING or CONFIRMED booking for this court, date and timeslot
        List<Booking> existingBookings = bookingRepository.findByCourtIdAndBookingDate(request.getCourtId(), request.getBookingDate());
        
        boolean isConflict = existingBookings.stream()
                .anyMatch(b -> b.getTimeSlot().getId().equals(timeSlot.getId()) 
                            && (b.getStatus() == BookingStatus.PENDING || b.getStatus() == BookingStatus.CONFIRMED));

        if (isConflict) {
            throw new RuntimeException("Conflict: The court is already booked for the selected time slot.");
        }

        Booking booking = Booking.builder()
                .user(user)
                .court(court)
                .timeSlot(timeSlot)
                .bookingDate(request.getBookingDate())
                .status(BookingStatus.PENDING)
                .totalAmount(court.getPricePerHour()) // Assuming 1 hour slot for simplicity
                .build();

        Booking savedBooking = bookingRepository.save(booking);
        return mapToDTO(savedBooking);
    }
    
    public List<BookingDTO> getCustomerBookings(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // UC-02 Requirement: Use Stream API to map Entity -> DTO
        return bookingRepository.findByUserId(user.getId()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    public BookingDTO approveBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(BookingStatus.CONFIRMED);
        return mapToDTO(bookingRepository.save(booking));
    }

    public BookingDTO rejectBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(BookingStatus.REJECTED);
        return mapToDTO(bookingRepository.save(booking));
    }
    
    private BookingDTO mapToDTO(Booking booking) {
        return BookingDTO.builder()
                .id(booking.getId())
                .courtId(booking.getCourt().getId())
                .courtName(booking.getCourt().getName())
                .bookingDate(booking.getBookingDate())
                .startTime(booking.getTimeSlot().getStartTime())
                .endTime(booking.getTimeSlot().getEndTime())
                .status(booking.getStatus())
                .totalAmount(booking.getTotalAmount())
                .build();
    }
}
