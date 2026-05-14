<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Medical Supply | VitalTrack</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="app-layout">
    <jsp:include page="sidebar.jsp" />
    <main class="main-content">
        <div class="page-header">
            <h1>Add Medical Supply</h1>
            <a href="index.jsp" class="back-btn">← Home</a>
        </div>
        <div class="card" style="cursor: default;">
            <form action="medicalsupply" method="POST">
                <div class="form-group">
                    <label for="name">Supply Name</label>
                    <input type="text" id="name" name="name" placeholder="e.g. Surgical Mask" required>
                </div>
                <div class="form-group">
                    <label for="category">Category</label>
                    <input type="text" id="category" name="category" placeholder="e.g. PPE" required>
                </div>
                <div class="form-group">
                    <label for="quantity">Initial Quantity</label>
                    <input type="number" id="quantity" name="quantity" value="0" required>
                </div>
                <div class="form-group">
                    <label for="unitOfMeasure">Unit of Measure</label>
                    <input type="text" id="unitOfMeasure" name="unitOfMeasure" placeholder="e.g. Boxes, Pieces" required>
                </div>
                <div class="form-group">
                    <label for="reorderLevel">Reorder Level</label>
                    <input type="number" id="reorderLevel" name="reorderLevel" value="10" required>
                </div>
                <div class="form-group">
                    <label for="expiryDate">Expiry Date</label>
                    <input type="date" id="expiryDate" name="expiryDate">
                </div>
                <button type="submit" class="btn">Save to Vault 🔒</button>
            </form>
            <a href="medicalsupply?view=list" class="view-list">View Supply Inventory →</a>
        </div>
    </main>
</body>
</html>
