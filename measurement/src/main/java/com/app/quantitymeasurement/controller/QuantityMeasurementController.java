package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.dto.QuantityDTO;
import com.app.quantitymeasurement.service.QuantityMeasurementService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuantityMeasurementController {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(QuantityMeasurementController.class);
    private final QuantityMeasurementService service;

    public QuantityMeasurementController(QuantityMeasurementService service) {
        this.service = service;
    }

    public QuantityDTO add(double v1, String u1, String type,
                           double v2, String u2) {
        LOGGER.info("ADD request received");
        QuantityDTO q1 = new QuantityDTO(v1, u1, type);
        QuantityDTO q2 = new QuantityDTO(v2, u2, type);
        return service.add(q1, q2);
    }

    public QuantityDTO subtract(double v1, String u1, String type,
                                double v2, String u2) {
        LOGGER.info("SUBTRACT request received");
        QuantityDTO q1 = new QuantityDTO(v1, u1, type);
        QuantityDTO q2 = new QuantityDTO(v2, u2, type);
        return service.subtract(q1, q2);
    }

    public QuantityDTO divide(double v1, String u1, String type,
                              double v2, String u2) {
        LOGGER.info("DIVIDE request received");
        QuantityDTO q1 = new QuantityDTO(v1, u1, type);
        QuantityDTO q2 = new QuantityDTO(v2, u2, type);
        return service.divide(q1, q2);
    }

    public QuantityDTO convert(double value, String unit,
                               String type, String targetUnit) {
        LOGGER.info("CONVERT request received");
        QuantityDTO input = new QuantityDTO(value, unit, type);
        return service.convert(input, targetUnit);
    }
    public QuantityDTO compare(double v1, String u1, String type,
                               double v2, String u2) {
        LOGGER.info("COMPARE request received");
        QuantityDTO q1 = new QuantityDTO(v1, u1, type);
        QuantityDTO q2 = new QuantityDTO(v2, u2, type);
        return service.compareEquality(q1, q2);
    }

    public void display(QuantityDTO result) {
        if (result == null) {
            LOGGER.warn("Display called with null result");
            System.out.println("No result");
            return;
        }
        if (result.hasError()) {
            LOGGER.error(
                    "Operation failed: {}",
                    result.getErrorMessage()
            );
            System.out.println(
                    "ERROR: " + result.getErrorMessage()
            );
        } else {
            LOGGER.info(
                    "Operation successful: {} {}",
                    result.getValue(),
                    result.getUnit()
            );
            System.out.println(
                    "Result: " +
                            result.getValue() +
                            " " +
                            result.getUnit()
            );
        }
    }
    public QuantityDTO add(QuantityDTO q1, QuantityDTO q2) {
        LOGGER.info("ADD DTO request received");
        return service.add(q1, q2);
    }
    public QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2) {
        LOGGER.info("SUBTRACT DTO request received");
        return service.subtract(q1, q2);
    }
    public QuantityDTO divide(QuantityDTO q1, QuantityDTO q2) {
        LOGGER.info("DIVIDE DTO request received");
        return service.divide(q1, q2);
    }
    public QuantityDTO convert(QuantityDTO input, String targetUnit) {
        LOGGER.info("CONVERT DTO request received");
        return service.convert(input, targetUnit);
    }
    public QuantityDTO compare(QuantityDTO q1, QuantityDTO q2) {
        LOGGER.info("COMPARE DTO request received");
        return service.compareEquality(q1, q2);
    }
}