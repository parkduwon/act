package com.act.ldk.external.lbank.response;

import com.act.ldk.external.lbank.module.AssetDetail;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Setter
@Getter
public class ResAssetDetailVo implements Serializable {
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
    private Map<String, AssetDetail> data;


    @Override
    public String toString() {
        return "ResAssetDetailVo{" +
                "result=" + result +
                ", error_code='" + error_code + '\'' +
                ", code='" + code + '\'' +
                ", ts=" + ts +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
