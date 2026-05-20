package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.dto.QuantityDTO;
import com.app.quantitymeasurement.dto.QuantityMeasurementDTO;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/quantities")
public class QuantityMeasurementRestController {

    @Autowired
    private IQuantityMeasurementService service;

    @PostMapping("/compare")
    public QuantityMeasurementDTO compare(@RequestBody List<QuantityDTO> inputs) {
        return service.performCompare(inputs.get(0), inputs.get(1));
    }

    @PostMapping("/convert")
    public QuantityMeasurementDTO convert(@RequestBody QuantityDTO quantity, @RequestParam String targetUnit) {
        return service.performConvert(quantity, targetUnit);
    }

    @PostMapping("/add")
    public QuantityMeasurementDTO add(@RequestBody List<QuantityDTO> inputs, @RequestParam String targetUnit) {
        return service.performAdd(inputs.get(0), inputs.get(1), targetUnit);
    }

    @PostMapping("/subtract")
    public QuantityMeasurementDTO subtract(@RequestBody List<QuantityDTO> inputs, @RequestParam String targetUnit) {
        return service.performSubtract(inputs.get(0), inputs.get(1), targetUnit);
    }

    @PostMapping("/multiply")
    public QuantityMeasurementDTO multiply(@RequestBody QuantityDTO quantity, @RequestParam double multiplier) {
        return service.performMultiply(quantity, multiplier);
    }

    @PostMapping("/divide")
    public QuantityMeasurementDTO divide(@RequestBody QuantityDTO quantity, @RequestParam double divisor) {
        return service.performDivide(quantity, divisor);
    }

    @GetMapping("/history")
    public List<QuantityMeasurementDTO> getHistory() {
        return service.getAllHistory();
    }

    @GetMapping("/history/operation/{operation}")
    public List<QuantityMeasurementDTO> getHistoryByOperation(@PathVariable String operation) {
        System.out.println("CONTROLLER HIT");
        return service.getHistoryByOperation(operation.toUpperCase());
    }

    @GetMapping("/history/errored")
    public List<QuantityMeasurementDTO> getErrorHistory() {
        return service.getErrorHistory();
    }

    @GetMapping("/count/{operation}")
    public long getOperationCount(@PathVariable String operation) {
        return service.getOperationCount(operation.toUpperCase());
    }
}