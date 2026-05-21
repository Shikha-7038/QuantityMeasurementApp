import React from 'react';
import { FiTrendingUp, FiTrendingDown } from 'react-icons/fi';

const MetricCard = ({ title, value, icon: Icon, trend, color }) => {
    const colors = {
        blue: 'from-blue-500 to-blue-600',
        green: 'from-green-500 to-green-600',
        purple: 'from-purple-500 to-purple-600',
        orange: 'from-orange-500 to-orange-600',
    };

    return (
        <div className="glass-card rounded-xl p-6 animate-fadeIn">
            <div className="flex items-center justify-between mb-4">
                <div className={`p-3 rounded-lg bg-gradient-to-r ${colors[color]} bg-opacity-20`}>
                    <Icon className="w-6 h-6 text-white" />
                </div>
                {trend && (
                    <div className={`flex items-center gap-1 text-sm ${trend > 0 ? 'text-green-400' : 'text-red-400'}`}>
                        {trend > 0 ? <FiTrendingUp /> : <FiTrendingDown />}
                        <span>{Math.abs(trend)}%</span>
                    </div>
                )}
            </div>
            <h3 className="text-2xl font-bold text-white">{value}</h3>
            <p className="text-sm text-gray-400 mt-1">{title}</p>
        </div>
    );
};

export default MetricCard;