# VitalTrack Request Lifecycle Walkthrough

This document explains the journey of a single user action in the VitalTrack system, from the browser request to the database persistence, utilizing Jakarta EE core concepts.

## 1. Sequence Diagram

```mermaid
sequenceDiagram
    participant Browser
    participant ActionDispatcherServlet
    participant ActionParamBinder
    participant EquipmentAction
   equenceDiagram
    participant Browser
    participant ActionDispatcherServlet
    participant ActionParamBinder
    participant EquipmentAction
    participant HospitalEquipmentEJB
    participant EquipmentDao
    participant MySQL

    Browser->>ActionDispatcherServlet: POST /vital/equipment/save
    ActionDispatcherServlet->>ActionParamBinder: Bind Form Data to HospitalEquipment
    ActionParamBinder-->>ActionDispatcherServlet: Return HospitalEquipment Object
    ActionDispatcherServlet->>EquipmentAction: invoke save(HospitalEquipment)
    EquipmentAction->>HospitalEquipmentEJB: save(equipment)
    HospitalEquipmentEJB->>HospitalEquipmentEJB: Validate & Fire Audit Event
    HospitalEquipmentEJB->>EquipmentDao: save(equipment)
    EquipmentDao->>MySQL: EntityManager.merge()
    MySQL-->>EquipmentDao: Record Persisted
    EquipmentDao-->>HospitaldEquipmentEJB: Success
    HospitalEquipmentEJB-->>EquipmentAction: Success
    EquipmentAction-->>ActionDispatcherServlet: Return ActionResponse(List)
    ActionDispatcherServlet->>Browser: Render Dashboard with Updated Table
```
d
---

## 2. Step-by-Step Deep Dive

### Step 1: The Front Controller Entry
The browser sends a `POST` request to `http://localhost:8080/VitalTrack/vital/equipment/save`. 
* **ActionDispatcherServlet**: Because we mapped `@WebServlet("/vital/*")` to this servlet, it catches dthe request.
* **ActionRegistry**: It looks at the `PathInfo` (`/equipment/save`) and identifies the matching Action class and method.
d
### Step 2: Parameter Binding (The "Magic" Step)
Before the Action method is called, raw HTTP parameters must be converted into a Java object.
* **ActionParamBinder**: Uses **Java Reflection** to inspect the method signature of `EquipmentAction.save(HospitalEquipment eq)`.
* It creates a new instance of `HospitalEquipment` and maps form fields (e.g., `name="name"`) to Java fields (e.g., `setName()`).
* **Type Conversion**: It automatically handles data types like `Long`, `int`, and `java.util.Date`.

### Step 3: The Action Controller Logic
The Servlet invokes the `save` method in **`EquipmentAction`**. 
* **Flow Control**: The Action controller's primary responsibility is coordination. It receives the bound object and hands it to the specialized EJB layer.

### Step 4: The EJB & Business Rules
The request enters **`HospitalEquipmentEJB`**, which operates within a **Transactional context**.
* **Validation**: It triggers `validator.process(equipment)` to ensure data integrity.
* **CDI Events**: It fires an `AuditTrail` event. This is a decoupled announcement—the EJB doesn't need to know how the audit is logged, keeping the code clean.

### Step 5: Persistence (JPA)
The EJB calls `equipmentDao.save(equipment)`.
* **GenericDao & EntityManager**: JPA translates the Java object into a SQL `INSERT` statement based on `@Table` and `@Column` annotations. 
* **JTA Transactions**: Because we use JTA (Java Transaction API), if the database operation fails, the entire transaction (including the audit trail) is rolled back.

### Step 6: Response Rendering
Upon success, the Action method returns an `ActionResponse` containing the updated data list.
* **VitalTrackFramework**: This utility uses reflection on the entity class to generate the HTML table dynamically.
* **AppPage**: Wraps the generated content in the premium "Glassmorphism" layout (sidebar, header, etc.) and sends the final HTML back to the browser.

---

## 3. Separation of Concerns
This lifecycle ensures a clean architecture:
1. **Servlet**: Routing and Entry.
2. **Binder**: Data Transformation.
3. **Action**: Flow Coordination.
4. **EJB**: Business Logic & Transactions.
5. **DAO/JPA**: Data Persistence.
