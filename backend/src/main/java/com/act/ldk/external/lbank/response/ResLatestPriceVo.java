package com.act.ldk.external.lbank.response;

import com.act.ldk.external.lbank.module.LatestPrice;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Setter
@Getter
public class ResLatestPriceVo implements Serializable {
    /**
     * 返回结果，true/false
     */
    private Boolean result;

    /**
     * 返回错误码
     */
    private String error_code;

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
    private List<LatestPrice> data;


    @Override
    public String toString() {
        return "ResponseVo{" +
                "result=" + result +
                ", error_code='" + error_code + '\'' +
                ", ts=" + ts +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
