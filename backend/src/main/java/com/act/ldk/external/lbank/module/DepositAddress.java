package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DepositAddress {
    private String address;
    private String memo;
    private String coin;

    @Override
    public String toString() {
        return "DepositAddress{" +
                "address='" + address + '\'' +
                ", memo='" + memo + '\'' +
                ", coin='" + coin + '\'' +
                '}';
    }
}
