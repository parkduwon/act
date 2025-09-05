import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card";
import {useEffect, useState} from "react";
import {api} from "@/api/client";

interface TradingInfo {
    priceInfo: {
        currentPrice: number;
        changePercent: number;
        volume24h: number;
    };
    ldkBalance: {
        currency: string;
        free: number;
        locked: number;
        total: number;
    };
    usdtBalance: {
        currency: string;
        free: number;
        locked: number;
        total: number;
    };
}

export default function TradingInfoCard() {
    const [tradingInfo, setTradingInfo] = useState<TradingInfo | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        void fetchTradingInfo();
        const interval = setInterval(fetchTradingInfo, 5000); // 5초마다 업데이트
        return () => clearInterval(interval);
    }, []);

    const fetchTradingInfo = async () => {
        try {
            const response = await api.get<TradingInfo>("/trading/info", {
                params: { symbol: "ldk_usdt" }
            });
            setTradingInfo(response.data);
        } catch (error) {
            console.error("Failed to fetch trading info:", error);
        } finally {
            setLoading(false);
        }
    };

    const formatNumber = (num: number, decimals: number = 2) => {
        // 천단위 콤마 추가
        return num.toLocaleString('en-US', {
            minimumFractionDigits: decimals,
            maximumFractionDigits: decimals
        });
    };

    if (loading) {
        return (
            <Card className="mb-4">
                <CardHeader className="pb-2">
                    <CardTitle className="text-sm font-medium text-gray-600">거래 정보</CardTitle>
                </CardHeader>
                <CardContent>
                    <div className="animate-pulse grid grid-cols-3 gap-4">
                        <div className="h-8 bg-gray-200 rounded"></div>
                        <div className="h-8 bg-gray-200 rounded"></div>
                        <div className="h-8 bg-gray-200 rounded"></div>
                    </div>
                </CardContent>
            </Card>
        );
    }

    return (
        <Card className="mb-4">
            <CardHeader className="py-2 flex flex-row items-center justify-between">
                <CardTitle className="text-xs font-medium text-gray-600">거래 정보</CardTitle>
                <span className="text-xs font-semibold">
                    LDK: ${tradingInfo?.priceInfo.currentPrice?.toFixed(5) || "0.00000"}
                </span>
            </CardHeader>
            <CardContent className="pb-3">
                <div className="grid grid-cols-3 gap-x-4 gap-y-1 text-xs">
                    <div className="text-gray-500 font-medium">코인</div>
                    <div className="text-gray-500 font-medium text-right">Free</div>
                    <div className="text-gray-500 font-medium text-right">Lock</div>
                    
                    <div className="font-semibold">LDK</div>
                    <div className="text-right">{formatNumber(tradingInfo?.ldkBalance.free || 0, 2)}</div>
                    <div className="text-right">{formatNumber(tradingInfo?.ldkBalance.locked || 0, 2)}</div>
                    
                    <div className="font-semibold">USDT</div>
                    <div className="text-right">{formatNumber(tradingInfo?.usdtBalance.free || 0, 2)}</div>
                    <div className="text-right">{formatNumber(tradingInfo?.usdtBalance.locked || 0, 2)}</div>
                </div>
            </CardContent>
        </Card>
    );
}