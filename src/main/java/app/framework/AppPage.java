package app.framework;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

@ApplicationScoped
public class AppPage implements Serializable {

    @Inject
    private Cohort12Framework framework;

    protected void display(HttpServletRequest request, HttpServletResponse response, String pageContent)
        throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>VitalTrack | Advanced Medical Logistics</title>");
        out.println("<link href='https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap' rel='stylesheet'>");
        out.println("<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css'>");
        out.println("<link rel='stylesheet' href='" + request.getContextPath() + "/css/style.css'>");

        out.println("</head>");
        out.println("<body class='app-layout'>");

        /* SIDEBAR INCLUSION */
        try {
            request.getRequestDispatcher("/sidebar.jsp").include(request, response);
        } catch (ServletException e) {
            throw new IOException(e);
        }

        out.println("<main class='main-content'>");
        out.println(pageContent);
        out.println("</main>");

        out.println("</body>");
        out.println("</html>");
    }
}
