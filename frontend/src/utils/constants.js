export const MEASUREMENT_TYPES = {
    LENGTH: 'LengthUnit',
    WEIGHT: 'WeightUnit',
    VOLUME: 'VolumeUnit',
    TEMPERATURE: 'TemperatureUnit',
};

export const UNITS = {
    [MEASUREMENT_TYPES.LENGTH]: [
        { value: 'FEET', label: 'Feet (ft)', symbol: 'ft' },
        { value: 'INCH', label: 'Inch (in)', symbol: 'in' },
        { value: 'YARD', label: 'Yard (yd)', symbol: 'yd' },
        { value: 'CENTIMETERS', label: 'Centimeter (cm)', symbol: 'cm' },
    ],
    [MEASUREMENT_TYPES.WEIGHT]: [
        { value: 'KILOGRAM', label: 'Kilogram (kg)', symbol: 'kg' },
        { value: 'GRAM', label: 'Gram (g)', symbol: 'g' },
        { value: 'POUND', label: 'Pound (lb)', symbol: 'lb' },
    ],
    [MEASUREMENT_TYPES.VOLUME]: [
        { value: 'LITRE', label: 'Litre (L)', symbol: 'L' },
        { value: 'MILLILITRE', label: 'Millilitre (mL)', symbol: 'mL' },
        { value: 'GALLON', label: 'Gallon (gal)', symbol: 'gal' },
    ],
    [MEASUREMENT_TYPES.TEMPERATURE]: [
        { value: 'CELSIUS', label: 'Celsius (°C)', symbol: '°C' },
        { value: 'FAHRENHEIT', label: 'Fahrenheit (°F)', symbol: '°F' },
        { value: 'KELVIN', label: 'Kelvin (K)', symbol: 'K' },
    ],
};

export const OPERATIONS = {
    ADD: 'ADD',
    SUBTRACT: 'SUBTRACT',
    MULTIPLY: 'MULTIPLY',
    DIVIDE: 'DIVIDE',
    COMPARE: 'COMPARE',
    CONVERT: 'CONVERT',
};

export const OPERATION_COLORS = {
    ADD: '#10b981',
    SUBTRACT: '#f59e0b',
    MULTIPLY: '#8b5cf6',
    DIVIDE: '#ef4444',
    COMPARE: '#3b82f6',
    CONVERT: '#06b6d4',
};