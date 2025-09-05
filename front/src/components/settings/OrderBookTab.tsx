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
import type { OrderBookSettings } from "@/api/bot";

interface OrderBookTabProps {
    orderBookSettings: OrderBookSettings | null;
    setOrderBookSettings: React.Dispatch<React.SetStateAction<OrderBookSettings | null>>;
    saveOrderBookSettings: () => Promise<void>;
    saving: boolean;
}

export default function OrderBookTab({ orderBookSettings, setOrderBookSettings, saveOrderBookSettings, saving }: OrderBookTabProps) {
    return (
        <Card>
            <CardHeader className="pb-3 flex flex-row items-center justify-between">
                <CardTitle className="text-lg">호가창 설정</CardTitle>
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
                            <AlertDialogAction onClick={saveOrderBookSettings}>확인</AlertDialogAction>
                        </AlertDialogFooter>
                    </AlertDialogContent>
                </AlertDialog>
            </CardHeader>
            <CardContent className="space-y-4">
                {/* 매도 호가창 설정 */}
                <div>
                    <h3 className="text-sm font-semibold mb-3">매도 호가창</h3>
                    <div className="grid grid-cols-2 gap-2">
                        <div>
                            <Label htmlFor="askStop">매도 여부</Label>
                            <Select
                                value={orderBookSettings?.askStopYN ? "stop" : "active"}
                                onValueChange={(value) => setOrderBookSettings(prev => prev ? {...prev, askStopYN: value === "stop"} : null)}
                            >
                                <SelectTrigger>
                                    <SelectValue placeholder="선택하세요"/>
                                </SelectTrigger>
                                <SelectContent>
                                    <SelectItem value="active">활성</SelectItem>
                                    <SelectItem value="stop">중지</SelectItem>
                                </SelectContent>
                            </Select>
                        </div>
                        <div>
                            <Label htmlFor="askLimitCount">최대 개수</Label>
                            <Input
                                id="askLimitCount"
                                type="number"
                                className="text-right"
                                value={orderBookSettings?.askOrderBookLimitCount || 0}
                                onChange={(e) => setOrderBookSettings(prev => prev ? {...prev, askOrderBookLimitCount: parseInt(e.target.value)} : null)}
                            />
                        </div>
                    </div>
                    <div className="grid grid-cols-2 gap-2 mt-2">
                        <div>
                            <Label htmlFor="askStartPrice">호가 랜덤 범위 시작</Label>
                            <Input
                                id="askStartPrice"
                                type="text"
                                className="text-right"
                                value={formatCurrency(orderBookSettings?.askOrderBookStartPrice, "LDK")}
                                onChange={(e) => setOrderBookSettings(prev => prev ? {...prev, askOrderBookStartPrice: parseCurrency(e.target.value)} : null)}
                            />
                        </div>
                        <div>
                            <Label htmlFor="askEndPrice">호가 랜덤 종료 가격</Label>
                            <Input
                                id="askEndPrice"
                                type="text"
                                className="text-right"
                                value={formatCurrency(orderBookSettings?.askOrderBookEndPrice, "LDK")}
                                onChange={(e) => setOrderBookSettings(prev => prev ? {...prev, askOrderBookEndPrice: parseCurrency(e.target.value)} : null)}
                            />
                        </div>
                    </div>
                </div>

                {/* 매수 호가창 설정 */}
                <div>
                    <h3 className="text-sm font-semibold mb-3">매수 호가창</h3>
                    <div className="grid grid-cols-2 gap-2">
                        <div>
                            <Label htmlFor="bidStop">매수 여부</Label>
                            <Select
                                value={orderBookSettings?.bidStopYN ? "stop" : "active"}
                                onValueChange={(value) => setOrderBookSettings(prev => prev ? {...prev, bidStopYN: value === "stop"} : null)}
                            >
                                <SelectTrigger>
                                    <SelectValue placeholder="선택하세요"/>
                                </SelectTrigger>
                                <SelectContent>
                                    <SelectItem value="active">활성</SelectItem>
                                    <SelectItem value="stop">중지</SelectItem>
                                </SelectContent>
                            </Select>
                        </div>
                        <div>
                            <Label htmlFor="bidLimitCount">최대 개수</Label>
                            <Input
                                id="bidLimitCount"
                                type="number"
                                className="text-right"
                                value={orderBookSettings?.bidOrderBookLimitCount || 0}
                                onChange={(e) => setOrderBookSettings(prev => prev ? {...prev, bidOrderBookLimitCount: parseInt(e.target.value)} : null)}
                            />
                        </div>
                    </div>
                    <div className="grid grid-cols-2 gap-2 mt-2">
                        <div>
                            <Label htmlFor="bidStartPrice">호가 랜덤 시작 가격</Label>
                            <Input
                                id="bidStartPrice"
                                type="text"
                                className="text-right"
                                value={formatCurrency(orderBookSettings?.bidOrderBookStartPrice, "LDK")}
                                onChange={(e) => setOrderBookSettings(prev => prev ? {...prev, bidOrderBookStartPrice: parseCurrency(e.target.value)} : null)}
                            />
                        </div>
                        <div>
                            <Label htmlFor="bidEndPrice">호가 랜덤 종료 가격</Label>
                            <Input
                                id="bidEndPrice"
                                type="text"
                                className="text-right"
                                value={formatCurrency(orderBookSettings?.bidOrderBookEndPrice, "LDK")}
                                onChange={(e) => setOrderBookSettings(prev => prev ? {...prev, bidOrderBookEndPrice: parseCurrency(e.target.value)} : null)}
                            />
                        </div>
                    </div>
                </div>
            </CardContent>
        </Card>
    );
}