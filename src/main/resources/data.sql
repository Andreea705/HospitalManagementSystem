-- ============================================
-- 100% WORKING data.sql
-- ============================================

-- 1. FIRST: Disable foreign key checks
SET FOREIGN_KEY_CHECKS = 0;

-- 2. Clear ALL old data
DELETE FROM rooms;
DELETE FROM departments;
DELETE FROM hospitals;
DELETE FROM medical_staff;
DELETE FROM doctors;
DELETE FROM appointments;

-- 3. Reset auto-increment
ALTER TABLE hospitals AUTO_INCREMENT = 1;
ALTER TABLE departments AUTO_INCREMENT = 1;
ALTER TABLE rooms AUTO_INCREMENT = 1;
ALTER TABLE medical_staff AUTO_INCREMENT = 1;
ALTER TABLE doctors AUTO_INCREMENT = 1;
ALTER TABLE appointments AUTO_INCREMENT = 1;

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

-- MEDICAL_STAFF (parent)
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

INSERT INTO doctors (medical_staff_id, specialization, email, phone, license_number) VALUES
                                                                                         (1, 'Cardiologist', 'anna.schmidt@hospital.com', '+49 30 1234567', 'L-CARD-001'),
                                                                                         (2, 'Neurologist', 'thomas.mueller@hospital.com', '+49 30 1234568', 'L-NEUR-002'),
                                                                                         (3, 'Pediatrician', 'sarah.wagner@hospital.com', '+49 30 1234569', 'L-PED-003'),
                                                                                         (4, 'Surgeon', 'michael.fischer@hospital.com', '+49 30 1234570', 'L-SURG-004'),
                                                                                         (5, 'Orthopedist', 'julia.weber@hospital.com', '+49 30 1234571', 'L-ORTH-005'),
                                                                                         (6, 'Gynecologist', 'robert.hoffmann@hospital.com', '+49 30 1234572', 'L-GYN-006'),
                                                                                         (7, 'Dermatologist', 'lisa.becker@hospital.com', '+49 30 1234573', 'L-DERM-007'),
                                                                                         (8, 'Psychiatrist', 'david.schulz@hospital.com', '+49 30 1234574', 'L-PSYCH-008'),
                                                                                         (9, 'Radiologist', 'maria.koch@hospital.com', '+49 30 1234575', 'L-RAD-009'),
                                                                                         (10, 'Emergency Medicine', 'stefan.bauer@hospital.com', '+49 30 1234576', 'L-EMER-010');

INSERT INTO appointments (appointment_date, patient_name, description, status, department_id, doctor_id, created_at, updated_at) VALUES
-- Cardiology appointments with Dr. Anna Schmidt
('2025-12-03 09:00:00', 'Max Mustermann', 'Regular heart checkup', 'ACTIVE', 1, 1, NOW(), NOW()),
('2025-12-03 10:30:00', 'Erika Musterfrau', 'ECG examination', 'ACTIVE', 1, 1, NOW(), NOW()),

-- Neurology appointments with Dr. Thomas Müller
('2025-12-04 09:00:00', 'Klaus Schmidt', 'Migraine consultation', 'ACTIVE', 2, 2, NOW(), NOW()),
('2025-12-04 11:00:00', 'Sabine Groß', 'Neurological exam', 'ACTIVE', 2, 2, NOW(), NOW()),

-- Pediatrics appointments with Dr. Sarah Wagner
('2025-12-05 09:30:00', 'Lena Klein (5 years)', 'Vaccination', 'ACTIVE', 3, 3, NOW(), NOW()),
('2025-12-05 13:00:00', 'Max Bauer (8 years)', 'Flu treatment', 'ACTIVE', 3, 3, NOW(), NOW()),

-- Surgery appointments with Dr. Michael Fischer
('2025-12-06 08:00:00', 'Martin Vogel', 'Pre-surgery consultation', 'ACTIVE', 4, 4, NOW(), NOW()),

-- Orthopedics appointments with Dr. Julia Weber
('2025-12-06 10:30:00', 'Frank Zimmermann', 'Knee pain treatment', 'ACTIVE', 5, 5, NOW(), NOW()),

-- Gynecology appointments with Dr. Robert Hoffmann
('2025-12-07 09:00:00', 'Sarah Meyer', 'Annual checkup', 'ACTIVE', 6, 6, NOW(), NOW()),

-- Completed appointment
('2025-11-30 14:00:00', 'Peter Müller', 'Dermatology consultation', 'COMPLETED', 7, 7, NOW(), NOW());

-- 7. Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- 8. Verify data
SELECT '=== DATA VERIFICATION ===' AS '';
SELECT 'Hospitals: ' AS '', COUNT(*) FROM hospitals;
SELECT 'Departments: ' AS '', COUNT(*) FROM departments;
SELECT 'Rooms: ' AS '', COUNT(*) FROM rooms;
SELECT 'Medical Staff' AS '', COUNT(*) FROM medical_staff;
SELECT 'Doctors' AS '', COUNT(*) FROM doctors;
SELECT 'Appointments' AS '', COUNT(*) FROM appointments;