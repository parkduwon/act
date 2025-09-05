package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class Transaction {
    private String symbol;
    private String id;
    private String orderId;
    private String price;
    private String qty;
    private String quoteQty;
    private String commission;
    private Long time;
    private Boolean isBuyer;
    private Boolean isMaker;


    @Override
    public String toString() {
        return "Transaction{" +
                "symbol='" + symbol + '\'' +
                ", id='" + id + '\'' +
                ", orderId='" + orderId + '\'' +
                ", price='" + price + '\'' +
                ", qty='" + qty + '\'' +
                ", quoteQty='" + quoteQty + '\'' +
                ", commission='" + commission + '\'' +
                ", time=" + time +
                ", isBuyer=" + isBuyer +
                ", isMaker=" + isMaker +
                '}';
    }
}
