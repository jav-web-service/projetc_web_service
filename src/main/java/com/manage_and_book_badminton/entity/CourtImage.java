package com.manage_and_book_badminton.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "court_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourtImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;

    @Column(nullable = false)
    private String imageUrl;
}
