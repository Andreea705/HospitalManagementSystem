-- data.sql (CLEAN VERSION - NO DDL COMMANDS)
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

-- Insert DEPARTMENTS (10 entities, distributed across hospitals)
INSERT INTO departments (name, room_numbers, department_head, hospital_id) VALUES
('Cardiology', 15, 'Dr. Schmidt', 1),
('Neurology', 12, 'Dr. Müller', 1),
('Pediatrics', 20, 'Dr. Wagner', 2),
('Surgery', 18, 'Dr. Fischer', 3),
('Orthopedics', 10, 'Dr. Weber', 4),
('Gynecology', 14, 'Dr. Hoffmann', 5),
('Dermatology', 6, 'Dr. Becker', 6),
('Psychiatry', 18, 'Dr. Schulz', 7),
('Radiology', 5, 'Dr. Koch', 8),
('Emergency', 25, 'Dr. Bauer', 9),
('Internal Medicine', 12, 'Dr. Schneider', 10);

-- Insert ROOMS (10 entities, distributed across hospitals)
INSERT INTO rooms (room_number, type, capacity, available, hospital_id) VALUES
('101', 'Patient Room', 2, true, 1),
('102', 'Patient Room', 2, true, 1),
('103', 'ICU', 1, true, 1),
('104', 'ICU', 1, false, 2),
('201', 'Operation Room', 1, true, 3),
('202', 'Operation Room', 1, true, 4),
('301', 'Examination Room', 1, true, 5),
('302', 'Examination Room', 1, true, 6),
('401', 'Isolation Room', 1, true, 7),
('501', 'Delivery Room', 1, true, 8),
('601', 'Recovery Room', 2, true, 9),
('701', 'ICU', 1, true, 10);

-- Insert PATIENTS (10 entities) - FIXED with unique emails
INSERT INTO patients (patient_id, name, email, phone_number, date_of_birth, emergency_contact, registration_date) VALUES
('PAT001', 'Max Mustermann', 'max.mustermann@email.com', '+49 30 1111111', '1980-05-15', 'Erika Mustermann +49 30 1111112', NOW()),
('PAT002', 'Erika Musterfrau', 'erika.musterfrau@email.com', '+49 30 2222222', '1985-08-20', 'Max Mustermann +49 30 1111111', NOW()),
('PAT003', 'Klaus Schmidt', 'klaus.schmidt@email.com', '+49 40 3333333', '1975-03-10', 'Maria Schmidt +49 40 3333334', NOW()),
('PAT004', 'Sabine Groß', 'sabine.gross@email.com', '+49 40 4444444', '1990-11-25', 'Thomas Groß +49 40 4444445', NOW()),
('PAT005', 'Lena Klein', 'lena.klein@email.com', '+49 89 5555555', '2020-02-14', 'Parents +49 89 5555556', NOW()),        -- CHANGED
('PAT006', 'Max Bauer', 'max.bauer@email.com', '+49 69 6666666', '2017-07-30', 'Parents +49 69 6666667', NOW()),          -- CHANGED
('PAT007', 'Martin Vogel', 'martin.vogel@email.com', '+49 511 7777777', '1965-09-12', 'Anna Vogel +49 511 7777778', NOW()),
('PAT008', 'Frank Zimmermann', 'frank.zimmermann@email.com', '+49 221 8888888', '1972-12-05', 'Julia Zimmermann +49 221 8888889', NOW()),
('PAT009', 'Sarah Meyer', 'sarah.meyer@email.com', '+49 211 9999999', '1988-04-18', 'Markus Meyer +49 211 9999990', NOW()),
('PAT010', 'Peter Müller', 'peter.mueller@email.com', '+49 341 1010101', '1955-06-22', 'Gabi Müller +49 341 1010102', NOW());

-- MEDICAL_STAFF (10 entities) - FIXED with medical_staff_id
INSERT INTO medical_staff (medical_staff_id, medical_staff_name, role, department_id) VALUES
('MED001', 'Dr. Anna Schmidt', 'doctor', 1),
('MED002', 'Dr. Thomas Müller', 'doctor', 2),
('MED003', 'Dr. Sarah Wagner', 'doctor', 3),
('MED004', 'Dr. Michael Fischer', 'doctor', 4),
('MED005', 'Dr. Julia Weber', 'doctor', 5),
('MED006', 'Dr. Robert Hoffmann', 'doctor', 6),
('MED007', 'Dr. Lisa Becker', 'doctor', 7),
('MED008', 'Dr. David Schulz', 'doctor', 8),
('MED009', 'Dr. Maria Koch', 'doctor', 9),
('MED010', 'Dr. Stefan Bauer', 'doctor', 10),
-- NURSES (10)
('NUR001', 'Maria Schmidt', 'nurse', 1),
('NUR002', 'Anna Müller', 'nurse', 1),
('NUR003', 'Sarah Wagner', 'nurse', 2),
('NUR004', 'Julia Fischer', 'nurse', 3),
('NUR005', 'Lisa Weber', 'nurse', 4),
('NUR006', 'Erika Hoffmann', 'nurse', 5),
('NUR007', 'Petra Becker', 'nurse', 6),
('NUR008', 'Claudia Schulz', 'nurse', 7),
('NUR009', 'Monika Koch', 'nurse', 8),
('NUR010', 'Barbara Bauer', 'nurse', 9);

