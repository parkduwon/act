package com.act.ldk.external.lbank.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OpenOrdersResponse {
    @JsonProperty("result")
    private String result;
    
    @JsonProperty("error_code")
    private Integer errorCode;
    
    @JsonProperty("ts")
    private Long timestamp;
    
    @JsonProperty("data")
    private OrdersData data;
    
    @Data
    public static class OrdersData {
        @JsonProperty("orders")
        private List<Order> orders;
        
        @JsonProperty("page_length")
        private Integer pageLength;
        
        @JsonProperty("current_page")
        private Integer currentPage;
        
        @JsonProperty("total")
        private Integer total;
    }
    
    @Data
    public static class Order {
        @JsonProperty("order_id")
        private String orderId;
        
        @JsonProperty("symbol")
        private String symbol;
        
        @JsonProperty("price")
        private BigDecimal price;
        
        @JsonProperty("amount")
        private BigDecimal amount;
        
        @JsonProperty("type")
        private String type;  // buy or sell
        
        @JsonProperty("status")
        private Integer status;
        
        @JsonProperty("create_time")
        private Long createTime;
        
        @JsonProperty("deal_amount")
        private BigDecimal dealAmount;
    }
    
    public List<Order> getOrders() {
        return data != null && data.getOrders() != null ? data.getOrders() : List.of();
    }
}