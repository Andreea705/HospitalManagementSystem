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