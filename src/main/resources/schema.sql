-- Create the table for our Machines
CREATE TABLE IF NOT EXISTS HospitalEquipment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    serialNumber VARCHAR(255) UNIQUE NOT NULL,
    purchaseDate DATE,
    lastCalibrationDate DATE,
    nextCalibrationDate DATE,
    status VARCHAR(50)
);

-- Create the table for our Supplies
CREATE TABLE IF NOT EXISTS HospitalMedicalSupply (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255),
    quantity INT DEFAULT 0,
    unitOfMeasure VARCHAR(50),
    expiryDate DATE,
    reorderLevel INT DEFAULT 10
);

-- Create the table for our Technicians
CREATE TABLE IF NOT EXISTS HospitalTechnician (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    specialization VARCHAR(255),
    contactInfo VARCHAR(255),
    status VARCHAR(50)
);

-- Create the table for our Maintenance Records
CREATE TABLE IF NOT EXISTS HospitalMaintenanceLog (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    equipmentId BIGINT,
    technicianId BIGINT,
    serviceDate DATE,
    actionTaken TEXT,
    notes TEXT,
    FOREIGN KEY (equipmentId) REFERENCES HospitalEquipment(id),
    FOREIGN KEY (technicianId) REFERENCES HospitalTechnician(id)
);