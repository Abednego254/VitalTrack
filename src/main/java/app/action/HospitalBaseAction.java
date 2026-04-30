package app.action;

import app.utility.DataSourceHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;

public class HospitalBaseAction<T> extends HttpServlet {
    private final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public HospitalBaseAction() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass()
        .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @jakarta.inject.Inject
    private DataSourceHelper dataSourceHelper;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String view = request.getParameter("view");
            String entityName = entityClass.getSimpleName().toLowerCase().replace("hospital", "");
            
            if ("list".equals(view)) {
                // GALLERY ROOM: Show the list
                java.util.List<T> items = findAll();
                request.setAttribute("items", items);
                request.getRequestDispatcher("/" + entityName + "-list.jsp").forward(request, response);
            } else {
                // SIGN-UP ROOM: Show the form
                request.getRequestDispatcher("/" + entityName + ".jsp").forward(request, response);
            }

        } catch (Exception e) {
            throw new ServletException("The super waiter couldn't show you the room!! " + e.getMessage());
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            T entity = entityClass.getDeclaredConstructor().newInstance();
            BeanUtils.populate(entity, request.getParameterMap());
            
            // Save to the vault
            save(entity);
            
            // Go straight to the Gallery Room to see the result!
            String entityName = entityClass.getSimpleName().toLowerCase().replace("hospital", "");
            response.sendRedirect(request.getContextPath() + "/" + entityName + "?view=list");

        } catch (Exception e) {
            throw new ServletException("The super waiter had a little accident!! " + e.getMessage());
        }
    }

    /**
     * [CONCEPT: GENERIC PERSISTENCE]
     * This is the Waiter's new "Storage Power".
     */
    private void save(T entity) throws Exception {
        // 1. Get all the details from the "Sticky Note" (the bean)
        Map<String, String> properties = BeanUtils.describe(entity);
        
        // 2. Remove the "class" label (we don't save that to the DB)
        properties.remove("class");
        properties.remove("id"); // ID is handled by the DB (Auto-increment)

        // 3. Build the Laser Pen command (SQL)
        String tableName = entityClass.getSimpleName();
        String columns = String.join(", ", properties.keySet());
        String placeholders = properties.keySet().stream().map(k -> "?").collect(java.util.stream.Collectors.joining(", "));
        
        String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";

        // 4. Open the vault and write!
        try (Connection conn = dataSourceHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            int index = 1;
            for (String value : properties.values()) {
                stmt.setObject(index++, value);
            }
            
            stmt.executeUpdate();
            System.out.println("********** WAITER: Saved " + tableName + " to the vault! **********");
        }
    }

    /**
     * [CONCEPT: GENERIC FETCHING]
     * This is how the Waiter reads the whole drawer!
     */
    public java.util.List<T> findAll() throws Exception {
        java.util.List<T> items = new java.util.ArrayList<>();
        String sql = "SELECT * FROM " + entityClass.getSimpleName();

        try (Connection conn = dataSourceHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = stmt.executeQuery()) {

            // Get the list of all "compartment" names (columns)
            java.sql.ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                // 1. Create a fresh, empty sticky note (Bean)
                T entity = entityClass.getDeclaredConstructor().newInstance();

                // 2. Fill it up with data from the vault
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(columnName);
                    
                    // Put the data into the right compartment
                    try {
                        BeanUtils.setProperty(entity, columnName, value);
                    } catch (Exception e) {
                        // Some fields might not match perfectly, that's okay!
                    }
                }
                
                // 3. Add it to our silver tray
                items.add(entity);
            }
        }
        return items;
    }
}
