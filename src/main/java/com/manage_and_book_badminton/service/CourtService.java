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
import org.springframework.web.multipart.MultipartFile;
import com.manage_and_book_badminton.service.CloudinaryService;

@Service
public class CourtService {

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private CourtImageRepository courtImageRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

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

    public CourtDTO addCourtImage(Long courtId, MultipartFile file) {
        try {
            Court court = courtRepository.findById(courtId)
                    .orElseThrow(() -> new RuntimeException("Court not found"));
            String imageUrl = cloudinaryService.uploadImage(file);
            CourtImage courtImage = CourtImage.builder()
                    .court(court)
                    .imageUrl(imageUrl)
                    .build();
            courtImageRepository.save(courtImage);
            return mapToDTO(court);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image: " + e.getMessage());
        }
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
