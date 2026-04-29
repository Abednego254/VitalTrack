package app.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.*;






@WebFilter(urlPatterns = {"/maintenance", "/technicians"})
public class HospitalAuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        boolean isLoggedIn = (session != null && session.getAttribute("username") != null);
        if(isLoggedIn){
            filterChain.doFilter(request, response);
        } else {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        }
    }

    @Override
    public void destroy() {}
}