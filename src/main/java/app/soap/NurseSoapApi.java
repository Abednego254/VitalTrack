package app.soap;

import app.ejb.HospitalNurseEJB;
import app.model.HospitalNurse;
import app.rest.ResponseStatus;
import app.rest.SuccessError;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@Stateless
@WebService(serviceName = "NurseSoapService")
public class NurseSoapApi {

    @EJB
    private HospitalNurseEJB nurseEJB;

    @WebMethod
    public ResponseStatus save(@WebParam(name = "nurse") HospitalNurse nurse) {
        try {
            nurseEJB.save(nurse);
            return new ResponseStatus(SuccessError.SUCCESS, "Nurse saved successfully");
        } catch (Exception e) {
            return new ResponseStatus(SuccessError.ERROR, e.getMessage());
        }
    }

    @WebMethod
    public HospitalNurse find(@WebParam(name = "id") Long id) {
        try {
            return nurseEJB.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    @WebMethod
    public NurseWrapper list() {
        try {
            return new NurseWrapper(nurseEJB.findAll());
        } catch (Exception e) {
            return new NurseWrapper();
        }
    }

    @WebMethod
    public ResponseStatus delete(@WebParam(name = "id") Long id) {
        try {
            nurseEJB.delete(id);
            return new ResponseStatus(SuccessError.SUCCESS, "Nurse deleted successfully");
        } catch (Exception e) {
            return new ResponseStatus(SuccessError.ERROR, e.getMessage());
        }
    }
}
