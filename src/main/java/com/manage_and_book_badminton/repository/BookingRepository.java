package com.manage_and_book_badminton.repository;

import com.manage_and_book_badminton.entity.Booking;
import com.manage_and_book_badminton.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByCourtIdAndBookingDateAndStatus(Long courtId, LocalDate bookingDate, BookingStatus status);
    List<Booking> findByCourtIdAndBookingDate(Long courtId, LocalDate bookingDate);
}
