import React, { useState, useEffect } from 'react';
import { quantityApi } from '../api/quantityApi';
import { formatDate, formatOperationType, formatResult } from '../utils/formatters';
import { OPERATION_COLORS } from '../utils/constants';
import { FiSearch, FiFilter, FiDownload, FiRefreshCw } from 'react-icons/fi';

const History = () => {
    const [history, setHistory] = useState([]);
    const [filteredHistory, setFilteredHistory] = useState([]);
    const [loading, setLoading] = useState(true);
    const [filter, setFilter] = useState('ALL');
    const [searchTerm, setSearchTerm] = useState('');

    useEffect(() => {
        fetchHistory();
    }, []);

    useEffect(() => {
        applyFilters();
    }, [filter, searchTerm, history]);

    const fetchHistory = async () => {
        try {
            const data = await quantityApi.getHistory();
            setHistory(data);
        } catch (error) {
            console.error('Failed to fetch history:', error);
        } finally {
            setLoading(false);
        }
    };

    const applyFilters = () => {
        let filtered = [...history];

        if (filter !== 'ALL') {
            filtered = filtered.filter(item => item.operation === filter);
        }

        if (searchTerm) {
            filtered = filtered.filter(item =>
                item.operation?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                item.errorMessage?.toLowerCase().includes(searchTerm.toLowerCase())
            );
        }

        setFilteredHistory(filtered);
    };

    const operations = ['ALL', 'ADD', 'SUBTRACT', 'MULTIPLY', 'DIVIDE', 'COMPARE', 'CONVERT'];

    if (loading) {
        return (
            <div className="flex items-center justify-center h-96">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
            </div>
        );
    }

    return (
        <div className="space-y-6 animate-fadeIn">
            {/* Page Header */}
            <div className="flex justify-between items-center">
                <div>
                    <h1 className="text-3xl font-bold text-white">Operation History</h1>
                    <p className="text-gray-400 mt-1">View all your measurement operations</p>
                </div>
                <button
                    onClick={fetchHistory}
                    className="p-2 rounded-lg bg-white/5 hover:bg-white/10 transition-all duration-200"
                >
                    <FiRefreshCw className="w-5 h-5 text-gray-400" />
                </button>
            </div>

            {/* Filters */}
            <div className="flex flex-col md:flex-row gap-4">
                {/* Search */}
                <div className="flex-1 relative">
                    <FiSearch className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-500" />
                    <input
                        type="text"
                        placeholder="Search operations..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className="w-full pl-10 pr-4 py-2 rounded-lg bg-white/5 border border-white/10 text-white focus:outline-none focus:border-blue-500 transition-all duration-200"
                    />
                </div>

                {/* Filter Buttons */}
                <div className="flex gap-2 overflow-x-auto pb-2">
                    {operations.map((op) => (
                        <button
                            key={op}
                            onClick={() => setFilter(op)}
                            className={`px-4 py-2 rounded-lg text-sm transition-all duration-200 whitespace-nowrap ${
                                filter === op
                                    ? 'bg-gradient-to-r from-blue-500 to-purple-500 text-white'
                                    : 'bg-white/5 text-gray-400 hover:bg-white/10'
                            }`}
                        >
                            {op === 'ALL' ? 'All' : op}
                        </button>
                    ))}
                </div>
            </div>

            {/* History Table */}
            <div className="glass-card rounded-xl overflow-hidden">
                <div className="overflow-x-auto">
                    <table className="w-full">
                        <thead className="bg-white/5 border-b border-white/10">
                        <tr>
                            <th className="px-6 py-4 text-left text-xs font-medium text-gray-400 uppercase tracking-wider">Operation</th>
                            <th className="px-6 py-4 text-left text-xs font-medium text-gray-400 uppercase tracking-wider">Input</th>
                            <th className="px-6 py-4 text-left text-xs font-medium text-gray-400 uppercase tracking-wider">Result</th>
                            <th className="px-6 py-4 text-left text-xs font-medium text-gray-400 uppercase tracking-wider">Status</th>
                            <th className="px-6 py-4 text-left text-xs font-medium text-gray-400 uppercase tracking-wider">Timestamp</th>
                        </tr>
                        </thead>
                        <tbody className="divide-y divide-white/5">
                        {filteredHistory.length > 0 ? (
                            filteredHistory.map((item, index) => (
                                <tr key={index} className="hover:bg-white/5 transition-all duration-200">
                                    <td className="px-6 py-4 whitespace-nowrap">
                                        <span className="text-sm text-white">{formatOperationType(item.operation)}</span>
                                    </td>
                                    <td className="px-6 py-4">
                                        <div className="text-sm text-gray-300">
                                            {item.inputValue !== null ? `${item.inputValue} ${item.inputUnit}` : '-'}
                                            {item.secondValue && `, ${item.secondValue} ${item.secondUnit}`}
                                        </div>
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap">
                                        <div className="text-sm text-gray-300">
                                            {item.isError ? item.errorMessage : formatResult(item.resultValue)}
                                        </div>
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`px-2 py-1 rounded-full text-xs ${
                          item.isError
                              ? 'bg-red-500/20 text-red-400'
                              : 'bg-green-500/20 text-green-400'
                      }`}>
                        {item.isError ? 'Failed' : 'Success'}
                      </span>
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                        {formatDate(item.createdAt)}
                                    </td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="5" className="px-6 py-12 text-center text-gray-500">
                                    No history found
                                </td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                </div>
            </div>

            {/* Summary */}
            <div className="glass rounded-lg p-4">
                <div className="flex justify-between items-center">
                    <p className="text-sm text-gray-400">
                        Showing {filteredHistory.length} of {history.length} operations
                    </p>
                    <div className="flex gap-4">
                        <div className="flex items-center gap-2">
                            <div className="w-3 h-3 rounded-full bg-green-500"></div>
                            <span className="text-xs text-gray-400">Success: {history.filter(h => !h.isError).length}</span>
                        </div>
                        <div className="flex items-center gap-2">
                            <div className="w-3 h-3 rounded-full bg-red-500"></div>
                            <span className="text-xs text-gray-400">Failed: {history.filter(h => h.isError).length}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default History;