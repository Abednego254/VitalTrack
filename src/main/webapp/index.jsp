<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>VitalTrack | Medical Logistics</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">
    <style>
        :root {
            --primary: #2563eb;
            --secondary: #64748b;
            --success: #10b981;
            --warning: #f59e0b;
            --danger: #ef4444;
            --background: #f8fafc;
            --card: #ffffff;
            --text: #1e293b;
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Inter', sans-serif;
        }

        body {
            background-color: var(--background);
            color: var(--text);
            min-height: 100vh;
        }

        .navbar {
            background: rgba(255, 255, 255, 0.8);
            backdrop-filter: blur(12px);
            border-bottom: 1px solid rgba(0, 0, 0, 0.1);
            padding: 1rem 2rem;
            position: sticky;
            top: 0;
            z-index: 100;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .navbar h1 {
            font-size: 1.5rem;
            font-weight: 700;
            color: var(--primary);
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .nav-links {
            display: flex;
            gap: 2rem;
        }

        .nav-links a {
            text-decoration: none;
            color: var(--secondary);
            font-weight: 500;
            transition: color 0.3s;
        }

        .nav-links a:hover {
            color: var(--primary);
        }

        .hero {
            padding: 4rem 2rem;
            text-align: center;
            background: linear-gradient(135deg, #eff6ff 0%, #ffffff 100%);
        }

        .hero h2 {
            font-size: 2.5rem;
            margin-bottom: 1rem;
            color: var(--text);
        }

        .hero p {
            color: var(--secondary);
            font-size: 1.1rem;
            max-width: 600px;
            margin: 0 auto;
        }

        .container {
            max-width: 1200px;
            margin: 2rem auto;
            padding: 0 1rem;
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 2rem;
        }

        .card {
            background: var(--card);
            padding: 2rem;
            border-radius: 1rem;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
            transition: transform 0.3s, box-shadow 0.3s;
            cursor: pointer;
            border: 1px solid rgba(0, 0, 0, 0.05);
        }

        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
        }

        .card-icon {
            width: 48px;
            height: 48px;
            background: #dbeafe;
            border-radius: 0.75rem;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 1.5rem;
            color: var(--primary);
            font-size: 1.5rem;
        }

        .card h3 {
            margin-bottom: 0.5rem;
            font-size: 1.25rem;
        }

        .card p {
            color: var(--secondary);
            font-size: 0.95rem;
            line-height: 1.5;
        }

        .badge {
            display: inline-block;
            padding: 0.25rem 0.75rem;
            border-radius: 9999px;
            font-size: 0.75rem;
            font-weight: 600;
            margin-top: 1rem;
        }

        .badge-success { background: #d1fae5; color: #065f46; }
        .badge-warning { background: #fef3c7; color: #92400e; }

        footer {
            margin-top: 4rem;
            padding: 2rem;
            text-align: center;
            color: var(--secondary);
            font-size: 0.9rem;
            border-top: 1px solid rgba(0, 0, 0, 0.05);
        }
    </style>
</head>
<body>

    <nav class="navbar">
        <h1><span>🚑</span> VitalTrack</h1>
        <div class="nav-links">
            <a href="index.jsp">Dashboard</a>
            <a href="equipment">Equipment</a>
            <a href="supplies">Supplies</a>
            <a href="maintenance">Maintenance</a>
        </div>
    </nav>

    <header class="hero">
        <h2>Hospital Command Center</h2>
        <p>Real-time monitoring of medical assets, supply levels, and technician availability.</p>
    </header>

    <main class="container">
        <!-- [CONCEPT: EL (Expression Language)] -->
        <!-- In the future, we will use ${variable} to show real data! -->
        
        <div class="card" onclick="location.href='equipment'">
            <div class="card-icon">⚙️</div>
            <h3>Medical Equipment</h3>
            <p>Track calibration status and maintenance cycles for all medical devices.</p>
            <span class="badge badge-success">Online</span>
        </div>

        <div class="card" onclick="location.href='supplies'">
            <div class="card-icon">📦</div>
            <h3>Medical Supplies</h3>
            <p>Monitor inventory levels and expiration dates for consumable stocks.</p>
            <span class="badge badge-success">Stock OK</span>
        </div>

        <div class="card" onclick="location.href='technicians'">
            <div class="card-icon">👨‍🔧</div>
            <h3>Technicians</h3>
            <p>View availability and specialization of service personnel.</p>
            <span class="badge badge-warning">2 On-Call</span>
        </div>

        <div class="card" onclick="location.href='maintenance'">
            <div class="card-icon">📝</div>
            <h3>Maintenance Logs</h3>
            <p>Historical records of all equipment servicing and repairs.</p>
            <span class="badge badge-success">Secure</span>
        </div>
    </main>

    <footer>
        &copy; 2026 VitalTrack Medical Logistics System. Developed for Jakarta EE Training.
    </footer>

</body>
</html>
