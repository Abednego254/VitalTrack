package app.action;

import jakarta.servlet.http.HttpServlet;
import java.lang.reflect.ParameterizedType;

public class HospitalBaseAction<T> extends HttpServlet {
    protected final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public HospitalBaseAction() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass()
        .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    // [CONCEPT: Separation of Concerns]
    // The Waiter (Servlet) no longer has "Storage Power" (JDBC logic).
    // All database connections and SQL generation have been moved to the
    // GenericDao class (The Storage Room Worker).
    // Subclasses will override doGet/doPost to call their respective EJBs!
}
