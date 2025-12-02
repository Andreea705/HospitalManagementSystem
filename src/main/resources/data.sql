-- -- ============================================
-- -- 100% WORKING data.sql
-- -- ============================================
--
-- -- 1. FIRST: Disable foreign key checks
-- SET FOREIGN_KEY_CHECKS = 0;
--
-- -- 2. Clear ALL old data
-- DELETE FROM rooms;
-- DELETE FROM departments;
-- DELETE FROM hospitals;
-- DELETE FROM appointments;
-- DELETE FROM doctors;
-- DELETE FROM medical_staff;
--
-- -- 3. Reset auto-increment
-- ALTER TABLE hospitals AUTO_INCREMENT = 1;
-- ALTER TABLE departments AUTO_INCREMENT = 1;
-- ALTER TABLE rooms AUTO_INCREMENT = 1;
-- ALTER TABLE appointments AUTO_INCREMENT = 1;
-- ALTER TABLE doctors AUTO_INCREMENT = 1;
-- ALTER TABLE medical_staff AUTO_INCREMENT = 1;
--
-- -- 4. Insert 10 HOSPITALS (exactly 10)
-- INSERT INTO hospitals (name, city) VALUES
-- ('Charité Berlin', 'Berlin'),
-- ('UK Hamburg', 'Hamburg'),
-- ('Klinikum München', 'München'),
-- ('UK Heidelberg', 'Heidelberg'),
-- ('Klinikum Stuttgart', 'Stuttgart'),
-- ('UK Frankfurt', 'Frankfurt'),
-- ('MHH Hannover', 'Hannover'),
-- ('UK Köln', 'Köln'),
-- ('UK Düsseldorf', 'Düsseldorf'),
-- ('UK Leipzig', 'Leipzig');
--
-- -- 5. Insert 10+ DEPARTMENTS (for first 3 hospitals)
-- INSERT INTO departments (name, room_numbers, department_head, hospital_id) VALUES
-- ('Cardiology', 15, 'Dr. Schmidt', 1),
-- ('Neurology', 12, 'Dr. Müller', 1),
-- ('Pediatrics', 20, 'Dr. Wagner', 1),
-- ('Surgery', 18, 'Dr. Fischer', 1),
-- ('Orthopedics', 10, 'Dr. Weber', 1),
-- ('Gynecology', 14, 'Dr. Hoffmann', 1),
-- ('Dermatology', 6, 'Dr. Becker', 1),
-- ('Psychiatry', 18, 'Dr. Schulz', 1),
-- ('Radiology', 5, 'Dr. Koch', 1),
-- ('Emergency', 25, 'Dr. Bauer', 1);
--
-- -- 6. Insert 10+ ROOMS (for first 3 hospitals)
-- INSERT INTO rooms (room_number, type, capacity, available, hospital_id) VALUES
-- ('101', 'Patient Room', 2, true, 1),
-- ('102', 'Patient Room', 2, true, 1),
-- ('103', 'ICU', 1, true, 1),
-- ('104', 'ICU', 1, false, 1),
-- ('201', 'Operation Room', 1, true, 1),
-- ('202', 'Operation Room', 1, true, 1),
-- ('301', 'Examination Room', 1, true, 1),
-- ('302', 'Examination Room', 1, true, 1),
-- ('401', 'Isolation Room', 1, true, 1),
-- ('501', 'Delivery Room', 1, true, 1);
--
--
-- -- 7. Insert DOCTORS (10 doctors)
-- INSERT INTO doctors (name, specialization, email, phone, department_id) VALUES
-- ('Dr. Anna Schmidt', 'Cardiologist', 'anna.schmidt@hospital.com', '+49 30 1234567', 1),
-- ('Dr. Thomas Müller', 'Neurologist', 'thomas.mueller@hospital.com', '+49 30 1234568', 2),
-- ('Dr. Sarah Wagner', 'Pediatrician', 'sarah.wagner@hospital.com', '+49 30 1234569', 3),
-- ('Dr. Michael Fischer', 'Surgeon', 'michael.fischer@hospital.com', '+49 30 1234570', 4),
-- ('Dr. Julia Weber', 'Orthopedist', 'julia.weber@hospital.com', '+49 30 1234571', 5),
-- ('Dr. Robert Hoffmann', 'Gynecologist', 'robert.hoffmann@hospital.com', '+49 30 1234572', 6),
-- ('Dr. Lisa Becker', 'Dermatologist', 'lisa.becker@hospital.com', '+49 30 1234573', 7),
-- ('Dr. David Schulz', 'Psychiatrist', 'david.schulz@hospital.com', '+49 30 1234574', 8),
-- ('Dr. Maria Koch', 'Radiologist', 'maria.koch@hospital.com', '+49 30 1234575', 9),
-- ('Dr. Stefan Bauer', 'Emergency Medicine', 'stefan.bauer@hospital.com', '+49 30 1234576', 10);
--
-- -- 8. Insert APPOINTMENTS (at least 10 appointments)
-- INSERT INTO appointments (appointment_date, patient_name, description, status, department_id, doctor_id) VALUES
-- -- Active appointments (future dates)
-- (DATE_ADD(NOW(), INTERVAL 1 DAY), 'Max Mustermann', 'Regular checkup', 'ACTIVE', 1, 1),
-- (DATE_ADD(NOW(), INTERVAL 2 DAY), 'Erika Musterfrau', 'Heart examination', 'ACTIVE', 1, 1),
-- (DATE_ADD(NOW(), INTERVAL 3 DAY), 'Klaus Klein', 'Headache consultation', 'ACTIVE', 2, 2),
-- (DATE_ADD(NOW(), INTERVAL 1 DAY), 'Sabine Groß', 'Child vaccination', 'ACTIVE', 3, 3),
-- (DATE_ADD(NOW(), INTERVAL 4 DAY), 'Peter Schmidt', 'Knee surgery follow-up', 'ACTIVE', 5, 5),
--
-- -- Completed appointments (past dates)
-- (DATE_SUB(NOW(), INTERVAL 2 DAY), 'Monika Fischer', 'Skin allergy test', 'COMPLETED', 7, 7),
-- (DATE_SUB(NOW(), INTERVAL 1 DAY), 'Hans Meier', 'Psychological consultation', 'COMPLETED', 8, 8),
-- (DATE_SUB(NOW(), INTERVAL 3 DAY), 'Gabi Weber', 'X-ray examination', 'COMPLETED', 9, 9),
--
-- -- More active appointments
-- (DATE_ADD(NOW(), INTERVAL 5 DAY), 'Dieter Schulz', 'Emergency treatment', 'ACTIVE', 10, 10),
-- (DATE_ADD(NOW(), INTERVAL 6 DAY), 'Brigitte Koch', 'Pregnancy check', 'ACTIVE', 6, 6),
-- (DATE_ADD(NOW(), INTERVAL 2 DAY), 'Frank Wagner', 'Broken arm treatment', 'ACTIVE', 4, 4),
-- (DATE_ADD(NOW(), INTERVAL 7 DAY), 'Laura Hoffmann', 'Annual checkup', 'ACTIVE', 1, 1);
--
--
-- INSERT INTO medical_staff (medical_staff_name, medical_staff_id, role, department_id) VALUES
-- -- Doctors
-- ('Dr. Anna Schmidt', 'DOC-001', 'Doctor', 1),
-- ('Dr. Thomas Müller', 'DOC-002', 'Doctor', 2),
-- ('Dr. Sarah Wagner', 'DOC-003', 'Doctor', 3),
-- ('Dr. Michael Fischer', 'DOC-004', 'Doctor', 4),
-- ('Dr. Julia Weber', 'DOC-005', 'Doctor', 5),
-- ('Dr. Robert Hoffmann', 'DOC-006', 'Doctor', 6),
-- ('Dr. Lisa Becker', 'DOC-007', 'Doctor', 7),
-- ('Dr. David Schulz', 'DOC-008', 'Doctor', 8),
-- ('Dr. Maria Koch', 'DOC-009', 'Doctor', 9),
-- ('Dr. Stefan Bauer', 'DOC-010', 'Doctor', 10),
--
-- -- 9. Re-enable foreign key checks
-- SET FOREIGN_KEY_CHECKS = 1;
--
-- -- 10. Verify data
-- SELECT '=== DATA VERIFICATION ===' AS '';
-- SELECT 'Hospitals: ' AS '', COUNT(*) FROM hospitals;
-- SELECT 'Departments: ' AS '', COUNT(*) FROM departments;
-- SELECT 'Rooms: ' AS '', COUNT(*) FROM rooms;

