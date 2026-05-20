package app.framework;

import app.utility.helper.ClassScanner;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class VitalTrackFramework {

    @Inject
    private ClassScanner clazzScanner;

    @Inject
    private app.dao.HospitalEquipmentDao equipmentDao;

    private Map<String, List<SelectBox>> formSelections = new HashMap<>();

    @PostConstruct
    public void init() {
        System.out.println("****************VitalTrackFramework Contextual Instance created ********");
        resetFormSelections();
    }

    public String htmlForm(Class<?> clazz){

        if (!clazz.isAnnotationPresent(VitalTrackForm.class))
            return "";

        VitalTrackForm formAnnot = clazz.getAnnotation(VitalTrackForm.class);

        StringBuilder formBuilder = new StringBuilder();
        formBuilder.append("<header class='page-header'>");
        formBuilder.append("<h1>").append(formAnnot.label()).append(" Registration</h1>");
        formBuilder.append("<p>Please provide the required details below to register a new entry.</p>");
        formBuilder.append("</header>");

        formBuilder.append("<div class='container'>");
        formBuilder.append("<div class='card glass'>");
        formBuilder.append("<form method='").append(formAnnot.method())
            .append("' action='")
            .append(ActionMap.APP_PATH)
            .append(formAnnot.actionUrl()).append("'>");

        formBuilder.append("<div class='form-grid'>");
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(VitalTrackFormField.class))
                continue;

            VitalTrackFormField fieldInfo = field.getAnnotation(VitalTrackFormField.class);
            formBuilder.append("<div class='form-group'>");
            formBuilder.append("<label>").append(fieldInfo.label()).append("</label>");
            if (!fieldInfo.select().equalsIgnoreCase("")
                && formSelections.containsKey(fieldInfo.select())) {
                    formBuilder.append("<select name='").append(field.getName()).append("' required>");

                    formSelections.get(fieldInfo.select()).forEach(formSelection ->
                        formBuilder.append("<option value='").append(formSelection.getValue()).append("'>")
                        .append(formSelection.getName()).append("</option>"));

                formBuilder.append("</select>");

            } else {
                formBuilder.append("<input type='").append(fieldInfo.type().isEmpty() ? "text" : fieldInfo.type()).append("' name='")
                    .append(fieldInfo.name().isEmpty() ? field.getName() : fieldInfo.name())
                    .append("' placeholder='").append(fieldInfo.placeholder())
                    .append("' required />");
            }
            formBuilder.append("</div>");
        }
        formBuilder.append("</div>");

        //reset form selection
        resetFormSelections();

        formBuilder.append("<div style='margin-top: 2rem;'>");
        formBuilder.append("<button type='submit' class='btn btn-primary'>Register Entry</button>");
        formBuilder.append("<button type='reset' class='btn btn-outline' style='margin-left: 1rem;'>Clear Form</button>");
        formBuilder.append("</div>");
        formBuilder.append("</form>");

        formBuilder.append("</div>");
        formBuilder.append("</div>");

        return formBuilder.toString();
    }

    public String htmlTable(Class<?> clazz, List<?> tableData) {

        if (!clazz.isAnnotationPresent(VitalTrackTable.class))
            return "";

        VitalTrackTable cohort12Table = clazz.getAnnotation(VitalTrackTable.class);

        StringBuilder tableBuilder = new StringBuilder();

        tableBuilder.append("<header class='page-header'>");
        tableBuilder.append("<h1>").append(cohort12Table.label()).append(" Registry</h1>");
        tableBuilder.append("<p>Securely manage and track all system records.</p>");
        tableBuilder.append("</header>");

        tableBuilder.append("<div class='container'>");
        tableBuilder.append("<div class='card glass'>");
        
        tableBuilder.append("<div style='display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;'>");
        tableBuilder.append("<h2 style='margin:0;'>Registered List</h2>");
        if (!cohort12Table.addLink().equalsIgnoreCase(""))
            tableBuilder.append("<a href=\"")
                .append(ActionMap.APP_PATH)
                .append(cohort12Table.addLink())
                .append("\" class='btn btn-primary'>&#43; Add New Record</a>");
        tableBuilder.append("</div>");

        tableBuilder.append("<div class='table-responsive'>");
        tableBuilder.append("<table class='data-table'>");

        class ColMetaData {
            final String fieldName;
            final String columnName;

            ColMetaData(String fieldName, String columnName){
                this.fieldName = fieldName;
                this.columnName = columnName;
            }

        }

        List<ColMetaData> colsMedaData = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(VitalTrackTableCol.class))
                continue;

            VitalTrackTableCol tableCol = field.getAnnotation(VitalTrackTableCol.class);

            colsMedaData.add(new ColMetaData(field.getName(), tableCol.label()));
        }

        tableBuilder.append("<thead><tr>");
        for (ColMetaData colMedaData : colsMedaData)
            tableBuilder.append("<th>").append(colMedaData.columnName).append("</th>");

        tableBuilder.append("<th style='text-align: right;'>Actions</th>");
        tableBuilder.append("</tr></thead>");
        tableBuilder.append("<tbody>");

        for (Object data : tableData) {
            tableBuilder.append("<tr>");
            for (ColMetaData colMedaData : colsMedaData) {
                try {
                    Field field = findField(data.getClass(), colMedaData.fieldName);
                    field.setAccessible(true);
                    Object val = field.get(data);
                    String displayVal = (val == null) ? "-" : val.toString();
                    
                    if (displayVal.equalsIgnoreCase("ACTIVE") || displayVal.equalsIgnoreCase("ONLINE")) {
                        tableBuilder.append("<td><span class='badge badge-success'>").append(displayVal).append("</span></td>");
                    } else if (displayVal.equalsIgnoreCase("PENDING") || displayVal.equalsIgnoreCase("WARNING")) {
                        tableBuilder.append("<td><span class='badge badge-warning'>").append(displayVal).append("</span></td>");
                    } else if (displayVal.equalsIgnoreCase("CRITICAL") || displayVal.equalsIgnoreCase("OFFLINE")) {
                        tableBuilder.append("<td><span class='badge badge-danger'>").append(displayVal).append("</span></td>");
                    } else {
                        tableBuilder.append("<td>").append(displayVal).append("</td>");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
            tableBuilder.append("<td style='text-align: right;'>");

            try {
                Field idField = findField(clazz, "id");
                idField.setAccessible(true);
                Object id = idField.get(data);

                /* EDIT BUTTON (Placeholder for now) */
                tableBuilder.append("<a href='#' class='icon-btn' title='Edit' style='background: #3b82f6; color: white;'>");
                tableBuilder.append("<i class='fa-solid fa-pen'></i>");
                tableBuilder.append("</a>");

                /* DELETE BUTTON */
                tableBuilder.append("<a href='")
                    .append(ActionMap.APP_PATH)
                    .append(cohort12Table.deleteLink())
                    .append("/")
                    .append(id)
                    .append("' class='icon-btn' title='Delete' style='background: #ef4444; color: white; margin-left: 0.5rem;' onclick='return confirm(\"Confirm permanent deletion of this record?\")'>");
                tableBuilder.append("<i class='fa-solid fa-trash'></i>");
                tableBuilder.append("</a>");

            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            tableBuilder.append("</td>");

            tableBuilder.append("</tr>");

        }
        tableBuilder.append("</tbody>");
        tableBuilder.append("</table>");
        tableBuilder.append("</div>");

        tableBuilder.append("</div>");
        tableBuilder.append("</div>");

        return tableBuilder.toString();

    }

    public String generateMenuItem(){
        return ClassScanner.scanForAction("app.action").stream()
            .map(clazz -> clazz.getAnnotation(Action.class))
            .filter(Objects::nonNull)
            .filter(Action::showLink)
            .sorted(Comparator.comparingInt(Action::linkPosition))
            .map(annotation -> "<a href='" + ActionMap.APP_PATH + annotation.value() + "/" + annotation.pageLink() + "'>"
                    + annotation.label() + "</a>")
            .collect(Collectors.joining("\n"));
    }

    public Map<String, List<SelectBox>> getFormSelections() {
        return formSelections;
    }

    public void setFormSelections(Map<String, List<SelectBox>> formSelections) {
        this.formSelections = formSelections;
    }

    public void resetFormSelections(){
        formSelections = new HashMap<>();
        List<SelectBox> genderSelections = new ArrayList<>();
        genderSelections.add(SelectBox.builder()
                .value("Male")
                .name("Male")
                .build());
        genderSelections.add(SelectBox.builder()
                .value("Female")
                .name("Female")
                .build());
        genderSelections.add(SelectBox.builder()
                .value("Non-Binary")
                .name("Non-Binary")
                .build());

        formSelections.put("gender", genderSelections);

        List<SelectBox> maintenanceSelections = new ArrayList<>();
        maintenanceSelections.add(SelectBox.builder()
                .value("STANDARD")
                .name("Standard Maintenance (6 Months)")
                .build());
        maintenanceSelections.add(SelectBox.builder()
                .value("URGENT")
                .name("Urgent Maintenance (2 Months)")
                .build());
        formSelections.put("maintenanceCategory", maintenanceSelections);

        List<SelectBox> eqStatusSelections = new ArrayList<>();
        eqStatusSelections.add(SelectBox.builder()
                .value("ACTIVE")
                .name("Active")
                .build());
        eqStatusSelections.add(SelectBox.builder()
                .value("FAULTY")
                .name("Faulty")
                .build());
        eqStatusSelections.add(SelectBox.builder()
                .value("UNDER_MAINTENANCE")
                .name("Under Maintenance")
                .build());
        formSelections.put("equipmentStatus", eqStatusSelections);

        List<SelectBox> techStatusSelections = new ArrayList<>();
        techStatusSelections.add(SelectBox.builder()
                .value("ACTIVE")
                .name("Active")
                .build());
        techStatusSelections.add(SelectBox.builder()
                .value("INACTIVE")
                .name("Inactive")
                .build());
        formSelections.put("technicianStatus", techStatusSelections);

        List<SelectBox> eqListSelections = new ArrayList<>();
        try {
            if (equipmentDao != null) {
                equipmentDao.findAll().forEach(eq -> {
                    eqListSelections.add(SelectBox.builder()
                            .value(String.valueOf(eq.getId()))
                            .name(eq.getName() + " (S/N: " + eq.getSerialNumber() + ")")
                            .build());
                });
            }
        } catch (Exception e) {
            System.err.println("Error fetching equipment options for form selections: " + e.getMessage());
        }
        formSelections.put("equipmentId", eqListSelections);
    }

    private Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }
}
