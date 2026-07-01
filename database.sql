-- Tạo database (nếu chưa có)
CREATE DATABASE IF NOT EXISTS badminton_db
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE badminton_db;

-- 1. Bảng users
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_CUSTOMER') NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- 2. Bảng token_blacklist
CREATE TABLE IF NOT EXISTS token_blacklist (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(512) NOT NULL UNIQUE,
    expiry_date DATETIME NOT NULL
);

-- 3. Bảng courts (Sân cầu lông)
CREATE TABLE IF NOT EXISTS courts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    price_per_hour DECIMAL(10, 2) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- 4. Bảng court_images (Hình ảnh sân)
CREATE TABLE IF NOT EXISTS court_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    court_id BIGINT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    CONSTRAINT fk_court_images_court FOREIGN KEY (court_id) REFERENCES courts(id) ON DELETE CASCADE
);

-- 5. Bảng time_slots (Khung giờ)
CREATE TABLE IF NOT EXISTS time_slots (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL
);

-- 6. Bảng bookings (Lịch đặt sân)
CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    court_id BIGINT NOT NULL,
    time_slot_id BIGINT NOT NULL,
    booking_date DATE NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'REJECTED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    total_amount DECIMAL(10, 2) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_bookings_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_bookings_court FOREIGN KEY (court_id) REFERENCES courts(id),
    CONSTRAINT fk_bookings_timeslot FOREIGN KEY (time_slot_id) REFERENCES time_slots(id)
);

-- -------------------------------------------------------------------------
-- DỮ LIỆU MẪU (SEED DATA)
-- -------------------------------------------------------------------------

-- Insert Tài khoản Admin (Mật khẩu mặc định: 123456 -> Đã được mã hóa BCrypt)
INSERT INTO users (username, email, password, role, active) VALUES
('admin', 'admin@badminton.com', '$2a$10$wY.uV7w/hC9DqzG2p.Zz/ePZ/0rP0rP0rP0rP0rP0rP0rP0rP0rP0', 'ROLE_ADMIN', 1),
('manager1', 'manager1@badminton.com', '$2a$10$wY.uV7w/hC9DqzG2p.Zz/ePZ/0rP0rP0rP0rP0rP0rP0rP0rP0rP0', 'ROLE_MANAGER', 1),
('customer1', 'customer1@badminton.com', '$2a$10$wY.uV7w/hC9DqzG2p.Zz/ePZ/0rP0rP0rP0rP0rP0rP0rP0rP0rP0', 'ROLE_CUSTOMER', 1);
-- Lưu ý: Bạn có thể thay đổi chuỗi mật khẩu mã hóa BCrypt nếu cần thiết bằng đoạn code Encoder trong Spring. Mật khẩu trên chỉ mang tính chất placeholder.

-- Insert Dữ liệu Sân cầu lông
INSERT INTO courts (name, description, price_per_hour, active) VALUES
('Sân VIP 1', 'Sân thảm cao cấp, có máy lạnh', 150000.00, 1),
('Sân Thường 1', 'Sân thảm tiêu chuẩn, quạt gió', 100000.00, 1),
('Sân Thường 2', 'Sân thảm tiêu chuẩn, quạt gió', 100000.00, 1);

-- Insert Các khung giờ (Ví dụ từ 17:00 đến 20:00)
INSERT INTO time_slots (start_time, end_time) VALUES
('17:00:00', '18:00:00'),
('18:00:00', '19:00:00'),
('19:00:00', '20:00:00');
