package app.soap;

import app.ejb.HospitalTechnicianEJB;
import app.model.HospitalTechnician;
import app.rest.ResponseStatus;
import app.rest.SuccessError;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@Stateless
@WebService(serviceName = "TechnicianSoapService")
public class TechnicianSoapApi {

    @EJB
    private HospitalTechnicianEJB technicianEJB;

    @WebMethod
    public ResponseStatus save(@WebParam(name = "technician") HospitalTechnician technician) {
        try {
            technicianEJB.save(technician);
            return new ResponseStatus(SuccessError.SUCCESS, "Technician saved successfully");
        } catch (Exception e) {
            return new ResponseStatus(SuccessError.ERROR, e.getMessage());
        }
    }

    @WebMethod
    public HospitalTechnician find(@WebParam(name = "id") Long id) {
        try {
            return technicianEJB.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    @WebMethod
    public TechnicianWrapper list() {
        try {
            return new TechnicianWrapper(technicianEJB.findAll());
        } catch (Exception e) {
            return new TechnicianWrapper();
        }
    }

    @WebMethod
    public ResponseStatus delete(@WebParam(name = "id") Long id) {
        try {
            technicianEJB.delete(id);
            return new ResponseStatus(SuccessError.SUCCESS, "Technician deleted successfully");
        } catch (Exception e) {
            return new ResponseStatus(SuccessError.ERROR, e.getMessage());
        }
    }
}
