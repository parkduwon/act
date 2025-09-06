import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {Tabs, TabsContent, TabsList, TabsTrigger} from "@/components/ui/tabs";
import type {BotStatus, ForceTradeSettings, OrderBookSettings, TradeSettings} from "@/api/bot";
import {botApi} from "@/api/bot";
import {authApi} from "@/api/auth";
import {toast} from "sonner";

// Import separated components
import Header from "./settings/Header";
import TradeTab from "./settings/TradeTab";
import OrderBookTab from "./settings/OrderBookTab";
import BidTradeTab from "./settings/BidTradeTab";
import ForceTradeTab from "./settings/ForceTradeTab";

const SYMBOL = "ldk_usdt";

export default function Settings() {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [botStatus, setBotStatus] = useState<BotStatus | null>(null);
    const [tradeSettings, setTradeSettings] = useState<TradeSettings | null>(null);
    const [orderBookSettings, setOrderBookSettings] = useState<OrderBookSettings | null>(null);
    const [forceTradeSettings, setForceTradeSettings] = useState<ForceTradeSettings | null>(null);

    useEffect(() => {
        void loadBotStatus();
    }, []);

    const loadBotStatus = async () => {
        try {
            setLoading(true);
            const status = await botApi.getStatus(SYMBOL);
            setBotStatus(status);
            setTradeSettings(status.tradeSettings);
            setOrderBookSettings(status.orderBookSettings);
            setForceTradeSettings(status.forceTradeSettings);
        } catch (error) {
            console.error("Failed to load bot status:", error);
            navigate("/");
        } finally {
            setLoading(false);
        }
    };

    const handleLogout = async () => {
        try {
            await authApi.logout();
            navigate("/");
        } catch (error) {
            console.error("Logout error:", error);
        }
    };

    const toggleBot = async () => {
        try {
            if (botStatus?.enabled) {
                await botApi.stop(SYMBOL);
            } else {
                await botApi.start(SYMBOL);
            }
            await loadBotStatus();
        } catch (error) {
            console.error("Failed to toggle bot:", error);
        }
    };

    const saveTradeSettings = async () => {
        if (!tradeSettings) return;
        try {
            setSaving(true);
            // 소수점 5자리로 절사
            const roundedSettings = {
                ...tradeSettings,
                targetPrice: Math.floor(tradeSettings.targetPrice * 100000) / 100000,
                maxTradePrice: Math.floor(tradeSettings.maxTradePrice * 100000) / 100000,
                minTradePrice: Math.floor(tradeSettings.minTradePrice * 100000) / 100000,
                minUsdtQuantity: Math.floor(tradeSettings.minUsdtQuantity * 100000) / 100000,
                boundDollar: Math.floor(tradeSettings.boundDollar * 100000) / 100000,
                bidTradeDollar: tradeSettings.bidTradeDollar ? Math.floor(tradeSettings.bidTradeDollar * 100000) / 100000 : tradeSettings.bidTradeDollar
            };
            await botApi.updateTradeSettings(roundedSettings);
            toast.success("거래 설정이 저장되었습니다.");
        } catch (error) {
            console.error("Failed to save trade settings:", error);
            toast.error("저장 실패");
        } finally {
            setSaving(false);
        }
    };

    const saveOrderBookSettings = async () => {
        if (!orderBookSettings) return;
        try {
            setSaving(true);
            // 소수점 5자리로 절사
            const roundedSettings = {
                ...orderBookSettings,
                askOrderBookStartPrice: Math.floor(orderBookSettings.askOrderBookStartPrice * 100000) / 100000,
                askOrderBookEndPrice: Math.floor(orderBookSettings.askOrderBookEndPrice * 100000) / 100000,
                bidOrderBookStartPrice: Math.floor(orderBookSettings.bidOrderBookStartPrice * 100000) / 100000,
                bidOrderBookEndPrice: Math.floor(orderBookSettings.bidOrderBookEndPrice * 100000) / 100000
            };
            await botApi.updateOrderBookSettings(roundedSettings);
            toast.success("호가창 설정이 저장되었습니다.");
        } catch (error) {
            console.error("Failed to save orderbook settings:", error);
            toast.error("저장 실패");
        } finally {
            setSaving(false);
        }
    };

    const saveForceTradeSettings = async () => {
        if (!forceTradeSettings) return;
        try {
            setSaving(true);
            await botApi.updateForceTradeSettings(forceTradeSettings);
            toast.success("강제 거래 설정이 저장되었습니다.");
        } catch (error) {
            console.error("Failed to save force trade settings:", error);
            toast.error("저장 실패");
        } finally {
            setSaving(false);
        }
    };

    if (loading || !botStatus) {
        return (
            <div className="min-h-screen flex items-center justify-center">
                <div className="text-lg">로딩 중...</div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 pb-20">
            {/* Header */}
            <Header 
                botStatus={botStatus}
                toggleBot={toggleBot}
                handleLogout={handleLogout}
            />

            {/* Settings */}
            <div className="p-4 space-y-4">

                {/* Settings Tabs */}
                <Tabs defaultValue="trade" className="w-full">
                    <TabsList className="grid w-full grid-cols-4">
                        <TabsTrigger value="trade">거래</TabsTrigger>
                        <TabsTrigger value="orderbook">호가창</TabsTrigger>
                        <TabsTrigger value="bidtrade">매도거래</TabsTrigger>
                        <TabsTrigger value="force">강제거래</TabsTrigger>
                    </TabsList>

                    {/* Trade Settings */}
                    <TabsContent value="trade" className="space-y-4">
                        <TradeTab
                            tradeSettings={tradeSettings}
                            setTradeSettings={setTradeSettings}
                            saveTradeSettings={saveTradeSettings}
                            saving={saving}
                        />
                    </TabsContent>

                    {/* OrderBook Settings */}
                    <TabsContent value="orderbook" className="space-y-4">
                        <OrderBookTab
                            orderBookSettings={orderBookSettings}
                            setOrderBookSettings={setOrderBookSettings}
                            saveOrderBookSettings={saveOrderBookSettings}
                            saving={saving}
                        />
                    </TabsContent>

                    {/* Bid Trade Settings */}
                    <TabsContent value="bidtrade" className="space-y-4">
                        <BidTradeTab
                            tradeSettings={tradeSettings}
                            setTradeSettings={setTradeSettings}
                            saveTradeSettings={saveTradeSettings}
                            saving={saving}
                        />
                    </TabsContent>

                    {/* Force Trade Settings */}
                    <TabsContent value="force" className="space-y-4">
                        <ForceTradeTab
                            forceTradeSettings={forceTradeSettings}
                            setForceTradeSettings={setForceTradeSettings}
                            saveForceTradeSettings={saveForceTradeSettings}
                            saving={saving}
                        />
                    </TabsContent>
                </Tabs>
            </div>
        </div>
    );
}