-- ============================================
-- DATA.SQL - EXACT 10 RECORDS PER ENTITY
-- ============================================

-- 1. Disable foreign key checks
SET FOREIGN_KEY_CHECKS = 0;

-- 2. Clear ALL old data
DELETE FROM appointments;
DELETE FROM doctors;
DELETE FROM medical_staff;
DELETE FROM departments;
DELETE FROM hospitals;

-- 3. Reset auto-increment
ALTER TABLE hospitals AUTO_INCREMENT = 1;
ALTER TABLE departments AUTO_INCREMENT = 1;
ALTER TABLE medical_staff AUTO_INCREMENT = 1;
ALTER TABLE doctors AUTO_INCREMENT = 1;
ALTER TABLE appointments AUTO_INCREMENT = 1;

-- 4. Enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

--============ HOSPITALS  ============
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

-- ============ DEPARTMENTS ============
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

-- ============ MEDICAL_STAFF (PARINTE) - 10 records ============
INSERT INTO medical_staff (medical_staff_id, medical_staff_name, role, department_id) VALUES
('DOC001', 'Dr. Anna Schmidt', 'doctor', 1),
('DOC002', 'Dr. Thomas Müller', 'doctor', 2),
('DOC003', 'Dr. Sarah Wagner', 'doctor', 3),
('DOC004', 'Dr. Michael Fischer', 'doctor', 4),
('DOC005', 'Dr. Julia Weber', 'doctor', 5),
('DOC006', 'Dr. Robert Hoffmann', 'doctor', 6),
('DOC007', 'Dr. Lisa Becker', 'doctor', 7),
('DOC008', 'Dr. David Schulz', 'doctor', 8),
('DOC009', 'Dr. Maria Koch', 'doctor', 9),
('DOC010', 'Dr. Stefan Bauer', 'doctor', 10);

