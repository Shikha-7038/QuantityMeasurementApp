import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { AuthProvider } from './hooks/useAuth';
import Navbar from './components/Layout/Navbar';
import Sidebar from './components/Layout/Sidebar';
import Home from './pages/Home';
import Dashboard from './pages/Dashboard';
import Operations from './pages/Operations';
import History from './pages/History';
import './App.css';

function App() {
    const [sidebarOpen, setSidebarOpen] = useState(true);

    return (
        <AuthProvider>
            <Router>
                <Toaster
                    position="top-right"
                    toastOptions={{
                        duration: 4000,
                        style: {
                            background: '#1e1e2f',
                            color: '#fff',
                            border: '1px solid #3b82f6',
                        },
                    }}
                />
                <div className="flex h-screen bg-dark-300">
                    <Sidebar isOpen={sidebarOpen} setIsOpen={setSidebarOpen} />
                    <div className="flex-1 flex flex-col overflow-hidden">
                        <Navbar sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen} />
                        <main className="flex-1 overflow-y-auto p-6">
                            <Routes>
                                <Route path="/" element={<Home />} />
                                <Route path="/dashboard" element={<Dashboard />} />
                                <Route path="/operations" element={<Operations />} />
                                <Route path="/history" element={<History />} />
                                <Route path="*" element={<Navigate to="/" />} />
                            </Routes>
                        </main>
                    </div>
                </div>
            </Router>
        </AuthProvider>
    );
}

export default App;