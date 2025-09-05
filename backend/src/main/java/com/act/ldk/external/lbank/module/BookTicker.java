package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookTicker {
    private String symbol;
    private String askPrice;
    private String askQty;
    private String bidQty;
    private String bidPrice;

    @Override
    public String toString() {
        return "BookTicker{" +
                "symbol='" + symbol + '\'' +
                ", askPrice='" + askPrice + '\'' +
                ", askQty='" + askQty + '\'' +
                ", bidQty='" + bidQty + '\'' +
                ", bidPrice='" + bidPrice + '\'' +
                '}';
    }
}