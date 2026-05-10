<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register Technician | VitalTrack</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">
    <style>
        :root { --primary: #8b5cf6; --background: #f8fafc; --card: #ffffff; --text: #1e293b; --border: #e2e8f0; }
        body { font-family: 'Inter', sans-serif; background-color: var(--background); color: var(--text); padding: 2rem; }
        .container { max-width: 600px; margin: 0 auto; }
        .header { margin-bottom: 2rem; display: flex; justify-content: space-between; align-items: center; }
        .header h1 { color: var(--primary); margin: 0; }
        .back-btn { text-decoration: none; color: var(--text); font-weight: 600; }
        .card { background: var(--card); padding: 2rem; border-radius: 1rem; box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1); }
        .form-group { margin-bottom: 1.5rem; }
        .form-group label { display: block; margin-bottom: 0.5rem; font-weight: 600; }
        .form-group input, .form-group select { width: 100%; padding: 0.75rem; border: 1px solid var(--border); border-radius: 0.5rem; }
        .btn { background: var(--primary); color: white; padding: 0.75rem; border: none; border-radius: 0.5rem; font-weight: 700; cursor: pointer; width: 100%; }
        .view-list { display: block; text-align: center; margin-top: 1.5rem; color: var(--primary); text-decoration: none; font-weight: 600; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Register Technician</h1>
            <a href="index.jsp" class="back-btn">← Home</a>
        </div>
        <div class="card">
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
    </div>
</body>
</html>
