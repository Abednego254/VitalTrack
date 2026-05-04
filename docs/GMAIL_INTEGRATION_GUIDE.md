# VitalTrack: Gmail Integration Guide

This document explains how we integrated real-time email notifications into the VitalTrack system using **Jakarta Mail** and **EJB Schedulers**.

## 1. Architectural Overview
We have a `@Singleton` bean called `EmailReminderBean`. It uses an EJB `@Schedule` to wake up every 30 seconds and check system status. When it needs to send a reminder, it uses a **Mail Session** provided by the WildFly container.

## 2. Jakarta EE Components
- **`@Resource`**: Used to inject the `jakarta.mail.Session` without hardcoding server details in the Java class.
- **`MimeMessage`**: The object used to construct the email (Subject, To, From, Body).
- **`Transport.send()`**: The method that hands the message to the SMTP server for delivery.

## 3. WildFly Configuration (Gmail)
To send real emails via Gmail, we configured the server using the following Management CLI commands:

### A. Socket Binding (The Connection)
```bash
/socket-binding-group=standard-sockets/remote-destination-outbound-socket-binding=mail-smtp:write-attribute(name=host, value=smtp.gmail.com)
/socket-binding-group=standard-sockets/remote-destination-outbound-socket-binding=mail-smtp:write-attribute(name=port, value=587)
```

### B. Mail Session (The Credentials)
```bash
/subsystem=mail/mail-session=default:write-attribute(name=jndi-name, value="java:jboss/mail/Default")

/subsystem=mail/mail-session=default/server=smtp:write-attribute(name=username, value="YOUR_GMAIL_ADDRESS")
/subsystem=mail/mail-session=default/server=smtp:write-attribute(name=password, value="YOUR_GMAIL_APP_PASSWORD")
/subsystem=mail/mail-session=default/server=smtp:write-attribute(name=tls, value=true)
```

## 4. Security Best Practices
- **App Passwords**: We used a Google App Password instead of the main account password. This is mandatory for Gmail's 2FA.
- **JNDI Lookup**: By using `@Resource(lookup = "...")`, we keep sensitive credentials in the server's `standalone.xml` rather than in our source code.

---
*Created during Phase 4 of VitalTrack Modernization.*
