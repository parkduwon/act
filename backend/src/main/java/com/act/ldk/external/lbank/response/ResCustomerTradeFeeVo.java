package com.act.ldk.external.lbank.response;

import com.act.ldk.external.lbank.module.CustomerTradeFee;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class ResCustomerTradeFeeVo {
    /**
     * 返回结果，true/false
     */
    private Boolean result;

    /**
     * 返回错误码
     */
    private String error_code;

    private String code;

    /**
     * 返回时间戳
     */
    private Long ts;

    /**
     * 返回错误信息
     */
    private String msg;

    /**
     * 返回数据结构
     */
    private List<CustomerTradeFee> data;

    @Override
    public String toString() {
        return "ResCustomerTradeFeeVo{" +
                "result=" + result +
                ", error_code='" + error_code + '\'' +
                ", code='" + code + '\'' +
                ", ts=" + ts +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
