# VitalTrack REST API Implementation Plan

This document outlines the architecture, comparative analysis, and step-by-step strategy for integrating standard JAX-RS (Jakarta RESTful Web Services) endpoints into the **VitalTrack Medical Logistics** platform. Our plan is strictly aligned with the architectural patterns established by the trainer in the `mikebavon/cohort12` reference repository.

---

## 1. Analysis of Trainer's Reference Commits

We analyzed the two commits from `mikebavon/cohort12`:
*   **`e2eeb5ffc37454da23f878aa58d65c64b450552f` (rest implementation - jax -rs)**
*   **`30c1ca89e37edec4e3465b818aeb8ef293651f5f` (refactor login filter)**

### Key Architectural Concepts Introduced:

1.  **JAX-RS Activation**:
    *   Created `RestActivation.java` extending `jakarta.ws.rs.core.Application` with the `@ApplicationPath("/api")` annotation. This bootstraps the REST engine in WildFly, mapping all JAX-RS endpoints under the `/api` prefix.
2.  **Standardized Envelopes**:
    *   An enum `SuccessError` and class `ResponseStatus` define standard JSON payloads returned upon completion of write actions (like `save` operations), ensuring client applications receive consistent status envelopes.
3.  **Base Class abstraction (`GenericApi<T>`)**:
    *   Defined a generic abstract structure to enable future controller generalizations.
4.  **Endpoint Operations (`SchoolRestApi.java`)**:
    *   Showcases standard endpoint conventions:
        *   `@Path("/school")` at class-level.
        *   `@EJB` for EJB injection.
        *   **POST** `/save` consuming and producing `application/json`. Returns a `jakarta.ws.rs.core.Response` wrapping the status envelope.
        *   **GET** `/find/{id}` using `@PathParam` to retrieve a single item.
        *   **GET** `/query` using `@QueryParam` as an alternate search.
        *   **GET** `/list` to return all records.
5.  **JSON Serialization & Circle Prevention**:
    *   Bidirectional relationships (e.g., `School` containing list of `Campus`) lead to circular loops or heavy payloads during Jackson serialization. The trainer resolved this by decorating lazy getters with `@JsonIgnore`.
6.  **Filter Exclusion Refactoring**:
    *   To prevent the `LoginFilter` (which redirects non-authenticated users to `/login`) from intercepting REST traffic under `/api/*`, the trainer first whitelisted `/api/*` and then cleanly refactored the filter to only intercept `/app/*` requests.

---

## 2. Comparative Analysis: VitalTrack vs. Trainer Reference

Our inspection of the local `VitalTrack` workspace revealed the following:

| Aspect | Trainer Repository (`cohort12`) | VitalTrack Workspace | Required Action |
| :--- | :--- | :--- | :--- |
| **Authentication Filter** | Refactored to map to `/app/*` (allowing `/api/*` and others to pass through naturally). | Maps to `/*` and intercepts everything, redirecting unauthenticated requests to `/login`. | Modify `HospitalAuthenticationFilter.java` to explicitly bypass filter checks for any request matching `path.startsWith("/api")`. |
| **EJB Capabilites** | Bean definitions have standard lookup functions such as `findById(Long)`. | `HospitalEquipmentEJB` and `HospitalMaintenanceLogEJB` lack `findById` methods. | Add a standard `findById(Long)` delegate method to both EJBs, calling their underlying generic DAO. |
| **Security / Ignored JSON Fields** | `@JsonIgnore` placed on bidirectional collections to stop recursion. | Model objects include private sensitive fields like `password` (`HospitalNurse` and `HospitalTechnician`). | Add `@JsonIgnore` to `getPassword()` getters to prevent raw passwords from leaking in JSON responses. |
| **Dependencies** | Uses Jakarta EE standard classes provided by WildFly (`jakarta.ws.rs-api`). | Already has `jakarta.jakartaee-api` scope `provided` and Jackson dependencies. | No new dependencies are needed; we are fully equipped. |

---

## 3. Step-by-Step Implementation Plan

### Step 1: Refactor `HospitalAuthenticationFilter` to Whitelist API Endpoints
Instead of changing the servlet mappings from `/vital/*` to `/app/*` (which would require extensive edits on all JSP views and JavaScript redirects), we will simply add an API exclusion check directly inside `HospitalAuthenticationFilter.java`.

```java
boolean isApiRequest = path.startsWith("/api");
if (isLoginRequest || isLogoutRequest || isStaticResource || isApiRequest) {
    filterChain.doFilter(request, response);
    return;
}
```

---

### Step 2: Establish the REST Infrastructure
Create the package `app.rest` and introduce the boilerplate elements modeled by the trainer:

1.  **`RestActivation.java`**:
    ```java
    package app.rest;
    
    import jakarta.ws.rs.ApplicationPath;
    import jakarta.ws.rs.core.Application;
    
    @ApplicationPath("/api")
    public class RestActivation extends Application {
    }
    ```
