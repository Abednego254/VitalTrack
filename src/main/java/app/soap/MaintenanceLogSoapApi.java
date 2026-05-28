package app.soap;

import app.ejb.MaintenanceLogEJB;
import app.model.MaintenanceLog;
import app.rest.*;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.jws.*;

@Stateless
@WebService(serviceName = "MaintenanceLogSoapService")
public class MaintenanceLogSoapApi {

    @EJB
    private MaintenanceLogEJB maintenanceEJB;

    @WebMethod
    public ResponseStatus save(@WebParam(name = "log") MaintenanceLog log) {
        try {
            maintenanceEJB.save(log);
            return new ResponseStatus(SuccessError.SUCCESS, "Log saved successfully");
        } catch (Exception e) {
            return new ResponseStatus(SuccessError.ERROR, e.getMessage());
        }
    }

    @WebMethod
    public MaintenanceLog find(@WebParam(name = "id") Long id) {
        try {
            return maintenanceEJB.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    @WebMethod
    public MaintenanceLogWrapper list() {
        try {
            return new MaintenanceLogWrapper(maintenanceEJB.findAll());
        } catch (Exception e) {
            return new MaintenanceLogWrapper();
        }
    }

    @WebMethod
    public ResponseStatus delete(@WebParam(name = "id") Long id) {
        try {
            maintenanceEJB.delete(id);
            return new ResponseStatus(SuccessError.SUCCESS, "Log deleted successfully");
        } catch (Exception e) {
            return new ResponseStatus(SuccessError.ERROR, e.getMessage());
        }
    }
}
