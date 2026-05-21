import React, { useState, useEffect } from 'react';
import {
    BarChart,
    Bar,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    ResponsiveContainer,
    PieChart,
    Pie,
    Cell,
} from 'recharts';
import { quantityApi } from '../../api/quantityApi';
import { OPERATION_COLORS } from '../../utils/constants';

const StatsChart = () => {
    const [operationStats, setOperationStats] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchOperationStats();
    }, []);

    const fetchOperationStats = async () => {
        try {
            const operations = ['ADD', 'SUBTRACT', 'MULTIPLY', 'DIVIDE', 'COMPARE', 'CONVERT'];
            const stats = await Promise.all(
                operations.map(async (op) => {
                    const count = await quantityApi.getOperationCount(op);
                    return { name: op, count, color: OPERATION_COLORS[op] };
                })
            );
            setOperationStats(stats.filter(s => s.count > 0));
        } catch (error) {
            console.error('Failed to fetch stats:', error);
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return (
            <div className="glass-card rounded-xl p-6">
                <div className="animate-pulse">
                    <div className="h-64 bg-white/5 rounded"></div>
                </div>
            </div>
        );
    }

    return (
        <div className="glass-card rounded-xl p-6">
            <h3 className="text-lg font-semibold text-white mb-4">Operation Statistics</h3>
            <div className="h-64">
                <ResponsiveContainer width="100%" height="100%">
                    <BarChart data={operationStats}>
                        <CartesianGrid strokeDasharray="3 3" stroke="#333" />
                        <XAxis dataKey="name" stroke="#888" />
                        <YAxis stroke="#888" />
                        <Tooltip
                            contentStyle={{
                                backgroundColor: '#1e1e2f',
                                border: '1px solid #3b82f6',
                                borderRadius: '8px',
                            }}
                            labelStyle={{ color: '#fff' }}
                        />
                        <Bar dataKey="count" fill="#3b82f6" radius={[8, 8, 0, 0]}>
                            {operationStats.map((entry, index) => (
                                <Cell key={`cell-${index}`} fill={entry.color} />
                            ))}
                        </Bar>
                    </BarChart>
                </ResponsiveContainer>
            </div>
        </div>
    );
};

export default StatsChart;