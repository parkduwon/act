package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerTradeFee {
    private String symbol;
    private String makerCommission;
    private String takerCommission;

    @Override
    public String toString() {
        return "CustomerTradeFee{" +
                "symbol='" + symbol + '\'' +
                ", makerCommission='" + makerCommission + '\'' +
                ", takerCommission='" + takerCommission + '\'' +
                '}';
    }
}
