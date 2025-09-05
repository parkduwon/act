package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class DepositPage {
    private Integer current_page;

    private Integer page_length;

    private Integer total;

    private List<Deposit> depositOrders;

    @Override
    public String toString() {
        return "DepositPage{" +
                "current_page=" + current_page +
                ", page_length=" + page_length +
                ", total=" + total +
                ", depositOrders=" + depositOrders +
                '}';
    }
}
