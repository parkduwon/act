package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

/**
 * 订单
 *
 * @author chen.li
 */
@Setter
@Getter
public class Order {
    //交易对
    private String symbol;
    //下单数量
    private String amount;
    //委托时间
    private String create_time;
    //委托价格
    private String price;
    //平均成交价
    private String avg_price;
    //类型
    private String type;
    //订单ID
    private String order_id;
    //成交数量
    private String deal_amount;
    //委托状态
    private String status;
    //用户发单请求时自定义字段
    private String customer_id;

    @Override
    public String toString() {
        return "Order{" +
                "symbol='" + symbol + '\'' +
                ", amount='" + amount + '\'' +
                ", create_time='" + create_time + '\'' +
                ", price='" + price + '\'' +
                ", avg_price='" + avg_price + '\'' +
                ", type='" + type + '\'' +
                ", order_id='" + order_id + '\'' +
                ", deal_amount='" + deal_amount + '\'' +
                ", status='" + status + '\'' +
                ", customer_id='" + customer_id + '\'' +
                '}';
    }
}
