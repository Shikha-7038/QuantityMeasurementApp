package QuantityMeasurementApp.enums;

public interface IMeasurable {

    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double baseValue);

    @FunctionalInterface
    interface SupportsArithmetic{
        boolean isSupported();
    }
    default String getUnitName(){
        return this.getClass().getSimpleName();
    }

    default SupportsArithmetic supportsArithmetic(){
        return () -> true;
    }

    default void validateOperationSupport(String operation){

    }
}
