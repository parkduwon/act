import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
    AlertDialogTrigger,
} from "@/components/ui/alert-dialog";
import { Save } from "lucide-react";
import { formatCurrency, parseCurrency } from "@/utils/currency";
import type { TradeSettings } from "@/api/bot";

interface BidTradeTabProps {
    tradeSettings: TradeSettings | null;
    setTradeSettings: React.Dispatch<React.SetStateAction<TradeSettings | null>>;
    saveTradeSettings: () => Promise<void>;
    saving: boolean;
}

export default function BidTradeTab({ tradeSettings, setTradeSettings, saveTradeSettings, saving }: BidTradeTabProps) {
    return (
        <Card>
            <CardHeader className="pb-3 flex flex-row items-center justify-between">
                <CardTitle className="text-lg">매도 거래 설정</CardTitle>
                <AlertDialog>
                    <AlertDialogTrigger asChild>
                        <Button
                            size="sm"
                            disabled={saving}
                            className="gap-2"
                        >
                            <Save className="h-4 w-4"/>
                            <span>저장</span>
                        </Button>
                    </AlertDialogTrigger>
                    <AlertDialogContent className="max-w-xs rounded-lg">
                        <AlertDialogHeader>
                            <AlertDialogTitle className="text-base">설정 저장</AlertDialogTitle>
                            <AlertDialogDescription className="text-sm">
                                변경사항을 저장하시겠습니까?
                            </AlertDialogDescription>
                        </AlertDialogHeader>
                        <AlertDialogFooter>
                            <AlertDialogCancel>취소</AlertDialogCancel>
                            <AlertDialogAction onClick={saveTradeSettings}>확인</AlertDialogAction>
                        </AlertDialogFooter>
                    </AlertDialogContent>
                </AlertDialog>
            </CardHeader>
            <CardContent className="space-y-4">
                <div className="grid grid-cols-1 gap-4">
                    <div className="grid grid-cols-2 gap-2">
                        <div>
                            <Label htmlFor="bidTradeSwitch">매도거래 스위치</Label>
                            <Select
                                value={tradeSettings?.bidTradeSwitch ? "on" : "off"}
                                onValueChange={(value) => setTradeSettings(prev => prev ? {...prev, bidTradeSwitch: value === "on"} : null)}
                            >
                                <SelectTrigger>
                                    <SelectValue placeholder="선택하세요"/>
                                </SelectTrigger>
                                <SelectContent>
                                    <SelectItem value="on">ON</SelectItem>
                                    <SelectItem value="off">OFF</SelectItem>
                                </SelectContent>
                            </Select>
                        </div>
                        <div>
                            <Label htmlFor="bidTradeScheduleRate">거래체결 간격(초)</Label>
                            <Input
                                id="bidTradeScheduleRate"
                                type="number"
                                className="text-right"
                                value={tradeSettings?.bidTradeScheduleRate || 5}
                                onChange={(e) => setTradeSettings(prev => prev ? {...prev, bidTradeScheduleRate: parseInt(e.target.value)} : null)}
                            />
                        </div>
                    </div>
                    <div className="grid grid-cols-2 gap-2">
                        <div>
                            <Label htmlFor="bidTradeDollar">거래주문량</Label>
                            <Input
                                id="bidTradeDollar"
                                type="text"
                                className="text-right"
                                value={formatCurrency(tradeSettings?.bidTradeDollar, "USDT")}
                                onChange={(e) => setTradeSettings(prev => prev ? {...prev, bidTradeDollar: parseCurrency(e.target.value)} : null)}
                            />
                            <p className="text-xs text-gray-500 mt-1">최소 5 USDT 이상</p>
                        </div>
                        <div>
                            <Label htmlFor="boundDollarBidTrade">거래주문량 랜덤범위</Label>
                            <Input
                                id="boundDollarBidTrade"
                                type="text"
                                className="text-right"
                                value={formatCurrency(tradeSettings?.boundDollar, "USDT")}
                                onChange={(e) => setTradeSettings(prev => prev ? {...prev, boundDollar: parseCurrency(e.target.value)} : null)}
                            />
                            <p className="text-xs text-gray-500 mt-1">주문량+랜덤범위=실제 주문량</p>
                        </div>
                    </div>
                </div>
            </CardContent>
        </Card>
    );
}