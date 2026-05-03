# Enterprise Java Beans (EJB) in VitalTrack
**Branch:** `feature/database`  
**Date:** May 2026  
**Author:** VitalTrack Development Team

---

## 1. What is an Enterprise Java Bean (EJB)?

An **Enterprise Java Bean** is a special type of Java class that is managed by the application server (WildFly). Unlike a plain CDI bean, an EJB comes with **enterprise superpowers** built in automatically:

| Superpower | What it means for VitalTrack |
|---|---|
| **Automatic Transactions** | If saving equipment fails halfway, WildFly rolls back everything automatically — no data corruption |
| **Thread Safety** | WildFly ensures that multiple nurses using the system simultaneously do not collide |
| **Bean Pooling** | WildFly keeps a pool of `@Stateless` beans ready, improving performance under load |
| **Lifecycle Management** | WildFly controls when the bean is created and destroyed |

---

## 2. The Three Types of EJBs Used

### 2.1 `@Stateless` — "The Fast Food Waiter"

A `@Stateless` bean has **no memory between calls**. It serves one request, forgets everything, and becomes available for the next caller immediately.

**Analogy:** Like a fast-food counter worker — they serve you, take payment, and immediately turn to serve the next customer. They do not remember your previous orders.

**When to use:** Any operation that is self-contained — saving a record, fetching a list, calculating a value.

**Example in VitalTrack:**
```java
@Stateless
public class HospitalEquipmentEJB {

    @Inject
    private DataSourceHelper dataSourceHelper;

    // Every method here is automatically wrapped in a transaction!
    public void save(HospitalEquipment equipment) throws Exception { ... }
    public List<HospitalEquipment> findAll() throws Exception { ... }
}
```

---

### 2.2 `@Singleton` — "The Hospital Director"

A `@Singleton` bean has **only ONE instance** for the entire application. Every part of the system shares the same single instance.

**Analogy:** Like the Hospital Director — there is only one, everyone reports to them, and they are always present.

**When to use:** Application-wide configuration, startup tasks, shared caches.

**Example in VitalTrack:**
```java
@Singleton
@Startup  // Created immediately when WildFly starts — no waiting!
public class HospitalConfigEJB {

    @PostConstruct
    public void onStartup() {
        // Runs once when WildFly starts
        System.out.println(" HOSPITAL DIRECTOR REPORTING! ALL SYSTEMS READY.");
    }

    @PreDestroy
    public void onShutdown() {
        // Runs once just before WildFly stops
        System.out.println(" HOSPITAL DIRECTOR: Signing off. Goodbye!");
    }
}
```

> **Note:** `@Startup` is unique to EJBs — it forces WildFly to create the bean
> immediately on deployment, without waiting for a first request.

---

### 2.3 `@Stateful` — "The Personal Nurse" (Conceptual Reference)

A `@Stateful` bean **remembers its state** across multiple calls within the same client session.

**Analogy:** Like a personal nurse who follows you through your entire hospital visit, remembering your full history.

**When to use:** Multi-step wizards, shopping carts, anything that accumulates state over time.

> VitalTrack does not currently implement `@Stateful` beans — this is listed
> here for completeness and future reference.

---

## 3. How EJBs Are Injected

### Into a Servlet — use `@EJB`
```java
@WebServlet("/equipment")
public class HospitalEquipmentAction extends HospitalBaseAction<HospitalEquipment> {

    // @EJB tells WildFly: "Give me one of your pooled Stateless beans!"
    @EJB
    private HospitalEquipmentEJB equipmentEJB;
}
```

> **Important:** We use `@EJB` (not `@Inject`) when injecting EJBs into Servlets.
> `@Inject` is for CDI beans. `@EJB` is specifically for Enterprise Java Beans.

### Into another EJB or CDI bean — use `@Inject`
```java
@Stateless
public class HospitalEquipmentEJB {

    @Inject  // DataSourceHelper is a CDI bean, so @Inject works here
    private DataSourceHelper dataSourceHelper;
}
```

---

## 4. The VitalTrack EJB Architecture

Before EJBs, our Servlet was doing **everything** — handling HTTP AND talking to the database. This is known as mixing responsibilities.

