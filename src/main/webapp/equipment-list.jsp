<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Equipment List | VitalTrack</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="auth-wrapper" style="display: block; max-width: 1000px; margin: 0 auto; padding-top: 4rem;">
        <div class="page-header">
            <h1>Registered Equipment</h1>
            <a href="equipment" class="btn" style="width: auto;">+ Add New</a>
        </div>
        <div class="card" style="cursor: default;">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Serial No</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${items}" var="item">
                        <tr>
                            <td>${item.id}</td>
                            <td>${item.name}</td>
                            <td>${item.serialNumber}</td>
                            <td><span class="status-badge">${item.status}</span></td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty items}">
                        <tr><td colspan="4" style="text-align: center;">No equipment found in the vault.</td></tr>
                    </c:if>
                </tbody>
            </table>
            <div style="margin-top: 1.5rem; text-align: center;">
                <a href="index.jsp" class="back-btn">← Back to Dashboard</a>
            </div>
        </div>
    </div>
</body>
</html>
