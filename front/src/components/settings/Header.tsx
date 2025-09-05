import { Button } from "@/components/ui/button";
import { LogOut, Power, PowerOff } from "lucide-react";
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

interface HeaderProps {
    botStatus: {
        enabled: boolean;
    };
    toggleBot: () => void;
    handleLogout: () => void;
}

export default function Header({ botStatus, toggleBot, handleLogout }: HeaderProps) {
    return (
        <div className="bg-white shadow-sm border-b sticky top-0 z-10">
            <div className="px-4 py-3 flex items-center justify-between">
                <h1 className="text-xl font-bold">LDK Trading Bot</h1>
                <div className="flex items-center gap-2">
                    <AlertDialog>
                        <AlertDialogTrigger asChild>
                            <Button
                                variant={botStatus.enabled ? "destructive" : "default"}
                                size="sm"
                                className="gap-2"
                            >
                                {botStatus.enabled ? (
                                    <>
                                        <PowerOff className="h-4 w-4"/>
                                        <span>봇 중지</span>
                                    </>
                                ) : (
                                    <>
                                        <Power className="h-4 w-4"/>
                                        <span>봇 시작</span>
                                    </>
                                )}
                            </Button>
                        </AlertDialogTrigger>
                        <AlertDialogContent className="max-w-xs rounded-lg">
                            <AlertDialogHeader>
                                <AlertDialogTitle className="text-base">
                                    {botStatus.enabled ? "봇 중지" : "봇 시작"}
                                </AlertDialogTitle>
                                <AlertDialogDescription className="text-sm">
                                    {botStatus.enabled 
                                        ? "거래 봇을 중지하시겠습니까? 진행 중인 거래가 취소될 수 있습니다." 
                                        : "거래 봇을 시작하시겠습니까? 설정된 조건에 따라 자동 거래가 실행됩니다."}
                                </AlertDialogDescription>
                            </AlertDialogHeader>
                            <AlertDialogFooter>
                                <AlertDialogCancel>취소</AlertDialogCancel>
                                <AlertDialogAction onClick={toggleBot}>확인</AlertDialogAction>
                            </AlertDialogFooter>
                        </AlertDialogContent>
                    </AlertDialog>
                    <Button
                        variant="ghost"
                        size="icon"
                        onClick={handleLogout}
                    >
                        <LogOut className="h-4 w-4"/>
                    </Button>
                </div>
            </div>
        </div>
    );
}