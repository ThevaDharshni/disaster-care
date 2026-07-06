-- ============================================================
-- DISASTER CARE MANAGEMENT SYSTEM — DATABASE SCHEMA
-- Run this in MySQL Workbench before starting the application
-- ============================================================

CREATE DATABASE IF NOT EXISTS disaster_care;
USE disaster_care;

-- USERS TABLE
CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id VARCHAR(20) UNIQUE NOT NULL,
  name VARCHAR(100) NOT NULL,
  password VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ADMINS TABLE
CREATE TABLE IF NOT EXISTS admins (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  admin_id VARCHAR(20) UNIQUE NOT NULL,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- SHELTERS TABLE
CREATE TABLE IF NOT EXISTS shelters (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  location VARCHAR(200) NOT NULL,
  district VARCHAR(100) NOT NULL,
  capacity INT NOT NULL,
  occupied INT DEFAULT 0,
  status ENUM('ACTIVE','FULL','CLOSED') DEFAULT 'ACTIVE',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- SHELTER REGISTRATIONS
CREATE TABLE IF NOT EXISTS shelter_registrations (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id VARCHAR(20),
  shelter_id BIGINT,
  name VARCHAR(100) NOT NULL,
  age INT NOT NULL,
  gender ENUM('MALE','FEMALE','OTHER') NOT NULL,
  blood_group VARCHAR(10),
  identification_mark VARCHAR(255),
  registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (shelter_id) REFERENCES shelters(id)
);

-- DONATIONS TABLE
CREATE TABLE IF NOT EXISTS donations (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  donor_name VARCHAR(100) NOT NULL,
  item_type ENUM('FOOD','CLOTHES','WATER','MEDICINE','OTHER') NOT NULL,
  quantity VARCHAR(100) NOT NULL,
  address TEXT NOT NULL,
  contact_number VARCHAR(15) NOT NULL,
  status ENUM('PENDING','ACCEPTED','REJECTED') DEFAULT 'PENDING',
  donated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- MISSING PERSONS TABLE
CREATE TABLE IF NOT EXISTS missing_persons (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  age INT NOT NULL,
  gender ENUM('MALE','FEMALE','OTHER') NOT NULL,
  blood_group VARCHAR(10),
  identification_mark VARCHAR(255),
  last_seen_location VARCHAR(255),
  photo_path VARCHAR(255),
  status ENUM('MISSING','FOUND','INVESTIGATING') DEFAULT 'MISSING',
  reported_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- DISTRICT STATUS TABLE
CREATE TABLE IF NOT EXISTS district_status (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  district_name VARCHAR(100) UNIQUE NOT NULL,
  status ENUM('SAFE','WARNING','DISASTER') DEFAULT 'SAFE',
  description VARCHAR(255),
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ============================================================
-- SAMPLE DATA
-- ============================================================

-- Default admin (password: admin123)
INSERT IGNORE INTO admins (admin_id, name, email, password)
VALUES ('ADMIN001', 'Super Admin', 'admin@disastercare.in', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi');

-- Sample shelters
INSERT IGNORE INTO shelters (name, location, district, capacity, occupied, status) VALUES
('Government School',    'Anna Nagar',    'Chennai',     500, 320, 'ACTIVE'),
('Community Hall',       'MG Road',       'Chennai',     300, 300, 'FULL'),
('College Auditorium',   'Gandhi Street', 'Chennai',     400, 250, 'ACTIVE'),
('Private Building',     'Kamaraj Road',  'Chennai',     200, 200, 'FULL'),
('Marriage Hall',        'Periyar Nagar', 'Chennai',     150,  60, 'ACTIVE'),
('District Collectorate','Beach Road',    'Rameswaram',  600, 410, 'ACTIVE'),
('Municipal Hall',       'Town Centre',   'Kanyakumari', 350, 280, 'ACTIVE'),
('Govt ITI Building',    'Main Street',   'Madurai',     500,  90, 'ACTIVE');

-- District status
INSERT IGNORE INTO district_status (district_name, status, description) VALUES
('Chennai',       'DISASTER', 'Severe flooding — multiple areas affected'),
('Kanyakumari',   'DISASTER', 'Cyclone landfall — evacuation in progress'),
('Rameswaram',    'WARNING',  'Heavy rain warning — coastal alert'),
('Cuddalore',     'WARNING',  'High tide alert'),
('Nagapattinam',  'WARNING',  'Cyclone watch'),
('Coimbatore',    'SAFE',     'Normal conditions'),
('Madurai',       'SAFE',     'Normal conditions'),
('Tiruchirappalli','SAFE',    'Normal conditions'),
('Salem',         'SAFE',     'Normal conditions'),
('Tirunelveli',   'SAFE',     'Normal conditions'),
('Vellore',       'SAFE',     'Normal conditions'),
('Erode',         'SAFE',     'Normal conditions'),
('Thanjavur',     'SAFE',     'Normal conditions'),
('Dindigul',      'SAFE',     'Normal conditions'),
('Tiruppur',      'SAFE',     'Normal conditions');
