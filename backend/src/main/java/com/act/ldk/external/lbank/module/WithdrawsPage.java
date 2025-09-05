package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class WithdrawsPage {
    private Integer current_page;

    private Integer page_length;

    private Integer total;

    private List<Withdraws> withdraws;

    @Override
    public String toString() {
        return "WithdrawsPage{" +
                "current_page=" + current_page +
                ", page_length=" + page_length +
                ", total=" + total +
                ", withdraws=" + withdraws +
                '}';
    }
}
