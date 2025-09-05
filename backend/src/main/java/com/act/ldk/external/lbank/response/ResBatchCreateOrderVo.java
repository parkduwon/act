package com.act.ldk.external.lbank.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @program: lbank_api_v2
 * @description: 数据返回结构体
 * @author: steel.cheng
 * @create: 2019-09-04 14:05
 **/
@Setter
@Getter
public class ResBatchCreateOrderVo implements Serializable {

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
    private List<ResCreateOrderVo> data;


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
