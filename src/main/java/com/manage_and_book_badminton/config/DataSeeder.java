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
        // Kiểm tra xem database đã có admin chưa, nếu chưa thì tự động tạo
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@badminton.com")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.ROLE_ADMIN)
                    .active(true)
                    .build();
            userRepository.save(admin);
            System.out.println("[DataSeeder] Đã tự động tạo tài khoản ADMIN mặc định (username: admin / password: password)");
        }

        // Tạo tài khoản Manager mặc định
        if (!userRepository.existsByUsername("manager")) {
            User manager = User.builder()
                    .username("manager")
                    .email("manager@badminton.com")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.ROLE_MANAGER)
                    .active(true)
                    .build();
            userRepository.save(manager);
            System.out.println("[DataSeeder] Đã tự động tạo tài khoản MANAGER mặc định (username: manager / password: password)");
        }

        // Tạo dữ liệu sân mẫu nếu chưa có
        if (courtRepository.count() == 0) {
            Court court1 = Court.builder().name("Sân VIP 1").active(true).pricePerHour(BigDecimal.valueOf(100.0)).build();
            courtRepository.save(court1);
            System.out.println("[DataSeeder] Đã tự động tạo Sân mẫu (ID = 1)");
        }

        // Tạo dữ liệu khung giờ mẫu nếu chưa có
        if (timeSlotRepository.count() == 0) {
            TimeSlot slot1 = TimeSlot.builder().startTime(LocalTime.of(17, 0)).endTime(LocalTime.of(18, 0)).build();
            timeSlotRepository.save(slot1);
            System.out.println("[DataSeeder] Đã tự động tạo Khung giờ mẫu (ID = 1)");
        }
    }
}
