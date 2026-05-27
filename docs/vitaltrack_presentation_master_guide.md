# VitalTrack: Mastering the Presentation Guide (Systech Limited Prep)

This guide is designed to prepare you to walk into your presentation with absolute confidence, demonstrate the system like an expert architect, and handle any tough question your trainer throws at you.

---

## 1. The Core Presentation Narrative (Your Opening Pitch)
When you start, don't just show the UI. Tell the architectural story.

> *"For this project, the goal was not just to build a functional medical logistics application, but to adhere strictly to the **Enterprise Java (Jakarta EE 10)** multi-tier pattern. The system is designed to be highly decoupled, transactional, and performant. In addition, I integrated two advanced enterprise features: **real-time event-driven notifications** via WebSockets and **container-managed security** via Jakarta Security (JSR 375)."*

---

## 2. Walkthrough Demo Script (Step-by-Step)

Follow this exact path during the live demo to tell a compelling story:

### Act I: Secure Container Login (The entry point)
1. **Show the login page**. Explain that this page is completely protected at the container level by **Jakarta Security**.
2. **Log in as a new Nurse or Technician** (using a temporary password starting with `VT-TEMP-`).
3. **Show the redirect**: Point out how the system instantly redirects them to the Password Reset screen. Mention that the HTTP Authentication Mechanism intercepts this automatically.
4. **Log in as Admin** (`admin`): Show the main dashboard. Point out the beautiful new split grid: the **Live Activity Feed** on the left and the **System Health Monitor** on the right.

### Act II: Real-Time Event loop (The WebSockets demo)
1. **Open two browser windows side-by-side**: 
   - Window A: Logged in as Admin.
   - Window B: Logged in as a Nurse.
2. **Open the Operations Chat**: Show that they can chat in real time. Type a message in the Nurse window and see it instantly appear in the Admin window.
3. **Consume stock below reorder level**:
   - In the Nurse window, go to the Medical Supplies list and click **Consume** on an item (e.g. syringes) so that its quantity falls below its reorder level.
   - **Watch the magic**: The Admin window will instantly pop up a **red toast notification** and update the **Critical Stock Warnings** banner at the very top of the dashboard—all without refreshing the page!

### Act III: The Audit Trail (The enterprise backbone)
1. In the Admin dashboard, click on **Audit Logs**.
2. Show that the consumption action and the low-stock warning were logged with high-urgency markers. Explain that this was fired via CDI Events decoupled from the main web thread.

---

## 3. Anticipated Q&A (Tough Questions & How to Ace Them)

Trainers love to ask "Why did you do it this way?" Here are the answers:

| Question | Your Answer (The "Ace") |
| :--- | :--- |
| **"Why did you use a custom HTTP Authentication Mechanism instead of standard Form Auth?"** | *"To keep 100% backward compatibility with the existing JSP files and front-end layout. By building a custom `HttpAuthenticationMechanism` and bridging it with a custom `IdentityStore`, we got container-managed roles (`@RolesAllowed`) while preserving all existing view layers that read session attributes."* |
| **"What was the WildFly Elytron authorization error (`ELY01177`) and how did you resolve it?"** | *"WildFly's Elytron operates in 'integrated-jaspi' mode by default. It assumes dynamic logins exist in static server realms. Since our users are dynamic (in MySQL), Elytron blocked it. I used the WildFly CLI to disable integrated JASPI (`integrated-jaspi=false`) for the `other` domain, delegating verification to our application's Jakarta Security module."* |
| **"Why are WebSockets mapped to separate classes rather than a single endpoint?"** | *"To maintain a clean separation of concerns and avoid a single point of failure. We aligned the structure with best practices: `/chat` for user chat, `/stock_alerts` for inventory warnings, and `/audit_feeds` for live activity logs. This makes each channel thread-safe and focused."* |
| **"How is database consistency guaranteed when multiple users consume stock?"** | *"All database changes run inside Container-Managed Transactions (CMT) using EJB `@Stateless` beans. If any validation fails (e.g. constraint violation), the transaction automatically rolls back, ensuring no partial or corrupted data is saved to MySQL."* |

---

## 4. Let's Practice!
To help you feel completely calm, we can do interactive mock Q&As. Let me know if you want me to ask you a question so you can practice drafting your answer!
