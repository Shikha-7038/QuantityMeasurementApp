package com.app.quantitymeasurement.dto;

import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuantityMeasurementDTO {

    private Long id;
    private String operation;
    private String measurementType;
    private Double inputValue;
    private String inputUnit;
    private Double secondValue;
    private String secondUnit;
    private Double resultValue;
    private String resultUnit;
    private boolean isError;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // FIXED: Returns a default DTO instead of null
    public static QuantityMeasurementDTO fromEntity(QuantityMeasurementEntity entity) {
        if (entity == null) {
            // Return a fallback DTO instead of null
            return QuantityMeasurementDTO.builder()
                    .isError(true)
                    .errorMessage("Entity is null")
                    .createdAt(LocalDateTime.now())
                    .modifiedAt(LocalDateTime.now())
                    .build();
        }

        return QuantityMeasurementDTO.builder()
                .id(entity.getId())
                .operation(entity.getOperation())
                .measurementType(entity.getMeasurementType())
                .inputValue(entity.getInputValue())
                .inputUnit(entity.getInputUnit())
                .secondValue(entity.getSecondValue())
                .secondUnit(entity.getSecondUnit())
                .resultValue(entity.getResultValue())
                .resultUnit(entity.getResultUnit())
                .isError(entity.hasError())
                .errorMessage(entity.getErrorMessage())
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .build();
    }

    public QuantityMeasurementEntity toEntity() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setId(this.id);
        entity.setOperation(this.operation);
        entity.setMeasurementType(this.measurementType);
        entity.setInputValue(this.inputValue);
        entity.setInputUnit(this.inputUnit);
        entity.setSecondValue(this.secondValue);
        entity.setSecondUnit(this.secondUnit);
        entity.setResultValue(this.resultValue);
        entity.setResultUnit(this.resultUnit);
        entity.setError(this.isError);
        entity.setErrorMessage(this.errorMessage);
        entity.setCreatedAt(this.createdAt);
        entity.setModifiedAt(this.modifiedAt);
        return entity;
    }

    public static List<QuantityMeasurementDTO> fromEntityList(List<QuantityMeasurementEntity> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .map(QuantityMeasurementDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public static List<QuantityMeasurementEntity> toEntityList(List<QuantityMeasurementDTO> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream()
                .map(QuantityMeasurementDTO::toEntity)
                .collect(Collectors.toList());
    }
}
//package com.app.quantitymeasurement.dto;
//
//import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class QuantityMeasurementDTO {
//
//    private Long id;
//    private String operation;
//    private String measurementType;
//    private Double inputValue;
//    private String inputUnit;
//    private Double secondValue;
//    private String secondUnit;
//    private Double resultValue;
//    private String resultUnit;
//    private boolean isError;
//    private String errorMessage;
//    private LocalDateTime createdAt;
//    private LocalDateTime modifiedAt;
//
//    public static QuantityMeasurementDTO fromEntity(QuantityMeasurementEntity entity) {
//        if (entity == null) return null;
//
//        return QuantityMeasurementDTO.builder()
//                .id(entity.getId())
//                .operation(entity.getOperation())
//                .measurementType(entity.getMeasurementType())
//                .inputValue(entity.getInputValue())
//                .inputUnit(entity.getInputUnit())
//                .secondValue(entity.getSecondValue())
//                .secondUnit(entity.getSecondUnit())
//                .resultValue(entity.getResultValue())
//                .resultUnit(entity.getResultUnit())
//                .isError(entity.hasError())  // ← CHANGE: Use hasError() instead of isError()
//                .errorMessage(entity.getErrorMessage())
//                .createdAt(entity.getCreatedAt())
//                .modifiedAt(entity.getModifiedAt())
//                .build();
//    }
//
//    public QuantityMeasurementEntity toEntity() {
//        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
//        entity.setId(this.id);
//        entity.setOperation(this.operation);
//        entity.setMeasurementType(this.measurementType);
//        entity.setInputValue(this.inputValue);
//        entity.setInputUnit(this.inputUnit);
//        entity.setSecondValue(this.secondValue);
//        entity.setSecondUnit(this.secondUnit);
//        entity.setResultValue(this.resultValue);
//        entity.setResultUnit(this.resultUnit);
//        entity.setError(this.isError);
//        entity.setErrorMessage(this.errorMessage);
//        entity.setCreatedAt(this.createdAt);
//        entity.setModifiedAt(this.modifiedAt);
//        return entity;
//    }
//
//    public static List<QuantityMeasurementDTO> fromEntityList(List<QuantityMeasurementEntity> entities) {
//        if (entities == null) return List.of();
//        return entities.stream()
//                .map(QuantityMeasurementDTO::fromEntity)
//                .collect(Collectors.toList());
//    }
//
//    public static List<QuantityMeasurementEntity> toEntityList(List<QuantityMeasurementDTO> dtos) {
//        if (dtos == null) return List.of();
//        return dtos.stream()
//                .map(QuantityMeasurementDTO::toEntity)
//                .collect(Collectors.toList());
//    }
//}
