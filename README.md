# VitalTrack – Medical Logistics & Maintenance System

VitalTrack is a Jakarta EE 11 application designed to manage medical supplies and equipment maintenance for healthcare facilities. This project demonstrates core Java enterprise concepts including Servlets, CDI, Filters, Listeners, and a Generic Reflection-based framework.

## Project Scope
- **Equipment Tracking**: Management of medical machines (X-rays, Ventilators, etc.) and their calibration equipmentStatus.
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

## Jakarta EE Concepts Implemented

This project serves as a comprehensive demonstration of the following Jakarta EE concepts:

### 1. Web Architecture & Protocols
- **HTTP Protocol**: Understanding Request/Response cycles, Methods (GET, POST, PUT, DELETE).
- **Web Containers**: Running applications within servers like WildFly.

### 2. Servlets (The Smart Workers)
- **Servlet Life Cycle**: Initialization, service, and destruction.
- **HttpServletRequest & HttpServletResponse**: Managing data input and output.
- **RequestDispatcher**: Forwarding and including resources.
- **Session Management**: Using `HttpSession` to track user state.

### 3. Contexts and Dependency Injection (CDI)
- **Inversion of Control (IoC)**: Letting the container manage object creation.
- **Beans**: Managed Java classes with specific lifecycles.
- **Scopes**: 
    - `@RequestScoped`: Lives for one request.
    - `@SessionScoped`: Lives for a user session.
    - `@ApplicationScoped`: Lives for the entire application.
- **Qualifiers**: Custom annotations to resolve dependency ambiguity.
- **Client Proxies**: Thread-safe injection of short-lived beans into long-lived ones.

### 4. Middleware & Monitoring
- **Filters**: Intercepting requests for security (Authentication/Authorization).
- **Listeners**: Responding to application-level events (Startup, Session creation).

### 5. Frontend & Expression Language
- **JSP (JavaServer Pages)**: Dynamic content generation.
- **JSTL (Jakarta Standard Tag Library)**: Logic tags in JSP.
- **Expression Language (EL)**: Simplified access to data in JSP.

---

---
*Developed as a capstone project for Java Enterprise Development training.*
