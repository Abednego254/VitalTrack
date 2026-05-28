package app.dao;

import app.model.HospitalNurse;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NurseDao extends GenericDao<HospitalNurse, Long> {
}
