import React from 'react';
import { NavLink } from 'react-router-dom';
import {
    FiHome,
    FiActivity,
    FiTool,
    FiClock,
    FiBarChart2,
    FiChevronLeft,
    FiChevronRight
} from 'react-icons/fi';

const Sidebar = ({ isOpen, setIsOpen }) => {
    const menuItems = [
        { path: '/', icon: FiHome, label: 'Home' },
        { path: '/dashboard', icon: FiActivity, label: 'Dashboard' },
        { path: '/operations', icon: FiTool, label: 'Operations' },
        { path: '/history', icon: FiClock, label: 'History' },
    ];

    return (
        <aside
            className={`${
                isOpen ? 'w-64' : 'w-20'
            } glass transition-all duration-300 ease-in-out flex flex-col`}
        >
            {/* Logo Area */}
            <div className="p-6 border-b border-white/10">
                {isOpen ? (
                    <div className="flex items-center gap-2">
                        <div className="w-8 h-8 bg-gradient-to-r from-blue-500 to-purple-500 rounded-lg flex items-center justify-center">
                            <FiBarChart2 className="w-5 h-5 text-white" />
                        </div>
                        <span className="text-lg font-bold gradient-text"></span>
                    </div>
                ) : (
                    <div className="flex justify-center">
                        <div className="w-8 h-8 bg-gradient-to-r from-blue-500 to-purple-500 rounded-lg flex items-center justify-center">
                            <FiBarChart2 className="w-5 h-5 text-white" />
                        </div>
                    </div>
                )}
            </div>

            {/* Navigation Menu */}
            <nav className="flex-1 py-6">
                {menuItems.map((item) => (
                    <NavLink
                        key={item.path}
                        to={item.path}
                        className={({ isActive }) =>
                            `flex items-center gap-3 px-6 py-3 mx-3 rounded-lg transition-all duration-200 ${
                                isActive
                                    ? 'bg-gradient-to-r from-blue-500/20 to-purple-500/20 text-blue-400 border-l-2 border-blue-500'
                                    : 'text-gray-400 hover:bg-white/5 hover:text-white'
                            }`
                        }
                    >
                        <item.icon className="w-5 h-5" />
                        {isOpen && <span className="text-sm">{item.label}</span>}
                    </NavLink>
                ))}
            </nav>

            {/* Toggle Button */}
            <button
                onClick={() => setIsOpen(!isOpen)}
                className="p-4 m-3 rounded-lg bg-white/5 hover:bg-white/10 transition-all duration-200 flex items-center justify-center"
            >
                {isOpen ? (
                    <FiChevronLeft className="w-4 h-4 text-gray-400" />
                ) : (
                    <FiChevronRight className="w-4 h-4 text-gray-400" />
                )}
            </button>
        </aside>
    );
};

export default Sidebar;