After EJBs, each layer has **one job only**:

```
Browser (HTTP Request)
       │
       ▼
┌─────────────────────────────────┐
│  HospitalEquipmentAction        │  @WebServlet
│  Responsibility: Handle HTTP    │  Reads form data, forwards to JSP
└────────────────┬────────────────┘
                 │ @EJB injection
                 ▼
┌─────────────────────────────────┐
│  HospitalEquipmentEJB           │  @Stateless
│  Responsibility: Database work  │  save(), findAll()
└────────────────┬────────────────┘
                 │ @Inject
                 ▼
┌─────────────────────────────────┐
│  DataSourceHelper               │  @ApplicationScoped (CDI)
│  Responsibility: Connection     │  Opens MySQL connections
└─────────────────────────────────┘
                 │
                 ▼
         MySQL Database (vitaltrack_db)
```

---

## 5. EJBs Implemented in VitalTrack

| EJB Class | Type | Manages |
|---|---|---|
| `HospitalConfigEJB` | `@Singleton @Startup` | App startup/shutdown logging |
| `HospitalEquipmentEJB` | `@Stateless` | Equipment CRUD operations |
| `HospitalMedicalSupplyEJB` | `@Stateless` | Medical Supply CRUD operations |
| `HospitalTechnicianEJB` | `@Stateless` | Technician CRUD operations |
| `HospitalMaintenanceLogEJB` | `@Stateless` | Maintenance Log CRUD operations |

---

## 6. Automatic Transactions Explained

This is one of the most powerful EJB features. Consider this scenario:

**Without EJB (manual, dangerous):**
```
1. Open database connection
2. Insert equipment record       ← What if the server crashes here?
3. Update stock count            ← This never runs — data is now inconsistent!
4. Close connection
```

**With EJB (automatic, safe):**
```
1. WildFly opens a Transaction
2. Insert equipment record       ← Server crashes here?
3. Update stock count            ← WildFly sees the crash...
                                 ← ROLLBACK! Step 2 is undone automatically!
4. WildFly commits Transaction
```

Every `public` method in a `@Stateless` EJB is automatically wrapped in a transaction. If **anything** goes wrong, WildFly rolls back all changes as if nothing happened.

---

## 7. JNDI — The Server's Phone Directory

When WildFly deploys an EJB, it registers it in **JNDI** (Java Naming and Directory Interface) — a global phone directory of all available beans.

Every EJB gets a unique JNDI address, for example:
```
java:global/VitalTrack/HospitalEquipmentEJB!app.ejb.HospitalEquipmentEJB
```

This address can be used by **any Java program on any machine** to look up and call the EJB remotely — even from a completely different JVM. This is the foundation for **Remote EJBs** (future concept).

---

## 8. Key Annotations Summary

| Annotation | Package | Purpose |
|---|---|---|
| `@Stateless` | `jakarta.ejb` | Pooled, no-memory EJB |
| `@Singleton` | `jakarta.ejb` | Single-instance EJB |
| `@Stateful` | `jakarta.ejb` | Session-aware EJB |
| `@Startup` | `jakarta.ejb` | Initialize on app deployment |
| `@EJB` | `jakarta.ejb` | Inject an EJB into a Servlet |
| `@PostConstruct` | `jakarta.annotation` | Run method after bean creation |
| `@PreDestroy` | `jakarta.annotation` | Run method before bean destruction |

---

## 9. Files Changed in This Implementation

```
src/main/java/
  app/ejb/
    HospitalConfigEJB.java          [NEW] @Singleton @Startup
    HospitalEquipmentEJB.java       [NEW] @Stateless
    HospitalMedicalSupplyEJB.java   [NEW] @Stateless
    HospitalTechnicianEJB.java      [NEW] @Stateless
    HospitalMaintenanceLogEJB.java  [NEW] @Stateless
  app/action/
    HospitalEquipmentAction.java    [MODIFIED] Now uses @EJB
    HospitalMedicalSupplyAction.java[MODIFIED] Now uses @EJB
    HospitalTechnicianAction.java   [MODIFIED] Now uses @EJB
    HospitalMaintenanceLogAction.java[MODIFIED] Now uses @EJB
```
