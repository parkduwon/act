package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 订单
 *
 * @author chen.li
 */
@Setter
@Getter
public class OrderPage {

    private Integer current_page;

    private Integer page_length;

    private Integer total;

    private List<Order> orders;

    @Override
    public String toString() {
        return "OrderPage{" +
                "current_page=" + current_page +
                ", page_length=" + page_length +
                ", total=" + total +
                ", orders=" + orders +
                '}';
    }
}
