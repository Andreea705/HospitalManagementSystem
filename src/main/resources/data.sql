-- Disable foreign key checks
SET FOREIGN_KEY_CHECKS = 0;

-- Clear ALL old data
TRUNCATE TABLE appointments;
TRUNCATE TABLE doctors;
TRUNCATE TABLE medical_staff;
TRUNCATE TABLE rooms;
TRUNCATE TABLE departments;
TRUNCATE TABLE hospitals;

-- Reset auto-increment
ALTER TABLE hospitals AUTO_INCREMENT = 1;
ALTER TABLE departments AUTO_INCREMENT = 1;
ALTER TABLE rooms AUTO_INCREMENT = 1;
ALTER TABLE medical_staff AUTO_INCREMENT = 1;
ALTER TABLE doctors AUTO_INCREMENT = 1;
ALTER TABLE appointments AUTO_INCREMENT = 1;

-- Insert 10 HOSPITALS
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

-- Insert DEPARTMENTS
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

-- Insert ROOMS
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

-- Insert PATIENTS (NEW TABLE)
INSERT INTO patients (first_name, last_name, date_of_birth, gender, phone, email, address, insurance_number, emergency_contact) VALUES
('Max', 'Mustermann', '1980-05-15', 'MALE', '+49 30 1111111', 'max.mustermann@email.com', 'Berliner Str. 1, Berlin', 'INS123456', 'Erika Mustermann +49 30 1111112'),
('Erika', 'Musterfrau', '1985-08-20', 'FEMALE', '+49 30 2222222', 'erika.musterfrau@email.com', 'Hamburger Str. 2, Hamburg', 'INS234567', 'Max Mustermann +49 30 1111111'),
('Klaus', 'Schmidt', '1975-03-10', 'MALE', '+49 40 3333333', 'klaus.schmidt@email.com', 'Münchner Str. 3, München', 'INS345678', 'Maria Schmidt +49 40 3333334'),
('Sabine', 'Groß', '1990-11-25', 'FEMALE', '+49 40 4444444', 'sabine.gross@email.com', 'Heidelberger Str. 4, Heidelberg', 'INS456789', 'Thomas Groß +49 40 4444445'),
('Lena', 'Klein', '2020-02-14', 'FEMALE', '+49 89 5555555', 'parents@email.com', 'Stuttgarter Str. 5, Stuttgart', 'INS567890', 'Parents +49 89 5555556'),
('Max', 'Bauer', '2017-07-30', 'MALE', '+49 69 6666666', 'parents@email.com', 'Frankfurter Str. 6, Frankfurt', 'INS678901', 'Parents +49 69 6666667'),
('Martin', 'Vogel', '1965-09-12', 'MALE', '+49 511 7777777', 'martin.vogel@email.com', 'Hannoveraner Str. 7, Hannover', 'INS789012', 'Anna Vogel +49 511 7777778'),
('Frank', 'Zimmermann', '1972-12-05', 'MALE', '+49 221 8888888', 'frank.zimmermann@email.com', 'Kölner Str. 8, Köln', 'INS890123', 'Julia Zimmermann +49 221 8888889'),
('Sarah', 'Meyer', '1988-04-18', 'FEMALE', '+49 211 9999999', 'sarah.meyer@email.com', 'Düsseldorfer Str. 9, Düsseldorf', 'INS901234', 'Markus Meyer +49 211 9999990'),
('Peter', 'Müller', '1955-06-22', 'MALE', '+49 341 1010101', 'peter.mueller@email.com', 'Leipziger Str. 10, Leipzig', 'INS012345', 'Gabi Müller +49 341 1010102');

-- MEDICAL_STAFF
INSERT INTO medical_staff (medical_staff_name, role, department_id) VALUES
('Dr. Anna Schmidt', 'doctor', 1),
('Dr. Thomas Müller', 'doctor', 2),
('Dr. Sarah Wagner', 'doctor', 3),
('Dr. Michael Fischer', 'doctor', 4),
('Dr. Julia Weber', 'doctor', 5),
('Dr. Robert Hoffmann', 'doctor', 6),
('Dr. Lisa Becker', 'doctor', 7),
('Dr. David Schulz', 'doctor', 8),
('Dr. Maria Koch', 'doctor', 9),
('Dr. Stefan Bauer', 'doctor', 10);

-- DOCTORS
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

INSERT INTO appointments (appointment_date, patient_name, patient_id, description, status, department_id, doctor_id, created_at, updated_at) VALUES
('2025-12-03 09:00:00', 'Max Mustermann', 1, 'Regular heart checkup', 'ACTIVE', 1, 1, NOW(), NOW()),
('2025-12-03 10:30:00', 'Erika Musterfrau', 2, 'ECG examination', 'ACTIVE', 1, 1, NOW(), NOW()),
('2025-12-04 09:00:00', 'Klaus Schmidt', 3, 'Migraine consultation', 'ACTIVE', 2, 2, NOW(), NOW()),
('2025-12-04 11:00:00', 'Sabine Groß', 4, 'Neurological exam', 'ACTIVE', 2, 2, NOW(), NOW()),
('2025-12-05 09:30:00', 'Lena Klein', 5, 'Vaccination', 'ACTIVE', 1, 1, NOW(), NOW()),
('2025-12-05 13:00:00', 'Max Bauer', 6, 'Flu treatment', 'ACTIVE', 1, 1, NOW(), NOW()),
('2025-12-06 08:00:00', 'Martin Vogel', 7, 'Pre-surgery consultation', 'ACTIVE', 1, 1, NOW(), NOW()),
('2025-12-06 10:30:00', 'Frank Zimmermann', 8, 'Knee pain treatment', 'ACTIVE', 1, 1, NOW(), NOW()),
('2025-12-07 09:00:00', 'Sarah Meyer', 9, 'Annual checkup', 'ACTIVE', 1, 1, NOW(), NOW()),
('2025-11-30 14:00:00', 'Peter Müller', 10, 'Dermatology consultation', 'COMPLETED', 1, 1, NOW(), NOW());

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;