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
    private Cohort12Framework framework;

    @Inject
    private AppPage appPage;

    @Override
    public void init() {
        ActionRegistry.scanAndRegister("app.action");
    }

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

        try {
            Object actionCtxInstance = CDI.current()
                .select(actionMapMatch.getActionMap().getAction())
                .get();

            Object[] argsParams = ActionParamBinder.bind(actionMapMatch.getActionMap(), req,
                resp, actionMapMatch.getPathVariables());

            ActionResponse actionResponse = (ActionResponse) actionMapMatch
                .getActionMap()
                .getMethod()
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