2.  **`SuccessError.java`**:
    ```java
    package app.rest;
    
    public enum SuccessError {
        SUCCESS,
        ERROR
    }
    ```
3.  **`ResponseStatus.java`**:
    ```java
    package app.rest;
    
    import java.io.Serializable;
    
    public class ResponseStatus implements Serializable {
        private SuccessError status = SuccessError.SUCCESS;
        private String message = "OK";
        
        public ResponseStatus() {}
        public ResponseStatus(SuccessError status, String message) {
            this.status = status;
            this.message = message;
        }
        // Getters and Setters...
    }
    ```
4.  **`GenericApi.java`**:
    ```java
    package app.rest;
    
    public abstract class GenericApi<T> {
    }
    ```

---

### Step 3: Secure Entities Against Credential Leakage
Ensure security by decorating the sensitive fields in our models with `@JsonIgnore` (imported from `com.fasterxml.jackson.annotation.JsonIgnore`):

*   **`HospitalNurse.java`**:
    ```java
    @JsonIgnore
    public String getPassword() {
        return password;
    }
    ```
*   **`HospitalTechnician.java`**:
    ```java
    @JsonIgnore
    public String getPassword() {
        return password;
    }
    ```

---

### Step 4: Expand EJB Interfaces
Add lookup support in EJBs where they are missing:

*   **`HospitalEquipmentEJB.java`**:
    ```java
    public HospitalEquipment findById(Long id) throws Exception {
        return equipmentDao.findById(id);
    }
    ```
*   **`HospitalMaintenanceLogEJB.java`**:
    ```java
    public HospitalMaintenanceLog findById(Long id) throws Exception {
        return logDao.findById(id);
    }
    ```

---

### Step 5: Implement JAX-RS REST Controllers
Create dedicated REST API controllers mapping our entity endpoints.

#### 1. Equipment REST API (`HospitalEquipmentRestApi.java`)

```java
package app.rest;

import app.ejb.EquipmentEJB;
import app.ejb.HospitalEquipmentEJB;
import app.model.Equipment;
import app.model.HospitalEquipment;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/equipment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HospitalEquipmentRestApi extends GenericApi<Equipment> {

    @EJB
    private EquipmentEJB equipmentEJB;

    @Path("/save")
    @POST
    public Response save(Equipment equipment) {
        try {
            equipmentEJB.save(equipment);
            return Response.ok(new ResponseStatus(SuccessError.SUCCESS, "Equipment saved successfully")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ResponseStatus(SuccessError.ERROR, e.getMessage())).build();
        }
    }

    @Path("/find/{id}")
    @GET
    public Response find(@PathParam("id") Long id) {
        try {
            Equipment equipment = equipmentEJB.findById(id);
            if (equipment == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ResponseStatus(SuccessError.ERROR, "Equipment not found")).build();
            }
            return Response.ok(equipment).build();
        } catch (Exception e) {
            return Response.serverError().entity(new ResponseStatus(SuccessError.ERROR, e.getMessage())).build();
        }
    }

    @Path("/list")
    @GET
    public Response list() {
        try {
            List<Equipment> list = equipmentEJB.findAll();
            return Response.ok(list).build();
        } catch (Exception e) {
            return Response.serverError().entity(new ResponseStatus(SuccessError.ERROR, e.getMessage())).build();
        }
    }
}
```

#### 2. Medical Supply REST API (`HospitalMedicalSupplyRestApi.java`)

```java
package app.rest;

import app.ejb.MedicalSupplyEJB;
import app.model.HospitalMedicalSupply;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/supply")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HospitalMedicalSupplyRestApi extends GenericApi<HospitalMedicalSupply> {

    @EJB
    private MedicalSupplyEJB supplyEJB;

    @Path("/save")
    @POST
    public Response save(HospitalMedicalSupply supply) {
        try {
            supplyEJB.save(supply);
            return Response.ok(new ResponseStatus(SuccessError.SUCCESS, "Supply saved successfully")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ResponseStatus(SuccessError.ERROR, e.getMessage())).build();
        }
    }

    @Path("/find/{id}")
    @GET
    public Response find(@PathParam("id") Long id) {
        try {
            HospitalMedicalSupply supply = supplyEJB.findById(id);
            if (supply == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ResponseStatus(SuccessError.ERROR, "Supply not found")).build();
            }
            return Response.ok(supply).build();
        } catch (Exception e) {
            return Response.serverError().entity(new ResponseStatus(SuccessError.ERROR, e.getMessage())).build();
        }
    }

    @Path("/list")
    @GET
    public Response list() {
        try {
            List<HospitalMedicalSupply> list = supplyEJB.findAll();
            return Response.ok(list).build();
        } catch (Exception e) {
            return Response.serverError().entity(new ResponseStatus(SuccessError.ERROR, e.getMessage())).build();
        }
    }
}
```

