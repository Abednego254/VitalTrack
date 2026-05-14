<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Set Your Password | VitalTrack</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="auth-wrapper">
    <div class="auth-card">
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
