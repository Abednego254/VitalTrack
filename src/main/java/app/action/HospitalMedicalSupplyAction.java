package app.action;

import app.ejb.HospitalMedicalSupplyEJB;
import app.model.HospitalMedicalSupply;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/medicalsupply")
public class HospitalMedicalSupplyAction extends HospitalBaseAction<HospitalMedicalSupply> {

    @EJB
    private HospitalMedicalSupplyEJB supplyEJB;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<HospitalMedicalSupply> items = supplyEJB.findAll();
            request.setAttribute("items", items);

            String view = request.getParameter("view");
            if ("list".equals(view)) {
                request.getRequestDispatcher("/medicalsupply-list.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/medicalsupply.jsp").forward(request, response);
            }
        } catch (Exception e) {
            throw new ServletException("Supply EJB had an accident: " + e.getMessage(), e);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HospitalMedicalSupply supply = new HospitalMedicalSupply();
            supply.setName(request.getParameter("name"));
            supply.setCategory(request.getParameter("category"));
            supply.setUnitOfMeasure(request.getParameter("unitOfMeasure"));

            String qty = request.getParameter("quantity");
            supply.setQuantity(qty != null && !qty.isEmpty() ? Integer.parseInt(qty) : 0);

            String reorder = request.getParameter("reorderLevel");
            supply.setReorderLevel(reorder != null && !reorder.isEmpty() ? Integer.parseInt(reorder) : 10);

            String expiryDate = request.getParameter("expiryDate");
            if (expiryDate != null && !expiryDate.isEmpty()) {
                supply.setExpiryDate(java.sql.Date.valueOf(expiryDate));
            }

            supplyEJB.save(supply);
            response.sendRedirect(request.getContextPath() + "/medicalsupply?view=list");
        } catch (Exception e) {
            throw new ServletException("Supply EJB had an accident: " + e.getMessage(), e);
        }
    }
}
