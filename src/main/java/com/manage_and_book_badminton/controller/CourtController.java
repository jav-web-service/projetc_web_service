package com.manage_and_book_badminton.controller;

import com.manage_and_book_badminton.dto.CourtDTO;
import com.manage_and_book_badminton.dto.CreateCourtRequest;
import com.manage_and_book_badminton.dto.ResponseDTO;
import com.manage_and_book_badminton.service.CourtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CourtController {

    @Autowired
    private CourtService courtService;

    // Public endpoint to view all courts
    @GetMapping("/courts")
    public ResponseEntity<ResponseDTO<List<CourtDTO>>> getAllCourts() {
        List<CourtDTO> courts = courtService.getAllCourts();
        return ResponseEntity.ok(new ResponseDTO<>(true, "Success", courts, 200, LocalDateTime.now().toString()));
    }

    // Public endpoint to view specific court
    @GetMapping("/courts/{id}")
    public ResponseEntity<ResponseDTO<CourtDTO>> getCourtById(@PathVariable Long id) {
        CourtDTO court = courtService.getCourtById(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Success", court, 200, LocalDateTime.now().toString()));
    }

    // Manager endpoint to create court with images
    @PostMapping("/manager/courts")
    public ResponseEntity<ResponseDTO<CourtDTO>> createCourt(@Valid @RequestBody CreateCourtRequest request) {
        CourtDTO courtDTO = courtService.createCourt(request);
        return new ResponseEntity<>(new ResponseDTO<>(true, "Court created successfully", courtDTO, 201, LocalDateTime.now().toString()), HttpStatus.CREATED);
    }
}
