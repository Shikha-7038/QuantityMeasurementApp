package QuantityMeasurementApp.controller;
import QuantityMeasurementApp.dto.QuantityDTO;
import QuantityMeasurementApp.service.QuantityMeasurementService;
public class QuantityMeasurementController {
    private final QuantityMeasurementService service;
    public QuantityMeasurementController(QuantityMeasurementService service) {
        this.service = service;
    }
    // ================= ADD =================
    public QuantityDTO add(double v1, String u1, String type,
                           double v2, String u2) {

        QuantityDTO q1 = new QuantityDTO(v1, u1, type);
        QuantityDTO q2 = new QuantityDTO(v2, u2, type);

        return service.add(q1, q2);
    }
    // ================= SUBTRACT =================
    public QuantityDTO subtract(double v1, String u1, String type,
                                double v2, String u2) {

        QuantityDTO q1 = new QuantityDTO(v1, u1, type);
        QuantityDTO q2 = new QuantityDTO(v2, u2, type);

        return service.subtract(q1, q2);
    }
    // ================= DIVIDE =================
    public QuantityDTO divide(double v1, String u1, String type,
                              double v2, String u2) {

        QuantityDTO q1 = new QuantityDTO(v1, u1, type);
        QuantityDTO q2 = new QuantityDTO(v2, u2, type);

        return service.divide(q1, q2);
    }
    // ================= CONVERT =================
    public QuantityDTO convert(double value, String unit,
                               String type, String targetUnit) {

        QuantityDTO input = new QuantityDTO(value, unit, type);
        return service.convert(input, targetUnit);
    }
    // ================= EQUALITY =================
    public QuantityDTO compare(double v1, String u1, String type,
                               double v2, String u2) {

        QuantityDTO q1 = new QuantityDTO(v1, u1, type);
        QuantityDTO q2 = new QuantityDTO(v2, u2, type);

        return service.compareEquality(q1, q2);
    }
    // ================= DISPLAY =================
    public void display(QuantityDTO result) {
        if (result == null) {
            System.out.println("No result");
            return;
        }

        if (result.hasError()) {
            System.out.println("ERROR: " + result.getErrorMessage());
        } else {
            System.out.println(
                    "Result: " + result.getValue() + " " + result.getUnit()
            );
        }
    }
    // ADD (DTO version)
    public QuantityDTO add(QuantityDTO q1, QuantityDTO q2) {
        return service.add(q1, q2);
    }

    // SUBTRACT
    public QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2) {
        return service.subtract(q1, q2);
    }

    // DIVIDE
    public QuantityDTO divide(QuantityDTO q1, QuantityDTO q2) {
        return service.divide(q1, q2);
    }

    // CONVERT
    public QuantityDTO convert(QuantityDTO input, String targetUnit) {
        return service.convert(input, targetUnit);
    }

    // COMPARE
    public QuantityDTO compare(QuantityDTO q1, QuantityDTO q2) {
        return service.compareEquality(q1, q2);
    }
}