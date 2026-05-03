# Advanced Framework Architecture

This document details the advanced custom framework architecture implemented in the VitalTrack application to align with modern Jakarta EE best practices.

## Overview
The backend architecture is structured around three core custom frameworks:
1. **Custom ORM & Generic DAO** (The Storage Room)
2. **Validation Abstraction** (The Bouncers)
3. **Listener-based Bootstrapping** (The Morning Checklist)

---

## 1. Custom ORM & Generic DAO (Persistence Layer)

To keep our HTTP Servlets (`app.action`) clean, database logic has been completely separated into a Data Access layer using a Generic DAO and custom annotations.

### The "Magical Sticky Notes" (Annotations)
We use two custom runtime annotations to map Java objects to database tables:
*   `@DbTable(name = "TableName")`: Applied at the **Class Level** to specify the corresponding database table.
*   `@DbColumn(name = "colName", type = "VARCHAR", primaryKey = false, autoIncrement = false)`: Applied at the **Field Level** to specify how a class property maps to a database column.

### The Generic DAO
`app.dao.GenericDao<T, ID>` acts as the ultimate "Storage Room Worker". 
Instead of writing raw SQL for every entity, the `GenericDao` uses Java Reflection to read the `@DbTable` and `@DbColumn` annotations at runtime. It automatically constructs and executes the `INSERT`, `SELECT`, `UPDATE`, and `DELETE` SQL commands.

**How it's used:**
```java
// Inside an EJB
private GenericDao<HospitalEquipment, Long> equipmentDao;

@PostConstruct
public void init() {
    // We instantiate the generic worker for the specific entity
    this.equipmentDao = new GenericDao<>(HospitalEquipment.class, dataSourceHelper);
}

// To save:
equipmentDao.save(equipment);
```

---

## 2. The Validation Abstraction (Business Layer)

Before an EJB sends data to the DAO to be saved, the data must be validated. We use a strictly typed, CDI-managed validation layer.

### The Rulebook (`Validate<T>`)
A generic interface `app.utility.validation.Validate<T>` defines the contract for all validators.
It mandates a `boolean process(T entity)` method.

### Specific Validators
For each entity, a specific validator implements the interface. These validators act as "Bouncers" ensuring bad data doesn't reach the database.
They are managed by the CDI container using `@ApplicationScoped` (one instance per app) and named using the `@Named` qualifier.

```java
@Named("ValidEquipment")
@ApplicationScoped
public class ValidateEquipment implements Validate<HospitalEquipment> {
    @Override
    public boolean process(HospitalEquipment equipment) {
        // Validation rules here...
        return true; 
    }
}
```

### CDI Injection in EJBs
The EJB injects the specific validator using the `@Named` qualifier and calls the `.process()` method before saving.

```java
@Inject
@Named("ValidEquipment")
private Validate<HospitalEquipment> validator;

public void save(HospitalEquipment equipment) {
    if(validator.process(equipment)) {
        equipmentDao.save(equipment);
    } else {
        throw new IllegalArgumentException("Invalid data!");
    }
}
```

---

## 3. WebListener Bootstrapping (Startup Layer)

VitalTrack avoids monolithic startup scripts (like `@Singleton @Startup` EJBs) by using a Strategy Pattern executed by a Servlet Context Listener.

### The Strategy Interface
`app.utility.bootstrap.Bootstrap` defines a simple `process()` method. Any task that needs to run when the hospital opens must implement this interface.

### Example Task: DatabaseBootstrap
`DatabaseBootstrap` implements `Bootstrap`. Its job is to use the `TableGenerator` utility to dynamically read all `@DbTable` annotations and execute `CREATE TABLE IF NOT EXISTS` queries when the application starts.

### The Hospital Manager (`AppContextListener`)
`app.listeners.AppContextListener` is a standard `@WebListener`. 
It utilizes an advanced CDI trick (`@Any Instance<T>`) to automatically collect every single class in the project that implements the `Bootstrap` interface.

```java
@Inject
@Any
private Instance<Bootstrap> bootstraps;

@Override
public void contextInitialized(ServletContextEvent sce) {
    // Loop through every single startup task and execute it!
    for (Bootstrap bootstrap : bootstraps) {
        bootstrap.process();
    }
}
```
This makes the startup process highly extensible. Adding a new startup routine requires zero modification to the `AppContextListener` itself. You only need to create a new class implementing `Bootstrap`.
