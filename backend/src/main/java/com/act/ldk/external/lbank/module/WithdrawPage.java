package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 订单
 *
 * @author steel.cheng
 */
@Setter
@Getter
public class WithdrawPage {

    private Integer current_page;

    private Integer page_length;

    private Integer total;

    private List<Withdraw> withdraws;

    @Override
    public String toString() {
        return "WithdrawPage{" +
                "current_page=" + current_page +
                ", page_length=" + page_length +
                ", total=" + total +
                ", withdraws=" + withdraws +
                '}';
    }
}
