-- Crear tabla "doctors"
CREATE TABLE doctors (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         first_name VARCHAR(255),
                         last_name VARCHAR(255),
                         age INT,
                         email VARCHAR(255)
);
-- Crear tabla "patients"
CREATE TABLE patients (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          first_name VARCHAR(255),
                          last_name VARCHAR(255),
                          age INT,
                          email VARCHAR(255)
);
-- Crear tabla "rooms"
CREATE TABLE rooms (
    room_name VARCHAR(255) PRIMARY KEY
);

-- Crear tabla "appointment"
CREATE TABLE appointment (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             patient_id BIGINT,
                             doctor_id BIGINT,
                             room_id VARCHAR(255),
                             starts_at DATETIME,
                             finishes_at DATETIME,
                             FOREIGN KEY (patient_id) REFERENCES patient(id),
                             FOREIGN KEY (doctor_id) REFERENCES doctor(id),
                             FOREIGN KEY (room_id) REFERENCES room(room_name)
);
