import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion";
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
import { followCoinOptions } from "@/components/FollowCoinOptions";
import { CurrencyInput } from "@/components/ui/currency-input";
import type { TradeSettings } from "@/api/bot";
import TradingInfoCard from "./TradingInfoCard";
import React from "react";

interface TradeTabProps {
    tradeSettings: TradeSettings | null;
    setTradeSettings: React.Dispatch<React.SetStateAction<TradeSettings | null>>;
    saveTradeSettings: () => Promise<void>;
    saving: boolean;
}

export default function TradeTab({ tradeSettings, setTradeSettings, saveTradeSettings, saving }: TradeTabProps) {
    return (
        <>
        <TradingInfoCard />
        <Card>
            <CardHeader className="pb-1 flex flex-row items-center justify-between">
                <CardTitle className="text-lg">거래 설정</CardTitle>
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
            <CardContent>
                <div className="grid grid-cols-1 gap-1">
                    <div className="grid grid-cols-2 gap-2">
                        <div>
                            <Label htmlFor="followCoinEnabled">특정 코인 거래가격 추종</Label>
                            <Select
                                value={tradeSettings?.followCoinEnabled ? "enabled" : "disabled"}
                                onValueChange={(value) => {
                                    const enabled = value === "enabled";
                                    setTradeSettings(prev => prev ? {
                                        ...prev,
                                        followCoinEnabled: enabled,
                                        followCoin: enabled ? prev.followCoin : ""
                                    } : null);
                                }}
                            >
                                <SelectTrigger>
                                    <SelectValue placeholder="선택하세요"/>
                                </SelectTrigger>
                                <SelectContent>
                                    <SelectItem value="disabled">비활성화</SelectItem>
                                    <SelectItem value="enabled">활성화</SelectItem>
                                </SelectContent>
                            </Select>
                        </div>
                        <div>
                            <Label htmlFor="followCoinSelect">거래가격 추종 대상 코인</Label>
                            <Select
                                disabled={!tradeSettings?.followCoinEnabled}
                                value={tradeSettings?.followCoin || ""}
                                onValueChange={(value) => {
                                    setTradeSettings(prev => prev ? {
                                        ...prev,
                                        followCoin: value
                                    } : null);
                                }}
                            >
                                <SelectTrigger>
                                    <SelectValue placeholder="선택하세요"/>
                                </SelectTrigger>
                                <SelectContent>
                                    {followCoinOptions.map(coin => (
                                        <SelectItem key={coin} value={coin}>{coin}</SelectItem>
                                    ))}
                                </SelectContent>
                            </Select>
                        </div>
                    </div>
                    {tradeSettings?.followCoinEnabled && !tradeSettings?.followCoin && (
                        <p className="text-xs text-red-500">추종 할 코인을 선택하세요</p>
                    )}
                    {tradeSettings?.followCoinEnabled && tradeSettings?.followCoin && (
                        <p className="text-xs text-gray-500">LDK/{tradeSettings?.followCoin} 비율을 유지하도록 자동 매매</p>
                    )}
                    {tradeSettings?.followCoinEnabled && tradeSettings?.followCoin && (
                        <Accordion type="single" collapsible className="w-full py-0">
                            <AccordionItem value="follow-info">
                                <AccordionTrigger>추종 코인 정보 (조회전용)</AccordionTrigger>
                                <AccordionContent>
                                    <div className="space-y-2">
                                        <div>
                                            <Label>추종비율</Label>
                                            <Input
                                                value={tradeSettings?.followCoinRate?.toFixed(8) || "첫 실행시 자동 설정"}
                                                disabled
                                                className="bg-gray-100"
                                            />
                                            <p className="text-xs text-gray-500 mt-1">LDK/{tradeSettings?.followCoin} 비율 (첫 실행시 자동 계산)</p>
                                        </div>
                                        <div>
                                            <Label>{tradeSettings?.followCoin} 현재 가격</Label>
                                            <Input
                                                value={tradeSettings?.followCoinCurrentPrice?.toFixed(4) || "-"}
                                                disabled
                                                className="bg-gray-100"
                                            />
                                            <p className="text-xs text-gray-500 mt-1">추종 코인의 현재 가격</p>
                                        </div>
                                        <div>
                                            <Label>추종 목표 가격</Label>
                                            <Input
                                                value={tradeSettings?.followTargetPrice?.toFixed(8) || "-"}
                                                disabled
                                                className="bg-gray-100"
                                            />
                                            <p className="text-xs text-gray-500 mt-1">비율에 따른 LDK 목표 가격</p>
                                        </div>
                                        <div>
                                            <Label>추종가격공식</Label>
                                            <Textarea
                                                value={tradeSettings?.followCoinRateFormula || "첫 실행시 자동 생성"}
                                                disabled
                                                className="bg-gray-100 text-sm resize-none"
                                                rows={2}
                                            />
                                        </div>
                                    </div>
                                </AccordionContent>
                            </AccordionItem>
                        </Accordion>
                    )}
                    {/* Row 1: 목표가격, 최저 USDT 개수 */}
                    <div className="grid grid-cols-2 gap-2">
                        <div>
                            <Label htmlFor="targetPrice">목표가격</Label>
                            <CurrencyInput
                                id="targetPrice"
                                value={tradeSettings?.targetPrice}
                                onChange={(value) => setTradeSettings(prev => prev ? {...prev, targetPrice: value} : null)}
                                currency="LDK"
                            />
                        </div>
                        <div>
                            <Label htmlFor="minUsdtQuantity">최저 USDT 개수</Label>
                            <CurrencyInput
                                id="minUsdtQuantity"
                                value={tradeSettings?.minUsdtQuantity}
                                onChange={(value) => setTradeSettings(prev => prev ? {...prev, minUsdtQuantity: value} : null)}
                                currency="USDT"
                            />
                            <p className="text-xs text-gray-500 mt-1">이 수량 이하시 LDK 강제 매도</p>
                        </div>
                    </div>
                    {/* Row 2: 최대 거래가격, 최소 거래가격 */}
                    <div className="grid grid-cols-2 gap-2">
                        <div>
                            <Label htmlFor="maxTradePrice">최대 거래가격</Label>
                            <CurrencyInput
                                id="maxTradePrice"
                                value={tradeSettings?.maxTradePrice}
                                onChange={(value) => setTradeSettings(prev => prev ? {...prev, maxTradePrice: value} : null)}
                                currency="LDK"
                            />
                        </div>
                        <div>
                            <Label htmlFor="minTradePrice">최소 거래가격</Label>
                            <CurrencyInput
                                id="minTradePrice"
                                value={tradeSettings?.minTradePrice}
                                onChange={(value) => setTradeSettings(prev => prev ? {...prev, minTradePrice: value} : null)}
                                currency="LDK"
                            />
                        </div>
                    </div>
                    {/* Row 3: 주문수량 랜덤 범위, 랜덤 확률 */}
                    <div className="grid grid-cols-2 gap-2">
                        <div>
                            <Label htmlFor="boundDollar">주문수량 랜덤수량 가격</Label>
                            <CurrencyInput
                                id="boundDollar"
                                value={tradeSettings?.boundDollar}
                                onChange={(value) => setTradeSettings(prev => prev ? {...prev, boundDollar: value} : null)}
                                currency="USDT"
                            />
                            <p className="text-xs text-gray-500 mt-1">주문량=최소수량+랜덤수량</p>
                        </div>
                        <div>
                            <Label htmlFor="randomTryPercent">주문 실행 확률(%)</Label>
                            <Input
                                id="randomTryPercent"
                                type="number"
                                className="text-right"
                                value={tradeSettings?.randomTryPercent || 0}
                                onChange={(e) => setTradeSettings(prev => prev ? {...prev, randomTryPercent: parseInt(e.target.value)} : null)}
                            />
                            <p className="text-xs text-gray-500 mt-1">거래 주문이 확율로 실행된다.</p>
                        </div>
                    </div>
                </div>
            </CardContent>
        </Card>
        </>
    );
}