/**
 * Available coins for follow trading
 */
export const followCoinOptions = ['LTC', 'TRX'] as const;

export type FollowCoin = typeof followCoinOptions[number];