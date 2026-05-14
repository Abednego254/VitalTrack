<%--
  Created by IntelliJ IDEA.
  User: abednegokaume
  Date: 5/10/26
  Time: 6:22 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Audit Trail | VitalTrack</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">
    <style>
        :root { --primary: #6366f1; --background: #f8fafc; --card: #ffffff; --text: #1e293b; --border: #e2e8f0; }
        body { font-family: 'Inter', sans-serif; background-color: var(--background); color: var(--text); padding: 2rem; }
        .container { max-width: 900px; margin: 0 auto; }
        .header { margin-bottom: 2rem; display: flex; justify-content: space-between; align-items: center; }
        .header h1 { color: var(--primary); margin: 0; }
        .back-btn { text-decoration: none; color: var(--text); font-weight: 600; }
        .card { background: var(--card); padding: 2rem; border-radius: 1rem; box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1); }
        table { width: 100%; border-collapse: collapse; margin-top: 1rem; }
        th, td { text-align: left; padding: 1rem; border-bottom: 1px solid var(--border); }
        th { background: #f1f5f9; font-size: 0.85rem; text-transform: uppercase; letter-spacing: 0.05em; font-weight: 700; }
        .activity { font-size: 0.95rem; line-height: 1.5; }
        .badge { display: inline-block; padding: 0.25rem 0.5rem; border-radius: 0.25rem; font-size: 0.75rem; font-weight: 700; text-transform: uppercase; }
        .badge-info { background: #e0e7ff; color: #4338ca; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>System Audit Trail</h1>
            <a href="index.jsp" class="back-btn">← Home</a>
        </div>
        <div class="card">
            <p style="font-size: 0.9rem; color: #64748b; margin-bottom: 1.5rem;">
                Monitoring all hospital system activities for security and compliance.
            </p>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Activity / Event Log</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${logs}" var="log">
                        <tr>
                            <td style="width: 80px; color: #94a3b8; font-weight: 600;">#${log.id}</td>
                            <td class="activity">
                                <span class="badge badge-info">LOG</span>
                                <span style="margin-left: 0.5rem;">${log.activity}</span>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty logs}">
                        <tr><td colspan="2" style="text-align: center; padding: 2rem; color: #94a3b8;">No activity logs found.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </main>
</body>
</html>
