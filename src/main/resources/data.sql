-- ============================================
-- 100% WORKING data.sql
-- ============================================

-- 1. FIRST: Disable foreign key checks
SET FOREIGN_KEY_CHECKS = 0;

-- 2. Clear ALL old data
DELETE FROM rooms;
DELETE FROM departments;
DELETE FROM hospitals;

-- 3. Reset auto-increment
ALTER TABLE hospitals AUTO_INCREMENT = 1;
ALTER TABLE departments AUTO_INCREMENT = 1;
ALTER TABLE rooms AUTO_INCREMENT = 1;

-- 4. Insert 10 HOSPITALS (exactly 10)
INSERT INTO hospitals (name, city) VALUES
('Charité Berlin', 'Berlin'),
('UK Hamburg', 'Hamburg'),
('Klinikum München', 'München'),
('UK Heidelberg', 'Heidelberg'),
('Klinikum Stuttgart', 'Stuttgart'),
('UK Frankfurt', 'Frankfurt'),
('MHH Hannover', 'Hannover'),
('UK Köln', 'Köln'),
('UK Düsseldorf', 'Düsseldorf'),
('UK Leipzig', 'Leipzig');

-- 5. Insert 10+ DEPARTMENTS (for first 3 hospitals)
INSERT INTO departments (name, room_numbers, department_head, hospital_id) VALUES
('Cardiology', 15, 'Dr. Schmidt', 1),
('Neurology', 12, 'Dr. Müller', 1),
('Pediatrics', 20, 'Dr. Wagner', 1),
('Surgery', 18, 'Dr. Fischer', 1),
('Orthopedics', 10, 'Dr. Weber', 1),
('Gynecology', 14, 'Dr. Hoffmann', 1),
('Dermatology', 6, 'Dr. Becker', 1),
('Psychiatry', 18, 'Dr. Schulz', 1),
('Radiology', 5, 'Dr. Koch', 1),
('Emergency', 25, 'Dr. Bauer', 1);

-- 6. Insert 10+ ROOMS (for first 3 hospitals)
INSERT INTO rooms (room_number, type, capacity, available, hospital_id) VALUES
('101', 'Patient Room', 2, true, 1),
('102', 'Patient Room', 2, true, 1),
('103', 'ICU', 1, true, 1),
('104', 'ICU', 1, false, 1),
('201', 'Operation Room', 1, true, 1),
('202', 'Operation Room', 1, true, 1),
('301', 'Examination Room', 1, true, 1),
('302', 'Examination Room', 1, true, 1),
('401', 'Isolation Room', 1, true, 1),
('501', 'Delivery Room', 1, true, 1);

-- 7. Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- 8. Verify data
SELECT '=== DATA VERIFICATION ===' AS '';
SELECT 'Hospitals: ' AS '', COUNT(*) FROM hospitals;
SELECT 'Departments: ' AS '', COUNT(*) FROM departments;
SELECT 'Rooms: ' AS '', COUNT(*) FROM rooms;


DELETE FROM patients;
INSERT INTO patients (patient_id, name, email, phone_number, date_of_birth, emergency_contact, registration_date) VALUES
('PAT001', 'John Smith', 'john.smith@email.com', '+1-555-0101', '1985-03-15', 'Mary Smith: +1-555-0102', NOW()),
('PAT002', 'Maria Garcia', 'maria.g@email.com', '+1-555-0202', '1990-07-22', 'Carlos Garcia: +1-555-0203', NOW()),
('PAT003', 'Robert Johnson', 'robert.j@email.com', '+1-555-0303', '1978-11-30', 'Lisa Johnson: +1-555-0304', NOW()),
('PAT004', 'Sarah Miller', 'sarah.m@email.com', '+1-555-0404', '1995-01-10', 'Tom Miller: +1-555-0405', NOW()),
('PAT005', 'James Wilson', 'james.w@email.com', '+1-555-0505', '1982-09-05', 'Emily Wilson: +1-555-0506', NOW()),
('PAT006', 'Emma Brown', 'emma.b@email.com', '+1-555-0606', '1992-12-18', 'Michael Brown: +1-555-0607', NOW()),
('PAT007', 'Daniel Lee', 'daniel.l@email.com', '+1-555-0707', '1988-04-25',  'Sophia Lee: +1-555-0708', NOW()),
('PAT008', 'Olivia Davis', 'olivia.d@email.com', '+1-555-0808', '1998-06-12', 'William Davis: +1-555-0809', NOW()),
('PAT009', 'William Taylor', 'william.t@email.com', '+1-555-0909', '1975-08-08', 'Charlotte Taylor: +1-555-0910', NOW()),
('PAT010', 'Sophia Martinez', 'sophia.m@email.com', '+1-555-1010', '1993-02-28','David Martinez: +1-555-1011', NOW());