package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserInfoAccount {
    private String uid;
    private Boolean canTrade;
    private Boolean canWithdraw;
    private Boolean canDeposit;
    private List<CustomerAssetInfo> balances;

    @Override
    public String toString() {
        return "UserInfoAccount{" +
                "uid='" + uid + '\'' +
                ", canTrade=" + canTrade +
                ", canWithdraw=" + canWithdraw +
                ", canDeposit=" + canDeposit +
                ", balances=" + balances +
                '}';
    }
}
