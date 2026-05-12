<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Log Maintenance | VitalTrack</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">
    <style>
        :root { --primary: #f59e0b; --background: #f8fafc; --card: #ffffff; --text: #1e293b; --border: #e2e8f0; }
        body { font-family: 'Inter', sans-serif; background-color: var(--background); color: var(--text); padding: 2rem; }
        .container { max-width: 600px; margin: 0 auto; }
        .header { margin-bottom: 2rem; display: flex; justify-content: space-between; align-items: center; }
        .header h1 { color: var(--primary); margin: 0; }
        .back-btn { text-decoration: none; color: var(--text); font-weight: 600; }
        .card { background: var(--card); padding: 2rem; border-radius: 1rem; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1); }
        .form-group { margin-bottom: 1.5rem; }
        .form-group label { display: block; margin-bottom: 0.5rem; font-weight: 600; }
        .form-group input, .form-group textarea, .form-group select { width: 100%; padding: 0.75rem; border: 1px solid var(--border); border-radius: 0.5rem; font-family: inherit; }
        .form-group textarea { resize: vertical; min-height: 80px; }
        .btn { background: var(--primary); color: white; padding: 0.75rem; border: none; border-radius: 0.5rem; font-weight: 700; cursor: pointer; width: 100%; font-size: 1rem; }
        .view-list { display: block; text-align: center; margin-top: 1.5rem; color: var(--primary); text-decoration: none; font-weight: 600; }
        .tech-info { background: #fffbeb; color: #92400e; padding: 0.75rem; border-radius: 0.5rem; margin-bottom: 1.5rem; font-size: 0.85rem; border-left: 4px solid var(--primary); }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>📝 Log Maintenance</h1>
            <a href="index.jsp" class="back-btn">← Home</a>
        </div>
        <div class="card">
            <div class="tech-info">
                <strong>Technician:</strong> ${username} (Active Session)
            </div>
            <form action="maintenancelog" method="POST">
                <div class="form-group">
                    <label for="equipmentId">Target Equipment</label>
                    <select id="equipmentId" name="equipmentId" required>
                        <option value="">-- Select Equipment --</option>
                        <c:forEach items="${equipments}" var="equip">
                            <option value="${equip.id}">${equip.name} (${equip.serialNumber})</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="serviceDate">Service Date</label>
                    <input type="date" id="serviceDate" name="serviceDate" required>
                </div>
                <div class="form-group">
                    <label for="actionTaken">Action Taken</label>
                    <textarea id="actionTaken" name="actionTaken" placeholder="Describe what was done..."></textarea>
                </div>
                <div class="form-group">
                    <label for="notes">Notes</label>
                    <textarea id="notes" name="notes" placeholder="Any additional notes..."></textarea>
                </div>
                <button type="submit" class="btn">Save Log 🔒</button>
            </form>
            <a href="maintenancelog?view=list" class="view-list">View Maintenance History →</a>
        </div>
    </div>
</body>
</html>
