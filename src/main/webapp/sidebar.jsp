<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<aside class="sidebar">
    <a href="index.jsp" class="sidebar-logo">
        VitalTrack
    </a>
    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/vital/home/index" class="sidebar-link">Dashboard</a>
        <a href="${pageContext.request.contextPath}/vital/equipment/list" class="sidebar-link">Equipment</a>
        <c:if test="${role == 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/vital/medicalsupply/list" class="sidebar-link">Supplies</a>
            <a href="${pageContext.request.contextPath}/vital/technician/list" class="sidebar-link">Technicians</a>
            <a href="${pageContext.request.contextPath}/vital/audit-trail/list" class="sidebar-link">Security Logs</a>
        </c:if>
        <a href="${pageContext.request.contextPath}/vital/maintenancelog/add" class="sidebar-link">Maintenance</a>
        <a href="${pageContext.request.contextPath}/vital/maintenancelog/list" class="sidebar-link">History</a>
    </nav>
    <div style="margin-top: auto; padding-top: 2rem; border-top: 1px solid var(--border);">
        <a href="logout" class="sidebar-link" style="color: var(--danger)">Logout</a>
    </div>
</aside>
