package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class ResponseVo<T> implements Serializable {

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
     * 返回数据结构
     */
    private T data;


    @Override
    public String toString() {
        return "ResponseVo{" +
                "result=" + result +
                ", error_code='" + error_code + '\'' +
                ", ts=" + ts +
                ", data=" + data +
                '}';
    }
}
