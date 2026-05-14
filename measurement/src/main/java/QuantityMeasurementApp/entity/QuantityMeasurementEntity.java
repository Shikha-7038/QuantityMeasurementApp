package QuantityMeasurementApp.entity;
import java.io.Serializable;
public class QuantityMeasurementEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String operation;
    private final String input;
    private final String result;
    private final boolean error;

    public QuantityMeasurementEntity(String operation, String input, String result, boolean error){
        this.operation = operation;
        this.input = input;
        this.result = result;
        //this.error = false;
        this.error = error;
    }
    public QuantityMeasurementEntity(String operation, String errorMessage){
        this.operation = operation;
        this.input = null;
        this.result = errorMessage;
        this.error = true;
    }
    public String getOperation() {
        return operation;
    }

    public String getInput() {
        return input;
    }
    public String getResult() {
        return result;
    }
    public boolean hasError(){
        return error;
    }
}
