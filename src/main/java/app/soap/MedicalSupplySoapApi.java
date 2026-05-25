package app.soap;

import app.ejb.HospitalMedicalSupplyEJB;
import app.model.HospitalMedicalSupply;
import app.rest.*;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.jws.*;

@Stateless
@WebService(serviceName = "MedicalSupplySoapService")
public class MedicalSupplySoapApi {

    @EJB
    private HospitalMedicalSupplyEJB supplyEJB;

    @WebMethod
    public ResponseStatus save(@WebParam(name = "supply") HospitalMedicalSupply supply) {
        try {
            supplyEJB.save(supply);
            return new ResponseStatus(SuccessError.SUCCESS, "Supply saved successfully");
        } catch (Exception e) {
            return new ResponseStatus(SuccessError.ERROR, e.getMessage());
        }
    }

    @WebMethod
    public HospitalMedicalSupply find(@WebParam(name = "id") Long id) {
        try {
            return supplyEJB.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    @WebMethod
    public MedicalSupplyWrapper list() {
        try {
            return new MedicalSupplyWrapper(supplyEJB.findAll());
        } catch (Exception e) {
            return new MedicalSupplyWrapper();
        }
    }

    @WebMethod
    public ResponseStatus delete(@WebParam(name = "id") Long id) {
        try {
            supplyEJB.delete(id);
            return new ResponseStatus(SuccessError.SUCCESS, "Supply deleted successfully");
        } catch (Exception e) {
            return new ResponseStatus(SuccessError.ERROR, e.getMessage());
        }
    }
}
