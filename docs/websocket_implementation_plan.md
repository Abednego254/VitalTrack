# Implementation Plan: Real-Time WebSockets in VitalTrack

This plan details the design, architecture, and step-by-step implementation tasks required to integrate bi-directional real-time communication into the VitalTrack portal using standard **Jakarta WebSocket API**.

## 1. Objectives & Features
1. **Real-Time Low Stock Alerts**: Pushes warnings to all logged-in `ADMIN` and `NURSE` users instantly when supply stock drops below the reorder level. Removes legacy store room scan email alerts.
2. **Live System Activity Feed**: Automatically appends system logs (added equipment, logins, deleted technicians) to the Admin Dashboard as they happen.
3. **Group Chat Widget**: A real-time floating chat sidebar allowing Admins, Nurses, and Technicians to communicate without page refreshes.

---

## 2. Architectural Design

```mermaid
graph TD
    subgraph Client Layer (Browser)
        UI[AppPage / Dashboard]
        WS_JS[websocket.js]
        Chat[Chat Widget]
        Alerts[Toast Notifications]
        UI --> WS_JS
    end

    subgraph Server Layer (WildFly)
        WS_Endpoint[VitalTrackWebSocketServer.java]
        AuthFilter[HospitalAuthenticationFilter]
        
        subgraph Business Logic (EJBs & CDIs)
            StockMonitor[StockMonitorBean]
            AuditTrail[AuditTrailBean]
            LoginAct[LoginAction]
        end
    end

    WS_JS <== WebSocket Connection ==> WS_Endpoint
    AuthFilter -- Whitelist Bypass --> WS_Endpoint
    StockMonitor -- CDI / Method Call --> WS_Endpoint
    AuditTrail -- CDI / Method Call --> WS_Endpoint
    LoginAct -- Fires Login Audit --> AuditTrail
```

### Server-Side Components
1. **`VitalTrackWebSocketServer.java`** (`@ServerEndpoint("/ws/{username}/{role}")`):
   * Manages active WebSocket sessions.
   * Maintains a thread-safe static collection of connected sessions mapped by user info and roles.
   * Handles incoming chat payloads and broadcasts them.
   * Provides helper methods for broadcasting notifications targeted by roles.

2. **`HospitalAuthenticationFilter.java`**:
   * Add a whitelist rule for `/ws/*` requests to allow WebSocket upgrade handshakes to succeed without redirecting to `/login` (since WebSockets do not support HTTP redirects).

3. **Event Integrations**:
   * **Low Stock**: Triggered inside `StockMonitorBean` (which processes `MedicalSupplyConsumedEvent`). It calls `VitalTrackWebSocketServer` to broadcast a warning to `ADMIN` and `NURSE` users.
   * **Activity Feed**: Triggered inside `AuditTrailBean` (which processes `AuditTrail` events). It calls `VitalTrackWebSocketServer` to broadcast the activity message to `ADMIN` users.
   * **Login Events**: Fire a new `AuditTrail` event inside `LoginAction.java` upon successful login, enabling immediate broadcast to the Admin activity feed.

---

## 3. UI/UX Design

### A. Dynamic Chat Widget (Floating Panel)
* Embed a collapsible chat widget in the bottom-right corner of the global dashboard frame (`AppPage.java`).
* Structure:
  * Chat Header (collapsible, shows online count).
  * Chat Body (scrollable list of messages with sender name, role, and message text).
  * Chat Footer (Input text field + Send button).
* Styling (Vanilla CSS in `style.css`): Modern Glassmorphism layout matching the existing theme.

### B. Notification Toaster
* Add a Toast Container in the top-right corner of the dashboard viewport.
* When a `stock_alert` event arrives, generate a sliding toast element with a warning style (orange/red accent).

### C. Live Activity Feed (Admin Dashboard)
* Update `HomeAction.java` to append a "Live System Activity Feed" container for `ADMIN` users.
* Initialize the feed by loading the last 5 records from the database on page load.
* Listen for new `"activity"` messages via WebSocket and prepend them dynamically using vanilla JavaScript.

---

## 4. Implementation Steps

### Phase 1: Server Infrastructure
- [ ] **Step 1.1**: Update `HospitalAuthenticationFilter.java` to whitelist `/ws/` paths.
- [ ] **Step 1.2**: Create `VitalTrackWebSocketServer.java` using `@ServerEndpoint`.
- [ ] **Step 1.3**: Implement JSON encoding/decoding helper or use basic String-based JSON formatting.
- [ ] **Step 1.4**: Remove email-sending logic from `ExternalAuditServerBean.java` (`sendUrgentEmail(...)`).

### Phase 2: Event Integration
- [ ] **Step 2.1**: Update `StockMonitorBean.java` to invoke the WebSocket broadcast for low stock alerts.
- [ ] **Step 2.2**: Update `AuditTrailBean.java` to invoke the WebSocket broadcast for live activities.
- [ ] **Step 2.3**: Update `LoginAction.java` to fire `AuditTrail` events on successful login.

### Phase 3: UI & Client-Side Scripts
- [ ] **Step 3.1**: Create `websocket.js` containing connection logic, toast alerts, chat panel handling, and activity feed updates.
- [ ] **Step 3.2**: Modify `AppPage.java` to:
  * Include the floating chat HTML container.
  * Inject current session username and role into client-side JS scope.
  * Link the `/js/websocket.js` script.
- [ ] **Step 3.3**: Modify `HomeAction.java` to append the Live Activity Feed HTML element for admins.
- [ ] **Step 3.4**: Add custom CSS rules for the Chat Widget, Toast Notifications, and Activity Feed in `style.css`.
