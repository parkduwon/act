package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class LatestPrice {
    private String symbol;
    private BigDecimal price;

    @Override
    public String toString() {
        return "LatestPrice{" +
                "symbol='" + symbol + '\'' +
                ", price=" + price +
                '}';
    }
}
