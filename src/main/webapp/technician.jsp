<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register Technician | VitalTrack</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="app-layout">
    <jsp:include page="sidebar.jsp" />
    <main class="main-content">
        <div class="page-header">
            <h1>Register Technician</h1>
            <a href="index.jsp" class="back-btn">← Home</a>
        </div>
        <div class="card" style="cursor: default;">
            <form action="technician" method="POST">
                <div class="form-group">
                    <label for="name">Full Name</label>
                    <input type="text" id="name" name="name" required>
                </div>
                <div class="form-group">
                    <label for="specialization">Specialization</label>
                    <input type="text" id="specialization" name="specialization" placeholder="e.g. Biomedical Engineer" required>
                </div>
                <div class="form-group">
                    <label for="email">Whitelist Email (Login)</label>
                    <input type="email" id="email" name="email" placeholder="e.g. tech@hospital.com" required>
                </div>
                <div class="form-group">
                    <label for="contactInfo">Phone / Extra Info</label>
                    <input type="text" id="contactInfo" name="contactInfo" placeholder="e.g. 0712345678" required>
                </div>
                <div class="form-group">
                    <label for="status">Status</label>
                    <select id="status" name="status">
                        <option value="Available">Available</option>
                        <option value="On Duty">On Duty</option>
                        <option value="Off Duty">Off Duty</option>
                    </select>
                </div>
                <button type="submit" class="btn">Register Technician 🔒</button>
            </form>
            <a href="technician?view=list" class="view-list">View Staff List →</a>
        </div>
    </main>
</body>
</html>
