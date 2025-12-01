-- ============================================
-- MINIMAL data.sql - FUNKTIONIERT!
-- ============================================

-- ZUERST Hospitals einfügen
INSERT INTO hospitals (name, city) VALUES
('Charité Berlin', 'Berlin'),
('Universitätsklinikum Hamburg', 'Hamburg'),
('Klinikum München', 'München');

-- JETZT Departments für das ERSTE Hospital (ID wird automatisch 1 sein)
INSERT INTO departments (name, room_numbers, department_head, hospital_id) VALUES
('Cardiology', 15, 'Dr. Schmidt',
    (SELECT id FROM hospitals WHERE name = 'Charité Berlin' LIMIT 1)),
('Neurology', 12, 'Dr. Müller',
    (SELECT id FROM hospitals WHERE name = 'Charité Berlin' LIMIT 1)),
('Pediatrics', 20, 'Dr. Wagner',
    (SELECT id FROM hospitals WHERE name = 'Charité Berlin' LIMIT 1));

-- Rooms für das ERSTE Hospital
INSERT INTO rooms (room_number, type, capacity, available, hospital_id) VALUES
('101', 'Patient Room', 2, true,
    (SELECT id FROM hospitals WHERE name = 'Charité Berlin' LIMIT 1)),
('102', 'Patient Room', 2, true,
    (SELECT id FROM hospitals WHERE name = 'Charité Berlin' LIMIT 1)),
('103', 'ICU', 1, true,
    (SELECT id FROM hospitals WHERE name = 'Charité Berlin' LIMIT 1));