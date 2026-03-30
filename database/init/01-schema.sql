-- Korporis Database Schema
-- This script creates the initial database schema for MySQL

-- Create departments table
CREATE TABLE IF NOT EXISTS departments (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   code VARCHAR(10) NOT NULL UNIQUE,
   name VARCHAR(100) NOT NULL,
   description VARCHAR(500),
   location VARCHAR(200),
   manager_id BIGINT,
   parent_department_id BIGINT,
   active BOOLEAN NOT NULL DEFAULT TRUE,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   INDEX idx_department_code (code),
   INDEX idx_department_parent (parent_department_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create employees table
CREATE TABLE IF NOT EXISTS employees (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   employee_code VARCHAR(20) NOT NULL UNIQUE,
   first_name VARCHAR(50) NOT NULL,
   last_name VARCHAR(50) NOT NULL,
   dpi VARCHAR(13) NOT NULL UNIQUE,
   birth_date DATE,
   gender VARCHAR(20),
   email VARCHAR(100) NOT NULL UNIQUE,
   phone VARCHAR(20),
   address VARCHAR(300),
   hire_date DATE NOT NULL,
   termination_date DATE,
   position VARCHAR(100) NOT NULL,
   salary DECIMAL(12,2) NOT NULL,
   contract_type VARCHAR(20) NOT NULL,
   status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
   department_id BIGINT,
   supervisor_id BIGINT,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   INDEX idx_employee_code (employee_code),
   INDEX idx_employee_dpi (dpi),
   INDEX idx_employee_email (email),
   INDEX idx_employee_department (department_id),
   INDEX idx_employee_supervisor (supervisor_id),
   CONSTRAINT fk_employee_department FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE SET NULL,
   CONSTRAINT fk_employee_supervisor FOREIGN KEY (supervisor_id) REFERENCES employees(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Add foreign key for department manager after employees table is created
ALTER TABLE departments
ADD CONSTRAINT fk_department_manager FOREIGN KEY (manager_id) REFERENCES employees(id) ON DELETE SET NULL;

-- Add foreign key for parent department (self-referencing)
ALTER TABLE departments
ADD CONSTRAINT fk_department_parent FOREIGN KEY (parent_department_id) REFERENCES departments(id) ON DELETE SET NULL;
