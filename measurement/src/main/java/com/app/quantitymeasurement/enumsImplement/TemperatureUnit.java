package com.app.quantitymeasurement.enumsImplement;
import com.app.quantitymeasurement.enums.IMeasurable;
import java.util.function.Function;
public enum TemperatureUnit implements IMeasurable {
    CELSIUS(
            c -> c,           // to base (Celsius)
            c -> c            // from base
    ),
    FAHRENHEIT(
            f -> (f - 32) * 5.0 / 9.0,   // to base (Celsius)
            c -> (c * 9.0 / 5.0) + 32    // from base
    ),
    KELVIN(
            k -> k - 273.15,    // to base (Celsius)
            c -> c + 273.15     // from base
    );

    private final Function<Double, Double> toBase;
    private final Function<Double, Double> fromBase;
    TemperatureUnit(Function<Double, Double> toBase, Function<Double, Double> fromBase) {
        this.toBase = toBase;
        this.fromBase = fromBase;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return toBase.apply(value);
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return fromBase.apply(baseValue);
    }

    @Override
    public SupportsArithmetic supportsArithmetic() {
        return () -> false;
    }

    @Override
    public void validateOperationSupport(String operation) {
        throw new UnsupportedOperationException(
                "Operation '" + operation + "' is not supported for Temperature measurements."
        );
    }
}