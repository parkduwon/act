
package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
public class NetworkList {

    private boolean isDefault;
    @Getter
    private String withdrawFeeRate;
    @Getter
    private String name;
    @Getter
    private BigDecimal withdrawMin;
    @Getter
    private BigDecimal minLimit;
    @Getter
    private BigDecimal minDeposit;
    @Getter
    private String feeAssetCode;
    @Getter
    private String withdrawFee;
    @Getter
    private int type;
    @Getter
    private String coin;
    @Getter
    private String network;

    @Override
    public String toString() {
        return "NetworkList{" +
                "isDefault=" + isDefault +
                ", withdrawFeeRate='" + withdrawFeeRate + '\'' +
                ", name='" + name + '\'' +
                ", withdrawMin=" + withdrawMin +
                ", minLimit=" + minLimit +
                ", minDeposit=" + minDeposit +
                ", feeAssetCode='" + feeAssetCode + '\'' +
                ", withdrawFee='" + withdrawFee + '\'' +
                ", type=" + type +
                ", coin='" + coin + '\'' +
                ", network='" + network + '\'' +
                '}';
    }
}