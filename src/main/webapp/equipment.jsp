<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Equipment | VitalTrack</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="auth-wrapper" style="display: block; max-width: 800px; margin: 0 auto; padding-top: 4rem;">
        <div class="page-header">
            <h1>Add Equipment</h1>
            <a href="index.jsp" class="back-btn">← Home</a>
        </div>
        <div class="card" style="cursor: default;">
            <form action="equipment" method="POST">
                <div class="form-group">
                    <label for="name">Equipment Name</label>
                    <input type="text" id="name" name="name" required>
                </div>
                <div class="form-group">
                    <label for="serialNumber">Serial Number</label>
                    <input type="text" id="serialNumber" name="serialNumber" required>
                </div>
                <div class="form-group">
                    <label for="purchaseDate">Purchase Date</label>
                    <input type="date" id="purchaseDate" name="purchaseDate">
                </div>
                <div class="form-group">
                    <label for="lastCalibrationDate">Last Calibration Date</label>
                    <input type="date" id="lastCalibrationDate" name="lastCalibrationDate">
                </div>
                <div class="form-group">
                    <label for="maintenanceCategory">Maintenance Category</label>
                    <select id="maintenanceCategory" name="maintenanceCategory">
                        <option value="STANDARD">Standard (6 Months)</option>
                        <option value="URGENT">Urgent (2 Months)</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="status">Status</label>
                    <select id="status" name="status">
                        <option value="Active">Active</option>
                        <option value="Maintenance">Maintenance</option>
                        <option value="Calibration Due">Calibration Due</option>
                        <option value="Decommissioned">Decommissioned</option>
                    </select>
                </div>
                <button type="submit" class="btn">Save to Vault 🔒</button>
            </form>
            <a href="equipment?view=list" class="view-list">View Registered Equipment →</a>
        </div>
    </div>
</body>
</html>
