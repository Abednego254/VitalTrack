# VitalTrack: Final Project Documentation

This document summarizes the complete implementation of the VitalTrack project, mapping every component to core Jakarta EE concepts from the training notes.

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
- **doGet() / doPost()**: Handling request methods.
- **HttpServletRequest/Response**: Managing data input and output.

### [CONCEPT: REFLECTION & GENERICS]
Using `<T>`, our base servlet automatically discovers which model it is handling at runtime.

---

## Phase 3: CDI Services (The "Helpers")
We integrated **Contexts and Dependency Injection** to manage business logic.

### [CONCEPT: SCOPES, @Inject & QUALIFIERS](file:///home/abednegokaume/IdeaProjects/maven-projects/cohort12/JEE%20NOTES.txt#L80)
- **@ApplicationScoped**: Used to define singletons like [StandardMaintenanceService.java](file:///home/abednegokaume/IdeaProjects/maven-projects/VitalTrack/src/main/java/app/utility/StandardMaintenanceService.java).
- **@Inject**: Used to automatically provide services to our servlets.
- **Qualifiers**: We created [MaintenanceQualifier.java](file:///home/abednegokaume/IdeaProjects/maven-projects/VitalTrack/src/main/java/app/utility/MaintenanceQualifier.java) to resolve **Dependency Ambiguity**. This allows our [HospitalEquipmentAction](file:///home/abednegokaume/IdeaProjects/maven-projects/VitalTrack/src/main/java/app/action/HospitalEquipmentAction.java) to choose between `URGENT` and `STANDARD` helpers just by their "hat" (annotation).

---

## Phase 4: Security & Monitoring (The "Guards & Alarms")
We implemented middleware to protect and monitor our application.

### [CONCEPT: FILTERS](file:///home/abednegokaume/IdeaProjects/maven-projects/cohort12/JEE%20NOTES.txt#L66)
[HospitalAuthenticationFilter.java](file:///home/abednegokaume/IdeaProjects/maven-projects/VitalTrack/src/main/java/app/filter/HospitalAuthenticationFilter.java) stands at the door, checking for valid user sessions before allowing access to sensitive logs.

### [CONCEPT: LISTENERS](file:///home/abednegokaume/IdeaProjects/maven-projects/cohort12/JEE%20NOTES.txt#L68)
[HospitalStockMonitorListener.java](file:///home/abednegokaume/IdeaProjects/maven-projects/VitalTrack/src/main/java/app/listener/HospitalStockMonitorListener.java) triggers an alarm as soon as the hospital (the server) starts up.

---

## Phase 5: Web Interface (The "Big Screen")
The user interface is built using **JSP**, **JSTL**, and **EL**.

### [CONCEPT: JSP & EXPRESSION LANGUAGE](file:///home/abednegokaume/IdeaProjects/maven-projects/VitalTrack/src/main/webapp/index.jsp)
- **JSTL**: Tag libraries used for logic on the page.
- **EL (${...})**: Used to display dynamic data from our Java beans directly on the screen.

---

## Final Concept Summary
| Concept | Implementation | 🧒 Simple Explanation |
| :--- | :--- | :--- |
| **Java Bean** | `HospitalEquipment.java` | A simple box with labels and handles. |
| **Servlet** | `HospitalBaseAction.java` | A waiter who takes orders and brings food. |
| **CDI Inject** | `@Inject` | Asking the computer to bring us a helper. |
| **Qualifier** | `@MaintenanceQualifier` | A special "Hat" to choose the right helper. |
| **Filter** | `HospitalAuthenticationFilter.java` | A security guard at the door. |
| **Listener** | `HospitalStockMonitorListener.java` | An alarm that rings on opening day. |
| **JSP / EL** | `index.jsp` | The big TV screen showing all the info. |
