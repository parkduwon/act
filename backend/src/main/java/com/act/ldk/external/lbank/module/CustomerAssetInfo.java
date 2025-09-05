package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerAssetInfo {
    private String asset;
    private String free;
    private String locked;

    @Override
    public String toString() {
        return "CustomerAssetInfo{" +
                "asset='" + asset + '\'' +
                ", free='" + free + '\'' +
                ", locked='" + locked + '\'' +
                '}';
    }
}
