import apiClient from './client';

export const quantityApi = {
    // Compare two quantities
    compare: async (quantity1, quantity2) => {
        const response = await apiClient.post('/compare', [quantity1, quantity2]);
        return response.data;
    },

    // Convert quantity to target unit
    convert: async (quantity, targetUnit) => {
        const response = await apiClient.post('/convert', quantity, {
            params: { targetUnit },
        });
        return response.data;
    },

    // Add two quantities
    add: async (quantity1, quantity2, targetUnit) => {
        const response = await apiClient.post('/add', [quantity1, quantity2], {
            params: { targetUnit },
        });
        return response.data;
    },

    // Subtract two quantities
    subtract: async (quantity1, quantity2, targetUnit) => {
        const response = await apiClient.post('/subtract', [quantity1, quantity2], {
            params: { targetUnit },
        });
        return response.data;
    },

    // Multiply quantity by multiplier
    multiply: async (quantity, multiplier) => {
        const response = await apiClient.post('/multiply', quantity, {
            params: { multiplier },
        });
        return response.data;
    },

    // Divide quantity by divisor
    divide: async (quantity, divisor) => {
        const response = await apiClient.post('/divide', quantity, {
            params: { divisor },
        });
        return response.data;
    },

    // Get all history
    getHistory: async () => {
        const response = await apiClient.get('/history');
        return response.data;
    },

    // Get history by operation
    getHistoryByOperation: async (operation) => {
        const response = await apiClient.get(`/history/operation/${operation}`);
        return response.data;
    },

    // Get error history
    getErrorHistory: async () => {
        const response = await apiClient.get('/history/errored');
        return response.data;
    },

    // Get operation count
    getOperationCount: async (operation) => {
        const response = await apiClient.get(`/count/${operation}`);
        return response.data;
    },
};