package com.manage_and_book_badminton.controller;

import com.manage_and_book_badminton.dto.ResponseDTO;
import com.manage_and_book_badminton.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;

@RestController
@RequestMapping("/api/v1/files")
public class UploadController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping(value = "/upload", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<Object>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = cloudinaryService.uploadImage(file);
            return ResponseEntity.ok(new ResponseDTO<>(true, "File uploaded successfully", Collections.singletonMap("url", imageUrl), 200, LocalDateTime.now().toString()));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDTO<>(false, e.getMessage(), null, 503, LocalDateTime.now().toString()), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
