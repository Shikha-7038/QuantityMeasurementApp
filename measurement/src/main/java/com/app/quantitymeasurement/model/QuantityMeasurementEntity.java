package com.app.quantitymeasurement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "quantity_measurements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuantityMeasurementEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "operation", nullable = false)
    private String operation;

    @Column(name = "measurement_type")
    private String measurementType;

    @Column(name = "input_value")
    private Double inputValue;

    @Column(name = "input_unit")
    private String inputUnit;

    @Column(name = "second_value")
    private Double secondValue;

    @Column(name = "second_unit")
    private String secondUnit;

    @Column(name = "result_value")
    private Double resultValue;

    @Column(name = "result_unit")
    private String resultUnit;

    @Column(name = "is_error")
    private boolean isError;

    @Column(name = "error_message", length = 500)
    private String errorMessage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Transient
    private String input;

    @Transient
    private String result;

    @Transient
    private boolean error;

    public QuantityMeasurementEntity(String operation, String input, String result, boolean error) {
        this.operation = operation;
        this.input = input;
        this.result = result;
        this.error = error;
        this.isError = error;
        this.errorMessage = error ? result : null;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    public QuantityMeasurementEntity(String operation, String errorMessage) {
        this.operation = operation;
        this.input = null;
        this.result = errorMessage;
        this.error = true;
        this.isError = true;
        this.errorMessage = errorMessage;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    public String getInput() {
        return input != null ? input : (inputValue != null && inputUnit != null ? inputValue + " " + inputUnit : null);
    }

    public String getResult() {
        return result != null ? result : (resultValue != null ? String.valueOf(resultValue) : errorMessage);
    }

    public boolean hasError() {
        return error || isError;
    }

    public boolean getIsError() {
        return isError;
    }
    public void setError(boolean error) {
        this.isError = error;
        this.error = error;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        modifiedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedAt = LocalDateTime.now();
    }
}