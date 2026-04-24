# VitalTrack: Project Overview & Architecture

## Problem Statement
Healthcare facilities often struggle with manual tracking of medical equipment calibration and supply levels. Overlooked maintenance can lead to equipment failure during emergencies, and expired supplies pose a safety risk.

## Solution
VitalTrack provides a centralized, automated system for tracking the lifecycle of every medical asset in a facility.

## Core Entities (The "Models")
Based on our Generic Framework, we will implement the following entities:

1.  **Equipment**:
    *   `id`, `name`, `serialNumber`, `purchaseDate`, `lastCalibrationDate`, `nextCalibrationDate`, `status` (Active, Maintenance, Retired).
2.  **MedicalSupply**:
    *   `id`, `name`, `category`, `quantity`, `unitOfMeasure`, `expiryDate`, `reorderLevel`.
3.  **Technician**:
    *   `id`, `name`, `specialization`, `contactInfo`, `status` (Available, On-Call).
4.  **MaintenanceLog**:
    *   `id`, `equipmentId`, `technicianId`, `serviceDate`, `actionTaken`, `notes`.

## Architectural Mapping
*   **Servlets**: Handle the registration and listing of the above entities.
*   **CDI**: Inject `MaintenanceService` to calculate `nextCalibrationDate` automatically.
*   **Filters**: `AuthenticationFilter` to protect sensitive maintenance logs.
*   **Listeners**: `StockMonitorListener` to scan `MedicalSupply` quantities on startup.
*   **JSP**: Dynamic tables using JSTL to highlight items that are "Expired" or "Overdue".

## Next Steps
1.  Initialize the Git repository.
2.  Implement the `BaseAction<T>` generic servlet.
3.  Create the initial `Equipment` and `MedicalSupply` Java Beans.
