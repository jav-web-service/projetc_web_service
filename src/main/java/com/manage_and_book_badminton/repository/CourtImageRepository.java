package com.manage_and_book_badminton.repository;

import com.manage_and_book_badminton.entity.CourtImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourtImageRepository extends JpaRepository<CourtImage, Long> {
}
