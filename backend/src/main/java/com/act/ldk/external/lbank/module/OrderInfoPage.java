package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrderInfoPage {
    private Integer current_page;

    private Integer page_length;

    private Integer total;

    private List<OrderInfo> orders;

    @Override
    public String toString() {
        return "OrderInfoPage{" +
                "current_page=" + current_page +
                ", page_length=" + page_length +
                ", total=" + total +
                ", orders=" + orders +
                '}';
    }
}
