package com.manage_and_book_badminton.repository;

import com.manage_and_book_badminton.entity.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {
}
