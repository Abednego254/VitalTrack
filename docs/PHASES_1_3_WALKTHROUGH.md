# VitalTrack: Phases 1-3 Documentation

This document summarizes the progress made during the first three phases of the VitalTrack project, mapping each implementation step to core Jakarta EE concepts.

## Phase 1: Core Entities (The "Sticky Notes")
We implemented our data models as **Java Beans**.

### [CONCEPT: JAVA BEANS](file:///home/abednegokaume/IdeaProjects/maven-projects/cohort12/JEE%20NOTES.txt#L94)
Our models (e.g., [HospitalEquipment.java](file:///home/abednegokaume/IdeaProjects/maven-projects/VitalTrack/src/main/java/app/model/HospitalEquipment.java)) follow the Bean specification:
- **Encapsulation**: Private fields for data integrity.
- **Accessors**: Getters and Setters (the "handles").
- **Default Constructor**: An empty constructor for container management.
- **Serializable**: Allowing the object state to be saved or transmitted.

---

## Phase 2: Generic Action Framework (The "Smart Workers")
We created a robust system to handle web requests using **Servlets** and **Reflection**.

### [CONCEPT: SERVLETS](file:///home/abednegokaume/IdeaProjects/maven-projects/cohort12/JEE%20NOTES.txt#L31)
[HospitalBaseAction.java](file:///home/abednegokaume/IdeaProjects/maven-projects/VitalTrack/src/main/java/app/action/HospitalBaseAction.java) is our primary worker:
- **doGet()**: Handles viewing/listing data.
- **doPost()**: Handles receiving and saving new data.
- **HttpServletRequest/Response**: Used to read user input and send back messages.

### [CONCEPT: REFLECTION & GENERICS]
Using `<T>` and `ParameterizedType`, our base servlet automatically discovers which model it is handling. This allows us to have one "Super Waiter" for all types of data.

---

## Phase 3: CDI Services (The "Helpers")
We integrated **Contexts and Dependency Injection** to manage business logic.

### [CONCEPT: CDI BEAN DISCOVERY](file:///home/abednegokaume/IdeaProjects/maven-projects/VitalTrack/src/main/webapp/WEB-INF/beans.xml)
By adding `beans.xml`, we enabled the CDI container to automatically manage our objects.

### [CONCEPT: SCOPES](file:///home/abednegokaume/IdeaProjects/maven-projects/cohort12/JEE%20NOTES.txt#L108)
- **@ApplicationScoped**: Used in [HospitalMaintenanceService.java](file:///home/abednegokaume/IdeaProjects/maven-projects/VitalTrack/src/main/java/app/utility/HospitalMaintenanceService.java) to ensure one single instance of the helper exists for the entire application lifecycle.

### [CONCEPT: @Inject](file:///home/abednegokaume/IdeaProjects/maven-projects/cohort12/JEE%20NOTES.txt#L80)
We used the magic word `@Inject` in [HospitalEquipmentAction.java](file:///home/abednegokaume/IdeaProjects/maven-projects/VitalTrack/src/main/java/app/action/HospitalEquipmentAction.java) to automatically link the Helper to the Waiter.
- **No manual creation**: We never use `new HospitalMaintenanceService()`. The container does it for us!

---

## Summary Table of Concepts
| Concept | Implementation File | 🧒 Simple Explanation |
| :--- | :--- | :--- |
| **Java Bean** | `HospitalEquipment.java` | A simple box with labels and handles. |
| **Servlet** | `HospitalBaseAction.java` | A waiter who takes orders and brings food. |
| **Generics** | `HospitalBaseAction<T>` | A waiter who can carry any type of tray. |
| **CDI Inject** | `@Inject` in Action classes | Asking the computer to bring us a helper. |
| **Scope** | `@ApplicationScoped` | A helper who stays at their desk all day. |
| **beans.xml** | `WEB-INF/beans.xml` | The "Green Light" for our CDI helpers. |
