package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class AssetDetail {
    private BigDecimal minWithdrawAmount;
    private Boolean stationDrawStatus;
    private Boolean depositStatus;
    private BigDecimal withdrawFee;
    private Boolean withdrawStatus;

    @Override
    public String toString() {
        return "AssetDetail{" +
                "minWithdrawAmount=" + minWithdrawAmount +
                ", stationDrawStatus=" + stationDrawStatus +
                ", depositStatus=" + depositStatus +
                ", withdrawFee=" + withdrawFee +
                ", withdrawStatus=" + withdrawStatus +
                '}';
    }
}
