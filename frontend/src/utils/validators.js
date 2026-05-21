export const validateQuantity = (value, unit, measurementType) => {
    const errors = [];

    if (value === undefined || value === null || isNaN(value)) {
        errors.push('Value is required');
    } else if (value < 0) {
        errors.push('Value must be positive or zero');
    }

    if (!unit) {
        errors.push('Unit is required');
    }

    if (!measurementType) {
        errors.push('Measurement type is required');
    }

    return {
        isValid: errors.length === 0,
        errors,
    };
};

export const validateDivisor = (divisor) => {
    if (divisor === 0) {
        return { isValid: false, error: 'Division by zero is not allowed' };
    }
    return { isValid: true, error: null };
};

export const validateMultiplier = (multiplier) => {
    if (isNaN(multiplier)) {
        return { isValid: false, error: 'Multiplier must be a number' };
    }
    return { isValid: true, error: null };
};