import React, { useState, useEffect } from 'react';
import { FiBarChart2, FiCheckCircle, FiXCircle, FiClock } from 'react-icons/fi';
import MetricCard from '../components/Dashboard/MetricCard';
import StatsChart from '../components/Dashboard/StatsChart';
import { quantityApi } from '../api/quantityApi';

const Dashboard = () => {
    const [stats, setStats] = useState({
        totalOperations: 0,
        successfulOps: 0,
        failedOps: 0,
        avgResponseTime: '0ms',
    });
    const [loading, setLoading] = useState(true);
    const [recentActivities, setRecentActivities] = useState([]);

    useEffect(() => {
        fetchDashboardData();
    }, []);

    const fetchDashboardData = async () => {
        try {
            const [history, errors] = await Promise.all([
                quantityApi.getHistory(),
                quantityApi.getErrorHistory(),
            ]);

            const total = history.length;
            const failed = errors.length;
            const successful = total - failed;

            setStats({
                totalOperations: total,
                successfulOps: successful,
                failedOps: failed,
                avgResponseTime: '145ms',
            });

            setRecentActivities(history.slice(0, 5));
        } catch (error) {
            console.error('Failed to fetch dashboard data:', error);
        } finally {
            setLoading(false);
        }
    };

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
            <div>
                <h1 className="text-3xl font-bold text-white">Dashboard</h1>
                <p className="text-gray-400 mt-1">Overview of your measurement operations</p>
            </div>

            {/* Metrics Grid */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                <MetricCard
                    title="Total Operations"
                    value={stats.totalOperations}
                    icon={FiBarChart2}
                    trend={12}
                    color="blue"
                />
                <MetricCard
                    title="Successful"
                    value={stats.successfulOps}
                    icon={FiCheckCircle}
                    trend={8}
                    color="green"
                />
                <MetricCard
                    title="Failed"
                    value={stats.failedOps}
                    icon={FiXCircle}
                    trend={-5}
                    color="orange"
                />
                <MetricCard
                    title="Avg Response"
                    value={stats.avgResponseTime}
                    icon={FiClock}
                    color="purple"
                />
            </div>

            {/* Charts Section */}
            <StatsChart />

            {/* Recent Activities */}
            <div className="glass-card rounded-xl p-6">
                <h3 className="text-lg font-semibold text-white mb-4">Recent Activities</h3>
                <div className="space-y-3">
                    {recentActivities.length > 0 ? (
                        recentActivities.map((activity, index) => (
                            <div
                                key={index}
                                className="flex items-center justify-between p-3 rounded-lg bg-white/5 hover:bg-white/10 transition-all duration-200"
                            >
                                <div className="flex items-center gap-3">
                                    <div className={`w-2 h-2 rounded-full ${activity.isError ? 'bg-red-500' : 'bg-green-500'}`}></div>
                                    <span className="text-sm text-gray-300">
                    {activity.operation} operation
                  </span>
                                </div>
                                <span className="text-xs text-gray-500">
                  {activity.modifiedAt ? new Date(activity.modifiedAt).toLocaleTimeString() : 'Just now'}
                </span>
                            </div>
                        ))
                    ) : (
                        <p className="text-gray-500 text-center py-4">No activities yet</p>
                    )}
                </div>
            </div>
        </div>
    );
};

export default Dashboard;