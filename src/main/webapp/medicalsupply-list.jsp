<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Supply Inventory | VitalTrack</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="app-layout">
    <jsp:include page="sidebar.jsp" />
    <main class="main-content">
        <div class="page-header">
            <h1>Supply Inventory</h1>
            <a href="medicalsupply" class="btn" style="width: auto;">+ Add New</a>
        </div>
        <div class="card" style="cursor: default;">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Category</th>
                        <th>Quantity</th>
                        <th>Unit</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${items}" var="item">
                        <tr>
                            <td>${item.id}</td>
                            <td>${item.name}</td>
                            <td>${item.category}</td>
                            <td>
                                <span style="${item.quantity <= item.reorderLevel ? 'color: #ef4444; font-weight: bold;' : ''}">
                                    ${item.quantity}
                                </span>
                            </td>
                            <td>${item.unitOfMeasure}</td>
                            <td>
                                <form action="medicalsupply" method="POST" style="display: flex; gap: 0.5rem; align-items: center;">
                                    <input type="hidden" name="mode" value="consume">
                                    <input type="hidden" name="id" value="${item.id}">
                                    <input type="hidden" name="name" value="${item.name}">
                                    <input type="number" name="consumeQty" value="1" min="1" max="${item.quantity}" style="width: 50px; padding: 0.25rem; border-radius: 0.25rem; border: 1px solid var(--border);">
                                    <button type="submit" style="background: #3b82f6; color: white; border: none; padding: 0.25rem 0.5rem; border-radius: 0.25rem; cursor: pointer; font-size: 0.8rem;">Use</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty items}">
                        <tr><td colspan="5" style="text-align: center;">No supplies found in the vault.</td></tr>
                    </c:if>
                </tbody>
            </table>
            <div style="margin-top: 1.5rem; text-align: center;">
                <a href="index.jsp" class="back-btn">← Back to Dashboard</a>
            </div>
        </div>
    </main>
</body>
</html>
