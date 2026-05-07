package app.action;

import jakarta.servlet.http.HttpServlet;
import java.lang.reflect.ParameterizedType;

public class HospitalBaseAction<T> extends HttpServlet { // web-enabled
    protected final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public HospitalBaseAction() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass()
        .getGenericSuperclass()).getActualTypeArguments()[0];
    }

}
