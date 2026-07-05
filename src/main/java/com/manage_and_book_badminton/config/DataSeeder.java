package com.manage_and_book_badminton.config;

import com.manage_and_book_badminton.entity.Role;
import com.manage_and_book_badminton.entity.User;
import com.manage_and_book_badminton.repository.UserRepository;
import com.manage_and_book_badminton.entity.Court;
import com.manage_and_book_badminton.entity.TimeSlot;
import com.manage_and_book_badminton.repository.CourtRepository;
import com.manage_and_book_badminton.repository.TimeSlotRepository;
import java.math.BigDecimal;
import java.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Seed Users (Admin, Manager, Customers)
        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(User.builder().username("admin").email("admin@badminton.com").password(passwordEncoder.encode("password")).role(Role.ROLE_ADMIN).active(true).build());
            System.out.println("[DataSeeder] Đã tạo ADMIN (admin/password)");
        }
        if (!userRepository.existsByUsername("manager")) {
            userRepository.save(User.builder().username("manager").email("manager@badminton.com").password(passwordEncoder.encode("password")).role(Role.ROLE_MANAGER).active(true).build());
            System.out.println("[DataSeeder] Đã tạo MANAGER (manager/password)");
        }
        if (!userRepository.existsByUsername("customer1")) {
            userRepository.save(User.builder().username("customer1").email("customer1@badminton.com").password(passwordEncoder.encode("password")).role(Role.ROLE_CUSTOMER).active(true).build());
            System.out.println("[DataSeeder] Đã tạo CUSTOMER 1 (customer1/password)");
        }
        if (!userRepository.existsByUsername("customer2")) {
            userRepository.save(User.builder().username("customer2").email("customer2@badminton.com").password(passwordEncoder.encode("password")).role(Role.ROLE_CUSTOMER).active(true).build());
            System.out.println("[DataSeeder] Đã tạo CUSTOMER 2 (customer2/password)");
        }

        // 2. Seed Courts (Sân cầu lông)
        if (courtRepository.count() < 5) {
            java.util.List<Court> courts = java.util.Arrays.asList(
                Court.builder().name("Sân VIP 1").description("Sân thảm cao cấp, có máy lạnh").active(true).pricePerHour(BigDecimal.valueOf(150000)).build(),
                Court.builder().name("Sân VIP 2").description("Sân thảm cao cấp, có máy lạnh").active(true).pricePerHour(BigDecimal.valueOf(150000)).build(),
                Court.builder().name("Sân Thường 1").description("Sân thảm tiêu chuẩn, quạt gió").active(true).pricePerHour(BigDecimal.valueOf(100000)).build(),
                Court.builder().name("Sân Thường 2").description("Sân thảm tiêu chuẩn, quạt gió").active(true).pricePerHour(BigDecimal.valueOf(100000)).build(),
                Court.builder().name("Sân Thường 3").description("Sân thảm tiêu chuẩn, quạt gió").active(true).pricePerHour(BigDecimal.valueOf(100000)).build()
            );
            // Xóa cũ để tránh trùng lặp nếu cần, nhưng tạm thời lưu mới
            courtRepository.deleteAll();
            courtRepository.saveAll(courts);
            System.out.println("[DataSeeder] Đã thêm 5 sân cầu lông.");
        }

        // 3. Seed TimeSlots (Khung giờ)
        if (timeSlotRepository.count() < 8) {
            java.util.List<TimeSlot> timeSlots = new java.util.ArrayList<>();
            // Tạo các ca chiều tối (16:00 đến 22:00)
            for (int i = 16; i <= 21; i++) {
                timeSlots.add(TimeSlot.builder().startTime(LocalTime.of(i, 0)).endTime(LocalTime.of(i + 1, 0)).build());
            }
            // Tạo thêm ca sáng (7:00 đến 9:00)
            timeSlots.add(TimeSlot.builder().startTime(LocalTime.of(7, 0)).endTime(LocalTime.of(8, 0)).build());
            timeSlots.add(TimeSlot.builder().startTime(LocalTime.of(8, 0)).endTime(LocalTime.of(9, 0)).build());
            
            timeSlotRepository.deleteAll();
            timeSlotRepository.saveAll(timeSlots);
            System.out.println("[DataSeeder] Đã thêm 8 khung giờ.");
        }
    }
}
