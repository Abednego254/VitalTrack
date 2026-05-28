package app.dao;

import app.model.HospitalMedicalSupply;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MedicalSupplyDao extends GenericDao<HospitalMedicalSupply, Long> {
}
