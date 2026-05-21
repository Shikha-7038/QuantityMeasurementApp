import React, { useState } from 'react';
import { quantityApi } from '../../api/quantityApi';
import { MEASUREMENT_TYPES, UNITS } from '../../utils/constants';
import { validateQuantity } from '../../utils/validators';

const Comparator = ({ onResult }) => {
    const [measurementType, setMeasurementType] = useState(MEASUREMENT_TYPES.LENGTH);
    const [quantity1, setQuantity1] = useState({ value: '', unit: 'FEET' });
    const [quantity2, setQuantity2] = useState({ value: '', unit: 'INCH' });
    const [loading, setLoading] = useState(false);

    const handleCompare = async () => {
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
            return;
        }

        setLoading(true);
        try {
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
            const response = await quantityApi.compare(q1, q2);
            onResult({
                ...response,
                resultValue: response.resultValue === 1 ? 'Equal' : 'Not Equal',
                resultUnit: '',
                operation: 'COMPARE',
            });
        } catch (error) {
            onResult({
                isError: true,
                errorMessage: error.response?.data?.message || 'Comparison failed',
            });
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="glass-card rounded-xl p-6">
            <h2 className="text-xl font-semibold text-white mb-6">Compare Quantities</h2>

            <div className="space-y-6">
                {/* Measurement Type */}
                <div>
                    <label className="block text-sm text-gray-400 mb-2">Measurement Category</label>
                    <select
                        value={measurementType}
                        onChange={(e) => setMeasurementType(e.target.value)}
                        className="w-full px-4 py-3 rounded-lg bg-white/5 border border-white/10 text-white focus:outline-none focus:border-blue-500"
                    >
                        <option value={MEASUREMENT_TYPES.LENGTH}>Length</option>
                        <option value={MEASUREMENT_TYPES.WEIGHT}>Weight</option>
                        <option value={MEASUREMENT_TYPES.VOLUME}>Volume</option>
                        <option value={MEASUREMENT_TYPES.TEMPERATURE}>Temperature</option>
                    </select>
                </div>

                {/* Quantity 1 */}
                <div className="grid grid-cols-2 gap-4">
                    <div>
                        <label className="block text-sm text-gray-400 mb-2">Value 1</label>
                        <input
                            type="number"
                            value={quantity1.value}
                            onChange={(e) => setQuantity1({ ...quantity1, value: e.target.value })}
                            placeholder="Enter value..."
                            className="w-full px-4 py-3 rounded-lg bg-white/5 border border-white/10 text-white focus:outline-none focus:border-blue-500"
                        />
                    </div>
                    <div>
                        <label className="block text-sm text-gray-400 mb-2">Unit 1</label>
                        <select
                            value={quantity1.unit}
                            onChange={(e) => setQuantity1({ ...quantity1, unit: e.target.value })}
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

                {/* Quantity 2 */}
                <div className="grid grid-cols-2 gap-4">
                    <div>
                        <label className="block text-sm text-gray-400 mb-2">Value 2</label>
                        <input
                            type="number"
                            value={quantity2.value}
                            onChange={(e) => setQuantity2({ ...quantity2, value: e.target.value })}
                            placeholder="Enter value..."
                            className="w-full px-4 py-3 rounded-lg bg-white/5 border border-white/10 text-white focus:outline-none focus:border-blue-500"
                        />
                    </div>
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
                </div>

                {/* Compare Button */}
                <button
                    onClick={handleCompare}
                    disabled={loading}
                    className="w-full py-3 bg-gradient-to-r from-green-500 to-emerald-500 rounded-lg text-white font-medium hover:opacity-90 transition-all duration-200 disabled:opacity-50"
                >
                    {loading ? 'Comparing...' : 'Compare'}
                </button>
            </div>
        </div>
    );
};

export default Comparator;