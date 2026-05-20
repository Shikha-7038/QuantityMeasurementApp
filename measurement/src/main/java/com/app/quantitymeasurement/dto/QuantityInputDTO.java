package com.app.quantitymeasurement.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityInputDTO {

    @NotNull(message = "Value cannot be null")
    @PositiveOrZero(message = "Value must be positive or zero")
    private Double value;

    @NotEmpty(message = "Unit cannot be empty")
    @Pattern(regexp = "^(CM|M|KM|INCH|FT|MILE|MG|G|KG|TONNE|OUNCE|POUND|ML|L|GALLON|CELSIUS|FAHRENHEIT|KELVIN)$",
            message = "Invalid unit")
    private String unit;

    @NotEmpty(message = "Measurement type cannot be empty")
    @Pattern(regexp = "^(LengthUnit|WeightUnit|VolumeUnit|TemperatureUnit)$",
            message = "Invalid measurement type")
    private String measurementType;
    private QuantityDTO thisQuantity;
    private QuantityDTO thatQuantity;

}
