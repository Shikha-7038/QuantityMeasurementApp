export const formatQuantity = (value, unit) => {
    if (value === undefined || value === null) return 'N/A';
    const formattedValue = typeof value === 'number'
        ? value.toFixed(6).replace(/\.?0+$/, '')
        : value;
    return `${formattedValue} ${unit}`;
};

export const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleString();
};

export const formatOperationType = (operation) => {
    const map = {
        ADD: '➕ Addition',
        SUBTRACT: '➖ Subtraction',
        MULTIPLY: '✖️ Multiplication',
        DIVIDE: '➗ Division',
        COMPARE: '⚖️ Compare',
        CONVERT: '🔄 Convert',
    };
    return map[operation] || operation;
};

export const formatResult = (result) => {
    if (result === null || result === undefined) return 'N/A';
    if (typeof result === 'boolean') return result ? 'Equal ✅' : 'Not Equal ❌';
    return result;
};