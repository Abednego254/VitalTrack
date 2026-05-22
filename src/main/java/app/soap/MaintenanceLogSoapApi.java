package app.soap;

import app.ejb.HospitalMaintenanceLogEJB;
import app.model.HospitalMaintenanceLog;
import app.rest.ResponseStatus;
import app.rest.SuccessError;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@Stateless
@WebService(serviceName = "MaintenanceLogSoapService")
public class MaintenanceLogSoapApi {

    @EJB
    private HospitalMaintenanceLogEJB maintenanceEJB;

    @WebMethod
    public ResponseStatus save(@WebParam(name = "log") HospitalMaintenanceLog log) {
        try {
            maintenanceEJB.save(log);
            return new ResponseStatus(SuccessError.SUCCESS, "Log saved successfully");
        } catch (Exception e) {
            return new ResponseStatus(SuccessError.ERROR, e.getMessage());
        }
    }

    @WebMethod
    public HospitalMaintenanceLog find(@WebParam(name = "id") Long id) {
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
