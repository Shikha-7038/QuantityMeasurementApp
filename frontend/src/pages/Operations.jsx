import React, { useState } from 'react';
import { FiRefreshCw } from 'react-icons/fi';
import Converter from '../components/Operations/Converter';
import Comparator from '../components/Operations/Comparator';
import ArithmeticOps from '../components/Operations/ArithmeticOps';
import toast from 'react-hot-toast';

const Operations = () => {
    const [activeTab, setActiveTab] = useState('convert');
    const [result, setResult] = useState(null);

    const tabs = [
        { id: 'convert', label: '🔄 Convert', color: 'cyan' },
        { id: 'compare', label: '⚖️ Compare', color: 'blue' },
        { id: 'arithmetic', label: '🧮 Arithmetic', color: 'purple' },
    ];

    const handleResult = (data) => {
        setResult(data);
        if (data.isError) {
            toast.error(data.errorMessage || 'Operation failed');
        } else {
            toast.success('Operation completed successfully');
        }
    };

    return (
        <div className="space-y-6 animate-fadeIn">
            {/* Page Header */}
            <div>
                <h1 className="text-3xl font-bold text-white">Operations</h1>
                <p className="text-gray-400 mt-1">Perform unit conversions and measurements</p>
            </div>

            {/* Tabs */}
            <div className="flex gap-2 border-b border-white/10 pb-2">
                {tabs.map((tab) => (
                    <button
                        key={tab.id}
                        onClick={() => setActiveTab(tab.id)}
                        className={`px-6 py-2 rounded-lg transition-all duration-200 ${
                            activeTab === tab.id
                                ? 'bg-gradient-to-r from-blue-500/20 to-purple-500/20 text-blue-400 border border-blue-500/30'
                                : 'text-gray-400 hover:text-white hover:bg-white/5'
                        }`}
                    >
                        {tab.label}
                    </button>
                ))}
            </div>

            {/* Tab Content */}
            <div>
                {activeTab === 'convert' && <Converter onResult={handleResult} />}
                {activeTab === 'compare' && <Comparator onResult={handleResult} />}
                {activeTab === 'arithmetic' && <ArithmeticOps onResult={handleResult} />}
            </div>

            {/* Result Display */}
            {result && (
                <div className={`glass-card rounded-xl p-6 mt-6 ${
                    result.isError ? 'border-red-500/30' : 'border-green-500/30'
                }`}>
                    <h3 className="text-lg font-semibold text-white mb-3">Result</h3>
                    <div className={`p-4 rounded-lg ${
                        result.isError ? 'bg-red-500/10' : 'bg-green-500/10'
                    }`}>
                        {result.isError ? (
                            <div>
                                <p className="text-red-400 font-medium">Error</p>
                                <p className="text-gray-300 text-sm mt-1">{result.errorMessage}</p>
                            </div>
                        ) : (
                            <div>
                                <p className="text-green-400 font-medium">Success</p>
                                <p className="text-2xl font-bold text-white mt-2">
                                    {typeof result.resultValue === 'number'
                                        ? result.resultValue.toFixed(6).replace(/\.?0+$/, '')
                                        : result.resultValue}{' '}
                                    {result.resultUnit}
                                </p>
                                {result.operation && (
                                    <p className="text-sm text-gray-400 mt-2">
                                        Operation: {result.operation}
                                    </p>
                                )}
                            </div>
                        )}
                    </div>
                </div>
            )}
        </div>
    );
};

export default Operations;