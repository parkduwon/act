package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

/**
 * @author chen.li
 */
@Getter
@Setter
public class TradesNew {
    private String quoteQty;
    private String price;
    private String qty;
    private String id;
    private String time;
    private String isBuyerMaker;

    @Override
    public String toString() {
        return "TradesNew{" +
                "quoteQty='" + quoteQty + '\'' +
                ", price='" + price + '\'' +
                ", qty='" + qty + '\'' +
                ", id='" + id + '\'' +
                ", time='" + time + '\'' +
                ", isBuyerMaker='" + isBuyerMaker + '\'' +
                '}';
    }
}
