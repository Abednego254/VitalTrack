<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Medical Supply | VitalTrack</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">
    <style>
        :root { --primary: #10b981; --background: #f8fafc; --card: #ffffff; --text: #1e293b; --border: #e2e8f0; }
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
            <h1>Add Medical Supply</h1>
            <a href="index.jsp" class="back-btn">← Home</a>
        </div>
        <div class="card">
            <form action="supplies" method="POST">
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
                <button type="submit" class="btn">Save to Vault 🔒</button>
            </form>
            <a href="supplies?view=list" class="view-list">View Supply Inventory →</a>
        </div>
    </div>
</body>
</html>
