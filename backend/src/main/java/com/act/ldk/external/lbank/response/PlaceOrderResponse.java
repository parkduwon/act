package com.act.ldk.external.lbank.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PlaceOrderResponse {
    @JsonProperty("result")
    private String result;
    
    @JsonProperty("error_code")
    private Integer errorCode;
    
    @JsonProperty("ts")
    private Long timestamp;
    
    @JsonProperty("data")
    private OrderData data;
    
    @Data
    public static class OrderData {
        @JsonProperty("order_id")
        private String orderId;
    }
    
    public String getOrderId() {
        return data != null ? data.getOrderId() : null;
    }
}