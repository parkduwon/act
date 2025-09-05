package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class Deposit {
    private String coin;
    private String status;
    private String txId;
    private String networkName;
    private String address;
    private BigDecimal amount;

    @Override
    public String toString() {
        return "Deposit{" +
                "coin='" + coin + '\'' +
                ", status='" + status + '\'' +
                ", txId='" + txId + '\'' +
                ", networkName='" + networkName + '\'' +
                ", address='" + address + '\'' +
                ", amount=" + amount +
                '}';
    }
}
