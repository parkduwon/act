package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserAssetInfo {

    private String usableAmt;
    private String assetAmt;
    private List<NetworkList> networkList;
    private String freezeAmt;
    private String coin;

    @Override
    public String toString() {
        return "UserAssetInfo{" +
                "usableAmt='" + usableAmt + '\'' +
                ", assetAmt='" + assetAmt + '\'' +
                ", networkList=" + networkList +
                ", freezeAmt='" + freezeAmt + '\'' +
                ", coin='" + coin + '\'' +
                '}';
    }
}