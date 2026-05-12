<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Set Your Password | VitalTrack</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">
    <style>
        :root { --primary: #3b82f6; --background: #f8fafc; --card: #ffffff; --text: #1e293b; --border: #e2e8f0; }
        body { font-family: 'Inter', sans-serif; background-color: var(--background); color: var(--text); padding: 2rem; }
        .container { max-width: 450px; margin: 4rem auto; }
        .card { background: var(--card); padding: 2rem; border-radius: 1rem; box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1); }
        .form-group { margin-bottom: 1.5rem; }
        .form-group label { display: block; margin-bottom: 0.5rem; font-weight: 600; }
        .form-group input { width: 100%; padding: 0.75rem; border: 1px solid var(--border); border-radius: 0.5rem; }
        .btn { background: var(--primary); color: white; padding: 0.75rem; border: none; border-radius: 0.5rem; font-weight: 700; cursor: pointer; width: 100%; }
        h1 { color: var(--primary); margin-bottom: 1rem; text-align: center; }
        p { text-align: center; color: #64748b; margin-bottom: 2rem; font-size: 0.9rem; }
    </style>
</head>
<body>
    <div class="container">
        <div class="card">
            <h1>Welcome to VitalTrack</h1>
            <p>Your email has been whitelisted. Please set your security password to continue to your dashboard.</p>
            <form action="set-password" method="POST">
                <div class="form-group">
                    <label for="password">New Password</label>
                    <input type="password" id="password" name="password" required placeholder="Create a strong password">
                </div>
                <div class="form-group">
                    <label for="confirm">Confirm Password</label>
                    <input type="password" id="confirm" name="confirm" required placeholder="Repeat your password">
                </div>
                <button type="submit" class="btn">Establish Account 🛡️</button>
            </form>
        </div>
    </div>
</body>
</html>
