package app.soap;

import app.ejb.HospitalEquipmentEJB;
import app.model.HospitalEquipment;
import app.rest.ResponseStatus;
import app.rest.SuccessError;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import java.util.List;

@Stateless
@WebService(serviceName = "EquipmentSoapService")
public class EquipmentSoapApi {

    @EJB
    private HospitalEquipmentEJB equipmentEJB;

    @WebMethod
    public ResponseStatus save(@WebParam(name = "equipment") HospitalEquipment equipment) {
        try {
            equipmentEJB.save(equipment);
            return new ResponseStatus(SuccessError.SUCCESS, "Equipment saved successfully");
        } catch (Exception e) {
            return new ResponseStatus(SuccessError.ERROR, e.getMessage());
        }
    }

    @WebMethod
    public HospitalEquipment find(@WebParam(name = "id") Long id) {
        try {
            return equipmentEJB.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    @WebMethod
    public EquipmentWrapper list() {
        try {
            return new EquipmentWrapper(equipmentEJB.findAll());
        } catch (Exception e) {
            return new EquipmentWrapper();
        }
    }

    @WebMethod
    public ResponseStatus delete(@WebParam(name = "id") Long id) {
        try {
            equipmentEJB.delete(id);
            return new ResponseStatus(SuccessError.SUCCESS, "Equipment deleted successfully");
        } catch (Exception e) {
            return new ResponseStatus(SuccessError.ERROR, e.getMessage());
        }
    }
}
