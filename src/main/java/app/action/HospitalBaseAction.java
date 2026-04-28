package app.action;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

public class HospitalBaseAction<T> extends HttpServlet {
    private final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public HospitalBaseAction() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass()
        .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("Hello! I am the super waiter for "+ entityClass.getSimpleName());
        response.getWriter().println("I am ready to show you the list of items");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            T entity = entityClass.getDeclaredConstructor().newInstance();
            BeanUtils.populate(entity, request.getParameterMap());
            response.getWriter().println("I have found a new "+ entityClass.getSimpleName() +" !");
            response.getWriter().println("Saving to database ... Coming soon!");

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ServletException("The super waiter had a little accident!!"+ e.getMessage());
        }
    }
}
