package app.ejb;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

@Singleton
@Startup
public class HospitalConfigEJB {
    private String hospitalName;
    private String version;

    @PostConstruct
    public void onStartup() {
        this.hospitalName = "VitalTrack Medical Centre";
        this.version = "1.0.0";

        System.out.println("==========================================");
        System.out.println(" HOSPITAL DIRECTOR (Singleton EJB) REPORTING!");
        System.out.println(" Hospital: " + hospitalName);
        System.out.println(" Version : " + version);
        System.out.println(" Status  : ALL SYSTEMS READY");
        System.out.println("==============================================");
    }

    @PreDestroy
    public void onShutdown() {
        System.out.println("===================================================");
        System.out.println("HOSPITAL DIRECTOR (Singleton EJB) SIGNING OUT!");
        System.out.println("====================================================");
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public String getVersion() {
        return version;
    }
}