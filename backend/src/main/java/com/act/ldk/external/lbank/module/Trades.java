package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

/**
 * @author chen.li
 */
@Setter
@Getter
public class Trades {
    //交易时间
    private String date_ms;
    //交易数量
    private String amount;
    //交易价格
    private String price;
    //类型
    private String type;
    //交易ID
    private String tid;

    @Override
    public String toString() {
        return "Trades [date_ms=" + date_ms + ", amount=" + amount + ", price=" + price + ", type=" + type + ", tid="
                + tid + "]";
    }

}
