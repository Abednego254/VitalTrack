<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>VitalTrack | Medical Logistics</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="app-layout">

    <jsp:include page="sidebar.jsp" />

    <main class="main-content">
        <header class="hero">
            <h1>${hospitalInfo.name}</h1>
            <p>${hospitalInfo.tagline}</p>
            <div class="badge badge-info" style="margin-top: 1rem;">📍 ${hospitalInfo.location}</div>
        </header>

        <section class="container">

            <div class="card" onclick="location.href='equipment'">
                <div class="card-icon">EQ</div>
                <h3>Medical Equipment</h3>
                <p>Track calibration status and maintenance cycles for all medical devices.</p>
                <span class="badge badge-success">Online</span>
            </div>

            <c:if test="${role == 'ADMIN'}">
                <div class="card" onclick="location.href='medicalsupply'">
                    <div class="card-icon">SP</div>
                    <h3>Medical Supplies</h3>
                    <p>Monitor inventory levels and expiration dates for consumable stocks.</p>
                    <span class="badge badge-success">Stock OK</span>
                </div>

                <div class="card" onclick="location.href='technician'">
                    <div class="card-icon">TC</div>
                    <h3>Technicians</h3>
                    <p>View availability and specialization of service personnel.</p>
                    <span class="badge badge-warning">2 On-Call</span>
                </div>
            </c:if>

            <div class="card" onclick="location.href='maintenancelog?view=list'">
                <div class="card-icon">ML</div>
                <h3>${role == 'TECHNICIAN' ? 'Maintenance Command' : 'Maintenance Logs'}</h3>
                <p>${role == 'TECHNICIAN' ? 'Access your superpower: View and manage every repair and calibration across the hospital.' : 'Historical records of all equipment servicing and repairs.'}</p>
                <span class="badge badge-success">${role == 'TECHNICIAN' ? 'Active' : 'Secure'}</span>
            </div>

            <c:if test="${role == 'ADMIN'}">
                <div class="card" onclick="location.href='audit-trail'" style="border: 1px solid var(--primary-light);">
                    <div class="card-icon" style="background: #e0e7ff;">AU</div>
                    <h3>System Audit Trail</h3>
                    <p>Advanced security logs tracking all system activities and events.</p>
                    <span class="badge badge-info">Admin Only</span>
                </div>
            </c:if>
        </section>

        <footer>
            &copy; 2026 VitalTrack Medical Logistics System. Developed for Jakarta EE Training.
        </footer>
    </main>

</body>
</html>
