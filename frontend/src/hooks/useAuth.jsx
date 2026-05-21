import React, { createContext, useContext, useState, useEffect } from 'react';
import { authApi } from '../api/authApi';

const AuthContext = createContext();

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within AuthProvider');
    }
    return context;
};

export const AuthProvider = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // Check for token in URL (after OAuth redirect)
        const urlParams = new URLSearchParams(window.location.search);
        const token = urlParams.get('token');

        if (token) {
            authApi.handleAuthSuccess(token);
            window.history.replaceState({}, document.title, window.location.pathname);
            setIsAuthenticated(true);
        } else {
            setIsAuthenticated(authApi.isAuthenticated());
        }

        setLoading(false);
    }, []);

    const login = () => {
        authApi.loginWithGoogle();
    };

    const logout = () => {
        authApi.logout();
        setIsAuthenticated(false);
        setUser(null);
    };

    return (
        <AuthContext.Provider
            value={{
                isAuthenticated,
                user,
                loading,
                login,
                logout,
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};