# VitalTrack – Medical Logistics & Maintenance System

VitalTrack is a Jakarta EE 11 application designed to manage medical supplies and equipment maintenance for healthcare facilities. This project demonstrates core Java enterprise concepts including Servlets, CDI, Filters, Listeners, and a Generic Reflection-based framework.

## Project Scope
- **Equipment Tracking**: Management of medical machines (X-rays, Ventilators, etc.) and their calibration status.
- **Supply Chain**: Inventory management for consumable supplies (Oxygen tanks, bandages, etc.).
- **Maintenance Scheduling**: Automated alerts and tracking for technician service calls.
- **Role-Based Access**: Security layers for Administrators, Nurses, and Technicians.

## Technology Stack
- **Backend**: Java 11+, Jakarta EE 11
- **CDI**: Context Dependency Injection for business logic.
- **Persistence**: MySQL via JDBC.
- **Frontend**: JSP, JSTL, and Expression Language (EL).
- **Server**: WildFly Application Server.
- **Build Tool**: Maven.

## Key Features to Implement
1.  **Generic Action Framework**: A reusable `BaseAction<T>` to handle CRUD operations via reflection.
2.  **Validation Engine**: CDI-managed beans for business rule validation.
3.  **Real-time Alerts**: Listeners to monitor stock levels on application startup.
4.  **Security Filters**: Robust session management and access control.

---
*Developed as a capstone project for Java Enterprise Development training.*
