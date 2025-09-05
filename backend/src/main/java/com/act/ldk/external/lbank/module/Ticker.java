package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

/**
 * @author chen.li
 */
@Setter
@Getter
public class Ticker {
    private String change;
    private String high;
    private String latest;
    private String low;
    private String turnover;
    private String vol;

    @Override
    public String toString() {
        return "Ticket [change=" + change + ", high=" + high + ", latest=" + latest + ", low=" + low + ", turnover="
                + turnover + ", vol=" + vol + "]";
    }

}