#### 3. Maintenance Log REST API (`HospitalMaintenanceLogRestApi.java`)

```java
package app.rest;

import app.ejb.MaintenanceLogEJB;
import app.model.MaintenanceLog;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/maintenance")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HospitalMaintenanceLogRestApi extends GenericApi<MaintenanceLog> {

    @EJB
    private MaintenanceLogEJB maintenanceEJB;

    @Path("/save")
    @POST
    public Response save(MaintenanceLog log) {
        try {
            maintenanceEJB.save(log);
            return Response.ok(new ResponseStatus(SuccessError.SUCCESS, "Maintenance log saved successfully")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ResponseStatus(SuccessError.ERROR, e.getMessage())).build();
        }
    }

    @Path("/find/{id}")
    @GET
    public Response find(@PathParam("id") Long id) {
        try {
            MaintenanceLog log = maintenanceEJB.findById(id);
            if (log == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ResponseStatus(SuccessError.ERROR, "Log not found")).build();
            }
            return Response.ok(log).build();
        } catch (Exception e) {
            return Response.serverError().entity(new ResponseStatus(SuccessError.ERROR, e.getMessage())).build();
        }
    }

    @Path("/list")
    @GET
    public Response list() {
        try {
            List<MaintenanceLog> list = maintenanceEJB.findAll();
            return Response.ok(list).build();
        } catch (Exception e) {
            return Response.serverError().entity(new ResponseStatus(SuccessError.ERROR, e.getMessage())).build();
        }
    }
}
```

#### 4. Nurse REST API (`HospitalNurseRestApi.java`)
```java
package app.rest;

import app.ejb.HospitalNurseEJB;
import app.model.HospitalNurse;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/nurse")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HospitalNurseRestApi extends GenericApi<HospitalNurse> {

    @EJB
    private HospitalNurseEJB nurseEJB;

    @Path("/save")
    @POST
    public Response save(HospitalNurse nurse) {
        try {
            nurseEJB.save(nurse);
            return Response.ok(new ResponseStatus(SuccessError.SUCCESS, "Nurse registration successfully scheduled")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ResponseStatus(SuccessError.ERROR, e.getMessage())).build();
        }
    }

    @Path("/list")
    @GET
    public Response list() {
        try {
            List<HospitalNurse> list = nurseEJB.findAll();
            return Response.ok(list).build();
        } catch (Exception e) {
            return Response.serverError().entity(new ResponseStatus(SuccessError.ERROR, e.getMessage())).build();
        }
    }
}
```

#### 5. Technician REST API (`HospitalTechnicianRestApi.java`)
```java
package app.rest;

import app.ejb.HospitalTechnicianEJB;
import app.model.HospitalTechnician;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/technician")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HospitalTechnicianRestApi extends GenericApi<HospitalTechnician> {

    @EJB
    private HospitalTechnicianEJB technicianEJB;

    @Path("/save")
    @POST
    public Response save(HospitalTechnician tech) {
        try {
            technicianEJB.save(tech);
            return Response.ok(new ResponseStatus(SuccessError.SUCCESS, "Technician registration successfully scheduled")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ResponseStatus(SuccessError.ERROR, e.getMessage())).build();
        }
    }

    @Path("/list")
    @GET
    public Response list() {
        try {
            List<HospitalTechnician> list = technicianEJB.findAll();
            return Response.ok(list).build();
        } catch (Exception e) {
            return Response.serverError().entity(new ResponseStatus(SuccessError.ERROR, e.getMessage())).build();
        }
    }
}
```

---

## 4. Verification and Testing Strategy

Once endpoints are deployed on WildFly, they can be tested locally using the following tools:

### Listing Medical Equipment
```bash
curl -X GET http://localhost:8080/VitalTrack/api/equipment/list -H "Accept: application/json"
```

### Retrieving a Specific Medical Supply
```bash
curl -X GET http://localhost:8080/VitalTrack/api/supply/find/1 -H "Accept: application/json"
```

### Submitting a New Medical Equipment Record
```bash
curl -X POST http://localhost:8080/VitalTrack/api/equipment/save \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "name": "Defibrillator D-50",
    "serialNumber": "SN-DEF-505",
    "purchaseDate": "2024-01-10",
    "lastCalibrationDate": "2026-05-15",
    "status": "OPERATIONAL"
  }'
```

---

## 5. Summary of Actions Required Next
1.  **Approval**: Confirm if the layout and design of this implementation plan matches your expectations.
2.  **Execution**: Upon approval, we will create the classes under `app.rest`, modify `HospitalAuthenticationFilter.java` to whitelist `/api/*`, expand EJBs where necessary, and add `@JsonIgnore` to the `password` fields of the nurse and technician models.
