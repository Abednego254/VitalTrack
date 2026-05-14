package app.action;

import app.framework.Action;
import app.framework.ActionGetMethod;
import app.framework.ActionResponse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequestScoped
@Action(value = "home", showLink = false)
public class HomeAction {

    @ActionGetMethod("index")
    public ActionResponse index() throws Exception {
        return new ActionResponse("forward:/index.jsp");
    }
}
