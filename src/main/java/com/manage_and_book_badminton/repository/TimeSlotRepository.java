package com.manage_and_book_badminton.repository;

import com.manage_and_book_badminton.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
}
