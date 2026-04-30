<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Maintenance History | VitalTrack</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">
    <style>
        :root { --primary: #f59e0b; --background: #f8fafc; --card: #ffffff; --text: #1e293b; --border: #e2e8f0; }
        body { font-family: 'Inter', sans-serif; background-color: var(--background); color: var(--text); padding: 2rem; }
        .container { max-width: 900px; margin: 0 auto; }
        .header { margin-bottom: 2rem; display: flex; justify-content: space-between; align-items: center; }
        .header h1 { color: var(--primary); margin: 0; }
        .card { background: var(--card); padding: 2rem; border-radius: 1rem; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1); }
        table { width: 100%; border-collapse: collapse; }
        th, td { text-align: left; padding: 1rem; border-bottom: 1px solid var(--border); }
        th { background: #fef3c7; font-size: 0.85rem; text-transform: uppercase; letter-spacing: 0.05em; color: #92400e; }
        .add-new { display: inline-block; background: var(--primary); color: white; padding: 0.5rem 1rem; border-radius: 0.5rem; text-decoration: none; font-weight: 600; font-size: 0.9rem; }
        .back-btn { text-decoration: none; color: var(--text); font-weight: 600; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>📋 Maintenance History</h1>
            <a href="maintenance" class="add-new">+ Log New</a>
        </div>
        <div class="card">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Equipment ID</th>
                        <th>Technician ID</th>
                        <th>Service Date</th>
                        <th>Action Taken</th>
                        <th>Notes</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${items}" var="item">
                        <tr>
                            <td>${item.id}</td>
                            <td>${item.equipmentId}</td>
                            <td>${item.technicianId}</td>
                            <td>${item.serviceDate}</td>
                            <td>${item.actionTaken}</td>
                            <td>${item.notes}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty items}">
                        <tr><td colspan="6" style="text-align:center;">No maintenance records found.</td></tr>
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
