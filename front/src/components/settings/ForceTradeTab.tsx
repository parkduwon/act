import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
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
import type { ForceTradeSettings } from "@/api/bot";
import React from "react";

interface ForceTradeTabProps {
    forceTradeSettings: ForceTradeSettings | null;
    setForceTradeSettings: React.Dispatch<React.SetStateAction<ForceTradeSettings | null>>;
    saveForceTradeSettings: () => Promise<void>;
    saving: boolean;
}

export default function ForceTradeTab({ forceTradeSettings, setForceTradeSettings, saveForceTradeSettings, saving }: ForceTradeTabProps) {
    return (
        <Card>
            <CardHeader className="pb-3 flex flex-row items-center justify-between">
                <CardTitle className="text-lg">강제 거래 설정</CardTitle>
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
                            <AlertDialogAction onClick={saveForceTradeSettings}>확인</AlertDialogAction>
                        </AlertDialogFooter>
                    </AlertDialogContent>
                </AlertDialog>
            </CardHeader>
            <CardContent className="space-y-4">
                <div className="grid grid-cols-2 gap-2">
                    <div>
                        <Label htmlFor="forceType">강제 유형</Label>
                        <Select
                            value={forceTradeSettings?.forceType || "NONE"}
                            onValueChange={(value) => setForceTradeSettings(prev => prev ? {...prev, forceType: value as 'NONE' | 'ALL' | 'BUY' | 'SELL'} : null)}
                        >
                            <SelectTrigger>
                                <SelectValue placeholder="선택하세요"/>
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="NONE">없음</SelectItem>
                                <SelectItem value="ALL">전체</SelectItem>
                                <SelectItem value="BUY">매수만</SelectItem>
                                <SelectItem value="SELL">매도만</SelectItem>
                            </SelectContent>
                        </Select>
                    </div>
                    <div>
                        <Label htmlFor="forceTradeType">강제 거래 방향</Label>
                        <Select
                            value={forceTradeSettings?.forceTradeType || "NONE"}
                            onValueChange={(value) => setForceTradeSettings(prev => prev ? {...prev, forceTradeType: value as 'NONE' | 'BUY' | 'SELL'} : null)}
                        >
                            <SelectTrigger>
                                <SelectValue placeholder="선택하세요"/>
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="NONE">없음</SelectItem>
                                <SelectItem value="BUY">매수</SelectItem>
                                <SelectItem value="SELL">매도</SelectItem>
                            </SelectContent>
                        </Select>
                    </div>
                </div>
            </CardContent>
        </Card>
    );
}