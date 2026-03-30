-- Korporis Sample Data
-- This script inserts sample data for testing

-- Insert Departments (without managers first)
INSERT INTO departments (code, name, description, location, active) VALUES
('IT', 'Information Technology', 'Manages all IT infrastructure and software development', 'Building A, Floor 3', TRUE),
('HR', 'Human Resources', 'Handles recruitment, employee relations, and benefits', 'Building A, Floor 1', TRUE),
('FIN', 'Finance', 'Manages company finances, accounting, and budgeting', 'Building B, Floor 2', TRUE),
('SALES', 'Sales', 'Handles customer acquisition and sales operations', 'Building C, Floor 1', TRUE),
('OPS', 'Operations', 'Manages day-to-day business operations', 'Building B, Floor 1', TRUE);

-- Insert Employees - IT Department
INSERT INTO employees (employee_code, first_name, last_name, dpi, birth_date, gender, email, phone, address, hire_date, position, salary, contract_type, status, department_id, supervisor_id) VALUES
('EMP-0001', 'Carlos', 'Martinez', '1234567890123', '1980-05-15', 'MALE', 'carlos.martinez@korporis.com', '5555-1234', 'Zone 10, Guatemala City', '2015-01-10', 'Chief Technology Officer', 25000.00, 'FULL_TIME', 'ACTIVE', 1, NULL),
('EMP-0002', 'Maria', 'Garcia', '2345678901234', '1985-08-22', 'FEMALE', 'maria.garcia@korporis.com', '5555-2345', 'Zone 14, Guatemala City', '2017-03-15', 'Development Lead', 18000.00, 'FULL_TIME', 'ACTIVE', 1, 1),
('EMP-0003', 'Juan', 'Lopez', '3456789012345', '1990-02-10', 'MALE', 'juan.lopez@korporis.com', '5555-3456', 'Zone 11, Guatemala City', '2019-06-01', 'Senior Developer', 12000.00, 'FULL_TIME', 'ACTIVE', 1, 2),
('EMP-0004', 'Ana', 'Rodriguez', '4567890123456', '1995-11-30', 'FEMALE', 'ana.rodriguez@korporis.com', '5555-4567', 'Zone 7, Guatemala City', '2022-01-15', 'Junior Developer', 7000.00, 'FULL_TIME', 'ACTIVE', 1, 2);

-- Insert Employees - HR Department
INSERT INTO employees (employee_code, first_name, last_name, dpi, birth_date, gender, email, phone, address, hire_date, position, salary, contract_type, status, department_id, supervisor_id) VALUES
('EMP-0005', 'Patricia', 'Hernandez', '5678901234567', '1978-07-08', 'FEMALE', 'patricia.hernandez@korporis.com', '5555-5678', 'Zone 15, Guatemala City', '2014-04-20', 'HR Director', 20000.00, 'FULL_TIME', 'ACTIVE', 2, NULL),
('EMP-0006', 'Roberto', 'Perez', '6789012345678', '1992-03-25', 'MALE', 'roberto.perez@korporis.com', '5555-6789', 'Zone 12, Guatemala City', '2020-08-10', 'Recruiter', 8000.00, 'FULL_TIME', 'ACTIVE', 2, 5);

-- Insert Employees - Finance Department
INSERT INTO employees (employee_code, first_name, last_name, dpi, birth_date, gender, email, phone, address, hire_date, position, salary, contract_type, status, department_id, supervisor_id) VALUES
('EMP-0007', 'Fernando', 'Castillo', '7890123456789', '1975-12-03', 'MALE', 'fernando.castillo@korporis.com', '5555-7890', 'Zone 10, Guatemala City', '2013-02-01', 'Chief Financial Officer', 28000.00, 'FULL_TIME', 'ACTIVE', 3, NULL),
('EMP-0008', 'Lucia', 'Morales', '8901234567890', '1988-09-17', 'FEMALE', 'lucia.morales@korporis.com', '5555-8901', 'Zone 9, Guatemala City', '2018-05-20', 'Senior Accountant', 10000.00, 'FULL_TIME', 'ACTIVE', 3, 7);

-- Insert Employees - Sales Department
INSERT INTO employees (employee_code, first_name, last_name, dpi, birth_date, gender, email, phone, address, hire_date, position, salary, contract_type, status, department_id, supervisor_id) VALUES
('EMP-0009', 'Miguel', 'Santos', '9012345678901', '1982-04-12', 'MALE', 'miguel.santos@korporis.com', '5555-9012', 'Zone 13, Guatemala City', '2016-07-05', 'Sales Director', 22000.00, 'FULL_TIME', 'ACTIVE', 4, NULL),
('EMP-0010', 'Carmen', 'Flores', '0123456789012', '1993-06-28', 'FEMALE', 'carmen.flores@korporis.com', '5555-0123', 'Zone 8, Guatemala City', '2021-03-01', 'Sales Representative', 6500.00, 'FULL_TIME', 'ACTIVE', 4, 9);

-- Update departments with their managers
UPDATE departments SET manager_id = 1 WHERE code = 'IT';
UPDATE departments SET manager_id = 5 WHERE code = 'HR';
UPDATE departments SET manager_id = 7 WHERE code = 'FIN';
UPDATE departments SET manager_id = 9 WHERE code = 'SALES';

-- Insert IT Sub-Departments (parent = IT department, id = 1)
INSERT INTO departments (code, name, description, location, parent_department_id, active) VALUES
('IT-DEV',  'Development',        'Software development and engineering team',          'Building A, Floor 3', 1, TRUE),
('IT-SUP',  'Technical Support',  'Handles internal and external technical support',    'Building A, Floor 3', 1, TRUE),
('IT-DBA',  'Database',           'Database administration and data management',        'Building A, Floor 3', 1, TRUE),
('IT-QA',   'Quality Assurance',  'Software testing and quality control',               'Building A, Floor 3', 1, TRUE),
('IT-INFRA','Infrastructure',     'Network, servers, and cloud infrastructure',         'Building A, Floor 3', 1, TRUE);

-- Update IT sub-department managers (reuse existing IT employees)
UPDATE departments SET manager_id = 2 WHERE code = 'IT-DEV';   -- Maria Garcia (Development Lead)
UPDATE departments SET manager_id = 3 WHERE code = 'IT-QA';    -- Juan Lopez (Senior Developer)
