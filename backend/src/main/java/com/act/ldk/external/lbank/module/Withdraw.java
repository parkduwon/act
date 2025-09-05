package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

/**
 * 提币结果
 *
 * @author steel.cheng
 */
@Setter
@Getter
public class Withdraw {

    //提币数量
    private String amount;
    //币种编号
    private String assetCode;
    //提币地址
    private String address;
    //提币手续费
    private String fee;
    //提币记录编号
    private String id;
    //提币时间
    private String time;
    //提币hash
    private String txHash;
    //提币状态
    private String status;


    @Override
    public String toString() {
        return "Withdraw{" +
                "id='" + id + '\'' +
                ", time='" + time + '\'' +
                ", assetCode='" + assetCode + '\'' +
                ", address='" + address + '\'' +
                ", status='" + status + '\'' +
                ", amount='" + amount + '\'' +
                ", fee='" + fee + '\'' +
                ", txHash='" + txHash + '\'' +
                '}';
    }
}
