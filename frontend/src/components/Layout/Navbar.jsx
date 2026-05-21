import React from 'react';
import { FiMenu, FiBell, FiUser, FiLogOut } from 'react-icons/fi';
import { useAuth } from '../../hooks/useAuth';
import toast from 'react-hot-toast';

const Navbar = ({ sidebarOpen, setSidebarOpen }) => {
    const { isAuthenticated, user, logout } = useAuth();
    const [showDropdown, setShowDropdown] = React.useState(false);

    const handleLogout = () => {
        logout();
        toast.success('Logged out successfully');
    };

    return (
        <nav className="glass sticky top-0 z-40 px-6 py-4">
            <div className="flex items-center justify-between">
                <div className="flex items-center gap-4">
                    <button
                        onClick={() => setSidebarOpen(!sidebarOpen)}
                        className="p-2 rounded-lg hover:bg-white/10 transition-all duration-200"
                    >
                        <FiMenu className="w-5 h-5 text-gray-400" />
                    </button>
                    <div>
                        <h1 className="text-xl font-semibold gradient-text">
                            Quantity Measurement System
                        </h1>
                        <p className="text-xs text-gray-500 mt-0.5">
                            Precision Measurement Tool
                        </p>
                    </div>
                </div>

                <div className="flex items-center gap-4">

                    {/* User Profile */}
                    {isAuthenticated ? (
                        <div className="relative">
                            <button
                                onClick={() => setShowDropdown(!showDropdown)}
                                className="flex items-center gap-3 p-2 rounded-lg hover:bg-white/10 transition-all duration-200"
                            >
                                <div className="w-8 h-8 rounded-full bg-gradient-to-r from-blue-500 to-purple-500 flex items-center justify-center">
                                    <FiUser className="w-4 h-4 text-white" />
                                </div>
                                <span className="text-sm text-gray-300 hidden md:block">
                  {user?.name || 'User'}
                </span>
                            </button>

                            {showDropdown && (
                                <div className="absolute right-0 mt-2 w-48 glass rounded-lg shadow-lg overflow-hidden z-50">
                                    <button
                                        onClick={handleLogout}
                                        className="w-full px-4 py-3 text-left text-sm text-gray-300 hover:bg-white/10 flex items-center gap-2 transition-all duration-200"
                                    >
                                        <FiLogOut className="w-4 h-4" />
                                        Logout
                                    </button>
                                </div>
                            )}
                        </div>
                    ) : (
                        <button
                            onClick={() => {}}
                            className="px-4 py-2 bg-gradient-to-r from-blue-500 to-purple-500 rounded-lg text-white text-sm font-medium hover:opacity-90 transition-all duration-200"
                        >
                            Sign In
                        </button>
                    )}
                </div>
            </div>
        </nav>
    );
};

export default Navbar;