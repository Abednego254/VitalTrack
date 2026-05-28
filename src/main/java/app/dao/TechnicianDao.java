package app.dao;

import app.model.HospitalTechnician;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TechnicianDao extends GenericDao<HospitalTechnician, Long> {
}