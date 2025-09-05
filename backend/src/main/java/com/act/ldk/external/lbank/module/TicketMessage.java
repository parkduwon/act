package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

/**
 * @author chen.li
 */
@Setter
@Getter
public class TicketMessage {
    private String symbol;
    private String timestamp;
    private Ticker ticker;

    @Override
    public String toString() {
        return "TicketMessage [symbol=" + symbol + ", timestamp=" + timestamp + ", ticker=" + ticker + "]";
    }

}
