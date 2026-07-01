package com.manage_and_book_badminton.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.math.BigDecimal;

@Entity
@Table(name = "courts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private BigDecimal pricePerHour;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "court", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CourtImage> images;
    
    @OneToMany(mappedBy = "court", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;
}
