import axios from 'axios';

// Vite uses import.meta.env instead of process.env
const AUTH_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

export const authApi = {
    // Redirect to Google OAuth
    loginWithGoogle: () => {
        window.location.href = `${AUTH_BASE_URL}/oauth2/authorization/google`;
    },

    // Store token after OAuth success
    handleAuthSuccess: (token) => {
        localStorage.setItem('auth_token', token);
    },

    // Logout
    logout: () => {
        localStorage.removeItem('auth_token');
        localStorage.removeItem('user');
        window.location.href = '/';
    },

    // Check if user is authenticated
    isAuthenticated: () => {
        const token = localStorage.getItem('auth_token');
        return !!token;
    },

    // Get token
    getToken: () => {
        return localStorage.getItem('auth_token');
    },
};