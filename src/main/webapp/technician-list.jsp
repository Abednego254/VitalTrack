<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Staff List | VitalTrack</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="app-layout">
    <jsp:include page="sidebar.jsp" />
    <main class="main-content">
        <div class="page-header">
            <h1>Technical Personnel</h1>
            <a href="technician" class="btn" style="width: auto;">+ Add Technician</a>
        </div>
        <div class="card" style="cursor: default;">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Specialization</th>
                        <th>Contact</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${items}" var="item">
                        <tr>
                            <td>${item.id}</td>
                            <td>${item.name}</td>
                            <td>${item.specialization}</td>
                            <td>${item.contactInfo}</td>
                            <td><span class="status-badge">${item.status}</span></td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty items}">
                        <tr><td colspan="5" style="text-align: center;">No technicians found in the vault.</td></tr>
                    </c:if>
                </tbody>
            </table>
            <div style="margin-top: 1.5rem; text-align: center;">
                <a href="index.jsp" class="back-btn">← Back to Dashboard</a>
            </div>
        </div>
    </main>
</body>
</html>
