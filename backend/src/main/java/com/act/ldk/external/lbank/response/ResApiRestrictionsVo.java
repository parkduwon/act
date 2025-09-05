package com.act.ldk.external.lbank.response;

import com.act.ldk.external.lbank.module.ApiRestriction;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class ResApiRestrictionsVo implements Serializable {
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
    private ApiRestriction data;


    @Override
    public String toString() {
        return "ResApiRestrictionsVo{" +
                "result=" + result +
                ", error_code='" + error_code + '\'' +
                ", code='" + code + '\'' +
                ", ts=" + ts +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
