import React, { useState } from 'react';
import { quantityApi } from '../../api/quantityApi';
import { MEASUREMENT_TYPES, UNITS } from '../../utils/constants';
import { validateQuantity, validateDivisor, validateMultiplier } from '../../utils/validators';

const ArithmeticOps = ({ onResult }) => {
    const [operation, setOperation] = useState('ADD');
    const [measurementType, setMeasurementType] = useState(MEASUREMENT_TYPES.LENGTH);
    const [quantity1, setQuantity1] = useState({ value: '', unit: 'FEET' });
    const [quantity2, setQuantity2] = useState({ value: '', unit: 'INCH' });
    const [singleValue, setSingleValue] = useState('');
    const [targetUnit, setTargetUnit] = useState('FEET');
    const [loading, setLoading] = useState(false);

    const operations = [
        { id: 'ADD', label: '➕ Add', needsTwo: true },
        { id: 'SUBTRACT', label: '➖ Subtract', needsTwo: true },
        { id: 'MULTIPLY', label: '✖️ Multiply', needsTwo: false },
        { id: 'DIVIDE', label: '➗ Divide', needsTwo: false },
    ];

    const needsTwoQuantities = operations.find(op => op.id === operation)?.needsTwo;

    const handleArithmetic = async () => {
        setLoading(true);
        try {
            let response;

            if (needsTwoQuantities) {
                const validation1 = validateQuantity(
                    parseFloat(quantity1.value),
                    quantity1.unit,
                    measurementType
                );
                const validation2 = validateQuantity(
                    parseFloat(quantity2.value),
                    quantity2.unit,
                    measurementType
                );

                if (!validation1.isValid || !validation2.isValid) {
                    onResult({
                        isError: true,
                        errorMessage: [...validation1.errors, ...validation2.errors].join(', '),
                    });
                    setLoading(false);
                    return;
                }

                const q1 = {
                    value: parseFloat(quantity1.value),
                    unit: quantity1.unit,
                    measurementType: measurementType,
                };
                const q2 = {
                    value: parseFloat(quantity2.value),
                    unit: quantity2.unit,
                    measurementType: measurementType,
                };

                if (operation === 'ADD') {
                    response = await quantityApi.add(q1, q2, targetUnit);
                } else if (operation === 'SUBTRACT') {
                    response = await quantityApi.subtract(q1, q2, targetUnit);
                }
            } else {
                const validation = validateQuantity(
                    parseFloat(singleValue),
                    quantity1.unit,
                    measurementType
                );

                if (!validation.isValid) {
                    onResult({ isError: true, errorMessage: validation.errors.join(', ') });
                    setLoading(false);
                    return;
                }

                const q = {
                    value: parseFloat(singleValue),
                    unit: quantity1.unit,
                    measurementType: measurementType,
                };

                if (operation === 'MULTIPLY') {
                    const multiplierValidation = validateMultiplier(parseFloat(quantity2.value));
                    if (!multiplierValidation.isValid) {
                        onResult({ isError: true, errorMessage: multiplierValidation.error });
                        setLoading(false);
                        return;
                    }
                    response = await quantityApi.multiply(q, parseFloat(quantity2.value));
                } else if (operation === 'DIVIDE') {
                    const divisorValidation = validateDivisor(parseFloat(quantity2.value));
                    if (!divisorValidation.isValid) {
                        onResult({ isError: true, errorMessage: divisorValidation.error });
                        setLoading(false);
                        return;
                    }
                    response = await quantityApi.divide(q, parseFloat(quantity2.value));
                }
            }

            onResult({
                ...response,
                operation: operation,
            });
        } catch (error) {
            onResult({
                isError: true,
                errorMessage: error.response?.data?.message || `${operation} operation failed`,
            });
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="glass-card rounded-xl p-6">
            <h2 className="text-xl font-semibold text-white mb-6">Arithmetic Operations</h2>

            <div className="space-y-6">
                {/* Operation Selection */}
                <div className="flex flex-wrap gap-2">
                    {operations.map((op) => (
                        <button
                            key={op.id}
                            onClick={() => setOperation(op.id)}
                            className={`px-4 py-2 rounded-lg transition-all duration-200 ${
                                operation === op.id
                                    ? 'bg-gradient-to-r from-blue-500 to-purple-500 text-white'
                                    : 'bg-white/5 text-gray-400 hover:bg-white/10'
                            }`}
                        >
                            {op.label}
                        </button>
                    ))}
                </div>

                {/* Measurement Type (for ADD/SUBTRACT only) */}
                {(operation === 'ADD' || operation === 'SUBTRACT') && (
                    <div>
                        <label className="block text-sm text-gray-400 mb-2">Measurement Category</label>
                        <select
                            value={measurementType}
                            onChange={(e) => {
                                setMeasurementType(e.target.value);
                                setTargetUnit(UNITS[e.target.value][0]?.value || '');
                            }}
                            className="w-full px-4 py-3 rounded-lg bg-white/5 border border-white/10 text-white focus:outline-none focus:border-blue-500"
                        >
                            <option value={MEASUREMENT_TYPES.LENGTH}>Length</option>
                            <option value={MEASUREMENT_TYPES.WEIGHT}>Weight</option>
                            <option value={MEASUREMENT_TYPES.VOLUME}>Volume</option>
                        </select>
                        <p className="text-xs text-gray-500 mt-1">
                            Note: Temperature does not support arithmetic operations
                        </p>
                    </div>
                )}

                {/* Quantity 1 */}
                <div className="grid grid-cols-2 gap-4">
                    <div>
                        <label className="block text-sm text-gray-400 mb-2">
                            {needsTwoQuantities ? 'Value 1' : 'Value'}
                        </label>
                        <input
                            type="number"
                            value={needsTwoQuantities ? quantity1.value : singleValue}
                            onChange={(e) => {
                                if (needsTwoQuantities) {
                                    setQuantity1({ ...quantity1, value: e.target.value });
                                } else {
                                    setSingleValue(e.target.value);
                                }
                            }}
                            placeholder="Enter value..."
                            className="w-full px-4 py-3 rounded-lg bg-white/5 border border-white/10 text-white focus:outline-none focus:border-blue-500"
                        />
                    </div>
                    <div>
                        <label className="block text-sm text-gray-400 mb-2">Unit</label>
                        <select
                            value={quantity1.unit}
                            onChange={(e) => setQuantity1({ ...quantity1, unit: e.target.value })}
                            className="w-full px-4 py-3 rounded-lg bg-white/5 border border-white/10 text-white focus:outline-none focus:border-blue-500"
                        >
                            {(operation === 'ADD' || operation === 'SUBTRACT'
                                    ? UNITS[measurementType]
                                    : UNITS[MEASUREMENT_TYPES.LENGTH]
                            )?.map((unit) => (
                                <option key={unit.value} value={unit.value}>
                                    {unit.label}
                                </option>
                            ))}
                        </select>
                    </div>
                </div>

                {/* Quantity 2 or Multiplier/Divisor */}
                <div className="grid grid-cols-2 gap-4">
                    <div>
                        <label className="block text-sm text-gray-400 mb-2">
                            {needsTwoQuantities ? 'Value 2' : operation === 'MULTIPLY' ? 'Multiplier' : 'Divisor'}
                        </label>
                        <input
                            type="number"
                            value={quantity2.value}
                            onChange={(e) => setQuantity2({ ...quantity2, value: e.target.value })}
                            placeholder={needsTwoQuantities ? "Enter second value..." : "Enter number..."}
                            className="w-full px-4 py-3 rounded-lg bg-white/5 border border-white/10 text-white focus:outline-none focus:border-blue-500"
                            step={operation === 'DIVIDE' ? "any" : "1"}
                        />
                    </div>
                    {needsTwoQuantities && (
                        <div>
                            <label className="block text-sm text-gray-400 mb-2">Unit 2</label>
                            <select
                                value={quantity2.unit}
                                onChange={(e) => setQuantity2({ ...quantity2, unit: e.target.value })}
                                className="w-full px-4 py-3 rounded-lg bg-white/5 border border-white/10 text-white focus:outline-none focus:border-blue-500"
                            >
                                {UNITS[measurementType]?.map((unit) => (
                                    <option key={unit.value} value={unit.value}>
                                        {unit.label}
                                    </option>
                                ))}
                            </select>
                        </div>
                    )}
                </div>

                {/* Target Unit (for ADD/SUBTRACT only) */}
                {(operation === 'ADD' || operation === 'SUBTRACT') && (
                    <div>
                        <label className="block text-sm text-gray-400 mb-2">Target Unit (for result)</label>
                        <select
                            value={targetUnit}
                            onChange={(e) => setTargetUnit(e.target.value)}
                            className="w-full px-4 py-3 rounded-lg bg-white/5 border border-white/10 text-white focus:outline-none focus:border-blue-500"
                        >
                            {UNITS[measurementType]?.map((unit) => (
                                <option key={unit.value} value={unit.value}>
                                    {unit.label}
                                </option>
                            ))}
                        </select>
                    </div>
                )}

                {/* Execute Button */}
                <button
                    onClick={handleArithmetic}
                    disabled={loading}
                    className="w-full py-3 bg-gradient-to-r from-purple-500 to-pink-500 rounded-lg text-white font-medium hover:opacity-90 transition-all duration-200 disabled:opacity-50"
                >
                    {loading ? 'Processing...' : `Execute ${operation}`}
                </button>
            </div>
        </div>
    );
};

export default ArithmeticOps;