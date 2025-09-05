package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiRestriction {
    private Boolean enableSpotTrading;
    private Long createTime;
    private Boolean enableReading;
    private Boolean ipRestrict;
    private Boolean enableWithdrawals;

    @Override
    public String toString() {
        return "ApiRestriction{" +
                "enableSpotTrading=" + enableSpotTrading +
                ", createTime=" + createTime +
                ", enableReading=" + enableReading +
                ", ipRestrict=" + ipRestrict +
                ", enableWithdrawals=" + enableWithdrawals +
                '}';
    }
}