-- ============ DOCTORS (COPIL) - 10 records ============
INSERT INTO doctors (medical_staff_id, specialization, email, phone, license_number) VALUES
(1, 'Cardiologist', 'anna.schmidt@hospital.com', '+49 30 1234567', 'L-CARD-001'),
(2, 'Neurologist', 'thomas.mueller@hospital.com', '+49 30 1234568', 'L-NEUR-001'),
(3, 'Pediatrician', 'sarah.wagner@hospital.com', '+49 30 1234569', 'L-PED-001'),
(4, 'Surgeon', 'michael.fischer@hospital.com', '+49 30 1234570', 'L-SURG-001'),
(5, 'Orthopedist', 'julia.weber@hospital.com', '+49 30 1234571', 'L-ORTH-001'),
(6, 'Gynecologist', 'robert.hoffmann@hospital.com', '+49 30 1234572', 'L-GYN-001'),
(7, 'Dermatologist', 'lisa.becker@hospital.com', '+49 30 1234573', 'L-DERM-001'),
(8, 'Psychiatrist', 'david.schulz@hospital.com', '+49 30 1234574', 'L-PSYCH-001'),
(9, 'Radiologist', 'maria.koch@hospital.com', '+49 30 1234575', 'L-RAD-001'),
(10, 'Emergency Medicine', 'stefan.bauer@hospital.com', '+49 30 1234576', 'L-EMER-001');

-- ============ APPOINTMENTS ============
INSERT INTO appointments (appointment_date, patient_name, description, status, department_id, doctor_id, created_at, updated_at) VALUES
-- Cardiology appointments
('2025-12-03 09:00:00', 'Max Mustermann', 'Heart checkup', 'ACTIVE', 1, 1, NOW(), NOW()),
('2025-12-03 11:00:00', 'Erika Musterfrau', 'ECG examination', 'ACTIVE', 1, 1, NOW(), NOW()),
-- Neurology appointments
('2025-12-04 10:00:00', 'Klaus Schmidt', 'Migraine consultation', 'ACTIVE', 2, 2, NOW(), NOW()),
('2025-12-04 14:00:00', 'Sabine Groß', 'Neurological exam', 'ACTIVE', 2, 2, NOW(), NOW()),
-- Pediatrics appointments
('2025-12-05 09:30:00', 'Lena Klein (5 years)', 'Vaccination', 'ACTIVE', 3, 3, NOW(), NOW()),
('2025-12-05 13:00:00', 'Max Bauer (8 years)', 'Flu treatment', 'ACTIVE', 3, 3, NOW(), NOW()),
-- Surgery appointments
('2025-12-06 08:00:00', 'Martin Vogel', 'Pre-surgery consult', 'ACTIVE', 4, 4, NOW(), NOW()),
-- Orthopedics appointments
('2025-12-06 11:00:00', 'Frank Zimmermann', 'Knee pain', 'ACTIVE', 5, 5, NOW(), NOW()),
-- Gynecology appointments
('2025-12-07 10:00:00', 'Sarah Meyer', 'Annual checkup', 'ACTIVE', 6, 6, NOW(), NOW()),
-- Completed appointment
('2025-11-30 14:00:00', 'Peter Müller', 'Skin examination', 'COMPLETED', 7, 7, NOW(), NOW());

-- ============================================
-- VERIFICATION (optional - can be removed)
-- ============================================

--SELECT '=== VERIFICATION ===' AS '';
--SELECT 'Hospitals count:' AS '', COUNT(*) FROM hospitals;
--SELECT 'Departments count:' AS '', COUNT(*) FROM departments;
--SELECT 'Medical Staff count:' AS '', COUNT(*) FROM medical_staff;
--SELECT 'Doctors count:' AS '', COUNT(*) FROM doctors;
--SELECT 'Appointments count:' AS '', COUNT(*) FROM appointments;
