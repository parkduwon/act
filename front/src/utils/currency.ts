/**
 * Currency formatting utilities
 */

export type Currency = "USDT" | "LDK";

/**
 * Format a number value with currency symbol
 * @param value - The numeric value to format
 * @param currency - The currency type ('USDT' or 'LDK')
 * @returns Formatted string with currency symbol
 */
export function formatCurrency(value: number | undefined | null, currency: Currency): string {
    if (value === undefined || value === null) return `${currency} 0`;

    let formatted: string;
    if (currency === "USDT") {
        // USDT는 소수점 없이 정수로 표시
        formatted = value.toLocaleString("en-US", {
            minimumFractionDigits: 0,
            maximumFractionDigits: 0
        });
    } else {
        // LDK는 소수점 5자리까지 표시
        formatted = value.toLocaleString("en-US", {
            minimumFractionDigits: 5,
            maximumFractionDigits: 5
        });
    }

    return `${currency} ${formatted}`;
}

/**
 * Parse a formatted currency string back to number
 * @param value - The formatted string to parse
 * @returns Numeric value or 0 if invalid
 */
export function parseCurrency(value: string): number {
    if (!value) return 0;

    // Remove currency symbols and commas
    const cleaned = value.replace(/[^0-9.-]/g, "");
    const parsed = parseFloat(cleaned);

    return isNaN(parsed) ? 0 : parsed;
}