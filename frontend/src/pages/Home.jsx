import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import {
    FiArrowRight,
    FiZap,
    FiShield,
    FiTrendingUp,
    FiGitBranch
} from 'react-icons/fi';

const Home = () => {
    const { isAuthenticated, login } = useAuth();
    const navigate = useNavigate();

    const features = [
        {
            icon: FiZap,
            title: 'Fast Conversions',
            description: 'Real-time unit conversions with high precision',
            color: 'from-yellow-500 to-orange-500',
        },
        {
            icon: FiShield,
            title: 'Accurate Results',
            description: 'IEEE 754 compliant arithmetic operations',
            color: 'from-green-500 to-emerald-500',
        },
        {
            icon: FiTrendingUp,
            title: 'Operation History',
            description: 'Track all your calculations with timestamps',
            color: 'from-blue-500 to-cyan-500',
        },
        {
            icon: FiGitBranch,
            title: 'Multiple Categories',
            description: 'Length, Weight, Volume, and Temperature',
            color: 'from-purple-500 to-pink-500',
        },
    ];

    return (
        <div className="animate-fadeIn">
            {/* Hero Section */}
            <div className="text-center mb-12">
                <div className="inline-block px-3 py-1 rounded-full bg-blue-500/10 border border-blue-500/20 mb-6">
                    <span className="text-sm text-blue-400">Spring Boot Backend</span>
                </div>
                <h1 className="text-5xl md:text-6xl font-bold mb-6">
                    <span className="gradient-text">Quantity Measurement</span>
                    <br />
                    <span className="text-white">System</span>
                </h1>
                <p className="text-lg text-gray-400 max-w-2xl mx-auto mb-8">
                    A comprehensive solution for unit conversions, arithmetic operations on quantities,
                    and measurement comparisons with OAuth2 authentication.
                </p>
                <div className="flex gap-4 justify-center">
                    {!isAuthenticated ? (
                        <button
                            onClick={login}
                            className="px-6 py-3 bg-gradient-to-r from-blue-500 to-purple-500 rounded-lg text-white font-medium hover:opacity-90 transition-all duration-200 flex items-center gap-2"
                        >
                            Get Started
                            <FiArrowRight className="w-4 h-4" />
                        </button>
                    ) : (
                        <button
                            onClick={() => navigate('/dashboard')}
                            className="px-6 py-3 bg-gradient-to-r from-blue-500 to-purple-500 rounded-lg text-white font-medium hover:opacity-90 transition-all duration-200 flex items-center gap-2"
                        >
                            Go to Dashboard
                            <FiArrowRight className="w-4 h-4" />
                        </button>
                    )}
                </div>
            </div>

            {/* Features Grid */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mt-16">
                {features.map((feature, index) => (
                    <div
                        key={index}
                        className="glass-card rounded-xl p-6 text-center hover:transform hover:-translate-y-1 transition-all duration-300"
                    >
                        <div className={`w-12 h-12 rounded-lg bg-gradient-to-r ${feature.color} flex items-center justify-center mx-auto mb-4`}>
                            <feature.icon className="w-6 h-6 text-white" />
                        </div>
                        <h3 className="text-lg font-semibold text-white mb-2">{feature.title}</h3>
                        <p className="text-sm text-gray-400">{feature.description}</p>
                    </div>
                ))}
            </div>

            {/* Supported Units Section */}
            <div className="mt-16 glass rounded-xl p-8">
                <h2 className="text-2xl font-bold text-white text-center mb-8">Supported Measurement Categories</h2>
                <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
                    {['Length', 'Weight', 'Volume', 'Temperature'].map((category) => (
                        <div key={category} className="text-center">
                            <div className="w-16 h-16 rounded-full bg-gradient-to-r from-blue-500/20 to-purple-500/20 flex items-center justify-center mx-auto mb-3">
                <span className="text-2xl">
                  {category === 'Length' && '📏'}
                    {category === 'Weight' && '⚖️'}
                    {category === 'Volume' && '🧪'}
                    {category === 'Temperature' && '🌡️'}
                </span>
                            </div>
                            <p className="text-white font-medium">{category}</p>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default Home;