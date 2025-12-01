-- Clear existing data (optional)
DELETE FROM rooms;
DELETE FROM departments;
DELETE FROM hospitals;

-- Insert 10 Hospitals
INSERT INTO hospitals (name, city) VALUES
('Charité Berlin', 'Berlin'),
('Universitätsklinikum Hamburg-Eppendorf', 'Hamburg'),
('Klinikum rechts der Isar', 'München'),
('Universitätsklinikum Heidelberg', 'Heidelberg'),
('Klinikum Stuttgart', 'Stuttgart'),
('Universitätsklinikum Frankfurt', 'Frankfurt'),
('Klinikum Hannover', 'Hannover'),
('Universitätsklinikum Köln', 'Köln'),
('Klinikum Düsseldorf', 'Düsseldorf'),
('Universitätsklinikum Leipzig', 'Leipzig');

-- Insert Departments for first hospital
INSERT INTO departments (name, room_numbers, department_head, hospital_id) VALUES
('Cardiology', 15, 'Dr. Schmidt', 1),
('Neurology', 12, 'Dr. Müller', 1),
('Pediatrics', 20, 'Dr. Wagner', 1),
('Oncology', 8, 'Dr. Fischer', 1),
('Orthopedics', 10, 'Dr. Weber', 1),
('Gynecology', 14, 'Dr. Hoffmann', 1),
('Dermatology', 6, 'Dr. Becker', 1),
('Psychiatry', 18, 'Dr. Schulz', 1),
('Radiology', 5, 'Dr. Koch', 1),
('Emergency', 25, 'Dr. Bauer', 1);

-- Insert Rooms for first hospital
INSERT INTO rooms (room_number, type, capacity, available, hospital_id) VALUES
('101', 'Patient Room', 2, true, 1),
('102', 'Patient Room', 2, true, 1),
('103', 'Patient Room', 1, true, 1),
('104', 'ICU', 1, true, 1),
('105', 'ICU', 1, false, 1),
('201', 'Operation Room', 1, true, 1),
('202', 'Operation Room', 1, true, 1),
('301', 'Examination Room', 1, true, 1),
('302', 'Examination Room', 1, true, 1),
('401', 'Isolation Room', 1, true, 1);