-- NURSES (10 entities)
INSERT INTO nurses (medical_staff_id, qualification_level, shift, on_duty) VALUES
(11, 'REGISTERED_NURSE', 'Day', true),
(12, 'PRACTICAL_NURSE', 'Night', true),
(13, 'NURSE_PRACTITIONER', 'Evening', true),
(14, 'REGISTERED_NURSE', 'Day', false),
(15, 'NURSE_PRACTITIONER', 'Night', true),
(16, 'REGISTERED_NURSE', 'Evening', true),
(17, 'PRACTICAL_NURSE', 'Day', true),
(18, 'NURSE_PRACTITIONER', 'Night', false),
(19, 'REGISTERED_NURSE', 'Evening', true),
(20, 'PRACTICAL_NURSE', 'Day', true);

-- DOCTORS (10 entities)
INSERT INTO doctors (medical_staff_id, specialization, email, phone, license_number) VALUES
(1, 'Cardiologist', 'anna.schmidt@hospital1.com', '+49 30 1234567', 'L-CARD-001'),
(2, 'Neurologist', 'thomas.mueller@hospital1.com', '+49 30 1234568', 'L-NEUR-002'),
(3, 'Pediatrician', 'sarah.wagner@hospital2.com', '+49 40 1234569', 'L-PED-003'),
(4, 'Surgeon', 'michael.fischer@hospital3.com', '+49 89 1234570', 'L-SURG-004'),
(5, 'Orthopedist', 'julia.weber@hospital4.com', '+49 6221 1234571', 'L-ORTH-005'),
(6, 'Gynecologist', 'robert.hoffmann@hospital5.com', '+49 711 1234572', 'L-GYN-006'),
(7, 'Dermatologist', 'lisa.becker@hospital6.com', '+49 69 1234573', 'L-DERM-007'),
(8, 'Psychiatrist', 'david.schulz@hospital7.com', '+49 511 1234574', 'L-PSYCH-008'),
(9, 'Radiologist', 'maria.koch@hospital8.com', '+49 221 1234575', 'L-RAD-009'),
(10, 'Emergency Medicine', 'stefan.bauer@hospital9.com', '+49 211 1234576', 'L-EMER-010');

-- APPOINTMENTS (10 entities)
INSERT INTO appointments (appointment_date, patient_name, patient_id, description, status, department_id, doctor_id, created_at, updated_at) VALUES
('2025-12-03 09:00:00', 'Max Mustermann', 1, 'Regular heart checkup', 'ACTIVE', 1, 1, NOW(), NOW()),
('2025-12-03 10:30:00', 'Erika Musterfrau', 2, 'Neurology consultation', 'ACTIVE', 2, 2, NOW(), NOW()),
('2025-12-04 09:00:00', 'Klaus Schmidt', 3, 'Pediatric checkup', 'ACTIVE', 3, 3, NOW(), NOW()),
('2025-12-04 11:00:00', 'Sabine Groß', 4, 'Surgery consultation', 'ACTIVE', 4, 4, NOW(), NOW()),
('2025-12-05 09:30:00', 'Lena Klein', 5, 'Orthopedic examination', 'ACTIVE', 5, 5, NOW(), NOW()),
('2025-12-05 13:00:00', 'Max Bauer', 6, 'Gynecology appointment', 'ACTIVE', 6, 6, NOW(), NOW()),
('2025-12-06 08:00:00', 'Martin Vogel', 7, 'Dermatology consultation', 'ACTIVE', 7, 7, NOW(), NOW()),
('2025-12-06 10:30:00', 'Frank Zimmermann', 8, 'Psychiatry session', 'ACTIVE', 8, 8, NOW(), NOW()),
('2025-12-07 09:00:00', 'Sarah Meyer', 9, 'Radiology scan', 'ACTIVE', 9, 9, NOW(), NOW()),
('2025-11-30 14:00:00', 'Peter Müller', 10, 'Emergency follow-up', 'COMPLETED', 10, 10, NOW(), NOW());

-- MEDICAL_STAFF_APPOINTMENTS
INSERT INTO medical_staff_appointments (medical_staff_id, appointment_id) VALUES
('MED001', '1'),                                                                              ('MED002', '2'),
('MED003', '3'),
('MED004', '4'),
('MED005', '5'),
('MED006', '6'),
('MED007', '7'),
('MED008', '8'),
('MED009', '9'),
('MED010', '10');