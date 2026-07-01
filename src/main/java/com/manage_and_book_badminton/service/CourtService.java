package com.manage_and_book_badminton.service;

import com.manage_and_book_badminton.dto.CourtDTO;
import com.manage_and_book_badminton.dto.CreateCourtRequest;
import com.manage_and_book_badminton.entity.Court;
import com.manage_and_book_badminton.entity.CourtImage;
import com.manage_and_book_badminton.repository.CourtImageRepository;
import com.manage_and_book_badminton.repository.CourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourtService {

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private CourtImageRepository courtImageRepository;

    public CourtDTO createCourt(CreateCourtRequest request) {
        Court court = Court.builder()
                .name(request.getName())
                .description(request.getDescription())
                .pricePerHour(request.getPricePerHour())
                .active(true)
                .build();
        
        Court savedCourt = courtRepository.save(court);

        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            List<CourtImage> images = request.getImageUrls().stream()
                    .map(url -> CourtImage.builder().court(savedCourt).imageUrl(url).build())
                    .collect(Collectors.toList());
            courtImageRepository.saveAll(images);
            savedCourt.setImages(images);
        }

        return mapToDTO(savedCourt);
    }

    public List<CourtDTO> getAllCourts() {
        return courtRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public CourtDTO getCourtById(Long id) {
        Court court = courtRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Court not found"));
        return mapToDTO(court);
    }

    private CourtDTO mapToDTO(Court court) {
        List<String> imageUrls = court.getImages() != null ? 
                court.getImages().stream().map(CourtImage::getImageUrl).collect(Collectors.toList()) : 
                List.of();

        return CourtDTO.builder()
                .id(court.getId())
                .name(court.getName())
                .description(court.getDescription())
                .pricePerHour(court.getPricePerHour())
                .active(court.isActive())
                .imageUrls(imageUrls)
                .build();
    }
}
