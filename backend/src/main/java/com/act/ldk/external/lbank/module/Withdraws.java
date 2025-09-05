package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Setter
@Getter
public class Withdraws {
    private BigDecimal amount;
    private String coid;
    private String address;
    private String withdrawOrderId;
    private BigDecimal fee;
    private String networkName;
    private String transferType;
    private String txId;
    private String feeAssetCode;
    private Long id;
    private Long applyTime;
    private String status;

    @Override
    public String toString() {
        return "Withdraws{" +
                "amount=" + amount +
                ", coid='" + coid + '\'' +
                ", address='" + address + '\'' +
                ", withdrawOrderId='" + withdrawOrderId + '\'' +
                ", fee=" + fee +
                ", networkName='" + networkName + '\'' +
                ", transferType='" + transferType + '\'' +
                ", txId='" + txId + '\'' +
                ", feeAssetCode='" + feeAssetCode + '\'' +
                ", id=" + id +
                ", applyTime=" + applyTime +
                ", status='" + status + '\'' +
                '}';
    }
}
