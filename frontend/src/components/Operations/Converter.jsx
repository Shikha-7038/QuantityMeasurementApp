import React, { useState } from 'react';
import { FiArrowRight } from 'react-icons/fi';
import { quantityApi } from '../../api/quantityApi';
import { MEASUREMENT_TYPES, UNITS } from '../../utils/constants';
import { validateQuantity } from '../../utils/validators';

const Converter = ({ onResult }) => {
    const [measurementType, setMeasurementType] = useState(MEASUREMENT_TYPES.LENGTH);
    const [fromUnit, setFromUnit] = useState('FEET');
    const [toUnit, setToUnit] = useState('INCH');
    const [value, setValue] = useState('');
    const [loading, setLoading] = useState(false);

    const handleConvert = async () => {
        const validation = validateQuantity(parseFloat(value), fromUnit, measurementType);
        if (!validation.isValid) {
            onResult({ isError: true, errorMessage: validation.errors.join(', ') });
            return;
        }

        setLoading(true);
        try {
            const quantity = {
                value: parseFloat(value),
                unit: fromUnit,
                measurementType: measurementType,
            };
            const response = await quantityApi.convert(quantity, toUnit);
            onResult({
                ...response,
                resultValue: response.resultValue,
                resultUnit: toUnit,
                operation: 'CONVERT',
            });
        } catch (error) {
            onResult({
                isError: true,
                errorMessage: error.response?.data?.message || 'Conversion failed',
            });
        } finally {
            setLoading(false);
        }
    };

    const handleSwap = () => {
        setFromUnit(toUnit);
        setToUnit(fromUnit);
    };

    return (
        <div className="glass-card rounded-xl p-6">
            <h2 className="text-xl font-semibold text-white mb-6">Unit Converter</h2>

            <div className="space-y-6">
                {/* Measurement Type */}
                <div>
                    <label className="block text-sm text-gray-400 mb-2">Measurement Category</label>
                    <select
                        value={measurementType}
                        onChange={(e) => {
                            setMeasurementType(e.target.value);
                            setFromUnit(UNITS[e.target.value][0]?.value || '');
                            setToUnit(UNITS[e.target.value][1]?.value || '');
                        }}
                        className="w-full px-4 py-3 rounded-lg bg-white/5 border border-white/10 text-white focus:outline-none focus:border-blue-500 transition-all duration-200"
                    >
                        <option value={MEASUREMENT_TYPES.LENGTH}>Length</option>
                        <option value={MEASUREMENT_TYPES.WEIGHT}>Weight</option>
                        <option value={MEASUREMENT_TYPES.VOLUME}>Volume</option>
                        <option value={MEASUREMENT_TYPES.TEMPERATURE}>Temperature</option>
                    </select>
                </div>

                {/* Value Input */}
                <div>
                    <label className="block text-sm text-gray-400 mb-2">Value</label>
                    <input
                        type="number"
                        value={value}
                        onChange={(e) => setValue(e.target.value)}
                        placeholder="Enter value..."
                        className="w-full px-4 py-3 rounded-lg bg-white/5 border border-white/10 text-white focus:outline-none focus:border-blue-500 transition-all duration-200"
                    />
                </div>

                {/* From/To Units */}
                <div className="grid grid-cols-[1fr,auto,1fr] gap-4 items-center">
                    <div>
                        <label className="block text-sm text-gray-400 mb-2">From</label>
                        <select
                            value={fromUnit}
                            onChange={(e) => setFromUnit(e.target.value)}
                            className="w-full px-4 py-3 rounded-lg bg-white/5 border border-white/10 text-white focus:outline-none focus:border-blue-500"
                        >
                            {UNITS[measurementType]?.map((unit) => (
                                <option key={unit.value} value={unit.value}>
                                    {unit.label}
                                </option>
                            ))}
                        </select>
                    </div>

                    <button
                        onClick={handleSwap}
                        className="mt-6 p-2 rounded-full bg-white/10 hover:bg-white/20 transition-all duration-200"
                    >
                        <FiArrowRight className="w-5 h-5 text-blue-400" />
                    </button>

                    <div>
                        <label className="block text-sm text-gray-400 mb-2">To</label>
                        <select
                            value={toUnit}
                            onChange={(e) => setToUnit(e.target.value)}
                            className="w-full px-4 py-3 rounded-lg bg-white/5 border border-white/10 text-white focus:outline-none focus:border-blue-500"
                        >
                            {UNITS[measurementType]?.map((unit) => (
                                <option key={unit.value} value={unit.value}>
                                    {unit.label}
                                </option>
                            ))}
                        </select>
                    </div>
                </div>

                {/* Convert Button */}
                <button
                    onClick={handleConvert}
                    disabled={loading}
                    className="w-full py-3 bg-gradient-to-r from-blue-500 to-purple-500 rounded-lg text-white font-medium hover:opacity-90 transition-all duration-200 disabled:opacity-50"
                >
                    {loading ? 'Converting...' : 'Convert'}
                </button>
            </div>
        </div>
    );
};

export default Converter;