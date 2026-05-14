<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login - VitalTrack</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="auth-wrapper">

<div class="auth-card">
    <h2>Welcome to VitalTrack</h2>
    
    <% if (request.getAttribute("error") != null) { %>
        <div class="error-message"><%= request.getAttribute("error") %></div>
    <% } %>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <div class="form-group">
            <label for="username">Username</label>
            <input type="text" id="username" name="username" required>
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" required>
        </div>
        <button type="submit" class="btn">Login</button>
    </form>
    
    <div class="login-footer">
        <p>Restricted Area. Authorized Personnel Only.</p>
    </div>
</div>

</body>
</html>
