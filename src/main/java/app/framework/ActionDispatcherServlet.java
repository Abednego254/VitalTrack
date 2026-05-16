package app.framework;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/vital/*")
public class ActionDispatcherServlet extends HttpServlet {

    @Inject
    private VitalTrackFramework framework;

    @Inject
    private AppPage appPage;

    @Override
    public void init() {
        ActionRegistry.scanAndRegister("app.action");
    }

    // We override service() because it catches every type of HTTP method (GET, POST, DELETE, etc.)
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        String requestPath = req.getPathInfo();
        String httpMethod = req.getMethod();

        ActionMapMatch actionMapMatch = ActionRegistry.findMatch(requestPath, httpMethod);

        if (actionMapMatch == null) {
            resp.sendError(404);
            return;
        }

        Class<?> actionClass = actionMapMatch.getActionMap().getAction();
        Action actionAnnotation = actionClass.getAnnotation(Action.class);

        if (actionAnnotation != null && "ADMIN".equals(actionAnnotation.role())) {
            HttpSession session = req.getSession(false);
            String userRole = (session != null) ? (String) session.getAttribute("role") : null;

            if (!"ADMIN".equals(userRole)) {
                resp.sendError(403, "Access Denied: Admin Rights Required");
                return;
            }
        }

        try {
            Object actionCtxInstance = CDI.current()
                .select(actionMapMatch.getActionMap().getAction())
                .get();

                // looks at the method parameters and fills them with data from the HTTP request
            Object[] argsParams = ActionParamBinder.bind(actionMapMatch.getActionMap(), req,
                resp, actionMapMatch.getPathVariables());

            ActionResponse actionResponse = (ActionResponse) actionMapMatch
                .getActionMap()
                .getMethod()
                // It executes the method found in your Action class (like save() or list()) and receives an ActionResponse.
                .invoke(actionCtxInstance, argsParams);

            String displayContent;
            if (actionResponse.getResponseText() != null) {
                displayContent = actionResponse.getResponseText();
                if (displayContent.startsWith("forward:")) {
                    req.getRequestDispatcher(displayContent.substring(8)).forward(req, resp);
                    return;
                }
            } else
                displayContent = framework.htmlTable(actionResponse.getResponseClazz(),
                    actionResponse.getResponseDataList());

            appPage.display(req, resp, displayContent);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}