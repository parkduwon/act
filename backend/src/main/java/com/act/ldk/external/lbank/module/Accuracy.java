package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Accuracy {

    private String minTranQua;

    private String symbol;

    private String quantityAccuracy;

    private String priceAccuracy;

    @Override
    public String toString() {
        return "Accuracy{" +
                "minTranQua='" + minTranQua + '\'' +
                ", symbol='" + symbol + '\'' +
                ", quantityAccuracy='" + quantityAccuracy + '\'' +
                ", priceAccuracy='" + priceAccuracy + '\'' +
                '}';
    }
}
