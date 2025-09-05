import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: '/api',
  withCredentials: true,
});

export interface TradeSettings {
  symbol: string;
  mainCoin: string;
  quoteCoin: string;
  targetPrice: number;
  maxTradePrice: number;
  minTradePrice: number;
  minUsdtQuantity: number;
  boundDollar: number;
  randomTryPercent: number;
  followCoinEnabled: boolean;
  followCoin?: string;
  followCoinRate?: number;  // 추종비율 (자동계산)
  followCoinRatePrice?: number;  // 추종가격
  followCoinRateFormula?: string;  // 추종가격공식
  bidTradeSwitch?: boolean;  // 매도 거래 스위치
  bidTradeScheduleRate?: number;  // 거래 체결 간격(초)
  bidTradeDollar?: number;  // 거래 주문량
  enabled: boolean;
}

export interface OrderBookSettings {
  symbol: string;
  askOrderBookStartPrice: number;
  askOrderBookEndPrice: number;
  askOrderBookLimitCount: number;
  askStopYN: boolean;
  bidOrderBookStartPrice: number;
  bidOrderBookEndPrice: number;
  bidOrderBookLimitCount: number;
  bidStopYN: boolean;
  enabled: boolean;
}

export interface ForceTradeSettings {
  symbol: string;
  forceStopEnabled: boolean;
  forceType: 'NONE' | 'ALL' | 'BUY' | 'SELL';
  forceTradeType: 'NONE' | 'BUY' | 'SELL';
}

export interface BotStatus {
  symbol: string;
  tradeSettings: TradeSettings;
  orderBookSettings: OrderBookSettings;
  forceTradeSettings: ForceTradeSettings;
  enabled: boolean;
}

export const botApi = {
  getStatus: async (symbol: string): Promise<BotStatus> => {
    const response = await axiosInstance.get<BotStatus>('/bot/status', {
      params: { symbol }
    });
    return response.data;
  },

  start: async (symbol: string): Promise<void> => {
    await axiosInstance.post('/bot/start', null, {
      params: { symbol }
    });
  },

  stop: async (symbol: string): Promise<void> => {
    await axiosInstance.post('/bot/stop', null, {
      params: { symbol }
    });
  },

  forceStop: async (symbol: string): Promise<void> => {
    await axiosInstance.post('/bot/force-stop', null, {
      params: { symbol }
    });
  },

  updateTradeSettings: async (settings: TradeSettings): Promise<TradeSettings> => {
    const response = await axiosInstance.put<TradeSettings>('/bot/trade-settings', settings);
    return response.data;
  },

  updateOrderBookSettings: async (settings: OrderBookSettings): Promise<OrderBookSettings> => {
    const response = await axiosInstance.put<OrderBookSettings>('/bot/orderbook-settings', settings);
    return response.data;
  },

  updateForceTradeSettings: async (settings: ForceTradeSettings): Promise<ForceTradeSettings> => {
    const response = await axiosInstance.put<ForceTradeSettings>('/bot/force-trade-settings', settings);
    return response.data;
  },
};