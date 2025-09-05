package com.act.ldk.external.lbank.service;

import com.act.ldk.external.lbank.client.LBankJavaApiSdkServiceGenerator;
import com.act.ldk.external.lbank.constant.LBankSecret;
import com.act.ldk.external.lbank.response.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class LBankApiService {
    
    private LBankJavaApiService apiService;
    
    @PostConstruct
    public void init() {
        this.apiService = LBankJavaApiSdkServiceGenerator.createService(
            LBankJavaApiService.class, LBankSecret.API_KEY, LBankSecret.API_SECRET, "HmacSHA256"
        );
    }
    
    public BigDecimal getLatestPrice(String symbol) throws Exception {
        ResLatestPriceVo response = LBankJavaApiSdkServiceGenerator.executeSync(
            apiService.latestPrice(symbol)
        );
        if (response != null && response.getData() != null && !response.getData().isEmpty()) {
            return response.getData().getFirst().getPrice();
        }
        return null;
    }
    
    public ResDepthVo getOrderBook(String symbol, int limit) throws Exception {
        return LBankJavaApiSdkServiceGenerator.executeSync(
            apiService.getDepth(symbol, limit, null)
        );
    }
    
    public ResCreateOrderVo placeOrder(String symbol, String type, String price, String amount) throws Exception {
        return LBankJavaApiSdkServiceGenerator.executeSync(
            apiService.createOrder(symbol, type, price, amount, null)
        );
    }
    
    public ResCancelOrderVo cancelOrder(String symbol, String orderId) throws Exception {
        return LBankJavaApiSdkServiceGenerator.executeSync(
            apiService.cancelOrder(symbol, orderId)
        );
    }
    
    public ResOrderVo queryOrder(String symbol, String orderId) throws Exception {
        return LBankJavaApiSdkServiceGenerator.executeSync(
            apiService.getOrdersInfo(symbol, orderId)
        );
    }
    
    
    public ResTransactionHistoryVo getTransactionHistory(String symbol, String startTime, String endTime) throws Exception {
        return LBankJavaApiSdkServiceGenerator.executeSync(
            apiService.transactionHistory(symbol, startTime, endTime, null, "200")
        );
    }
    
    public ResUserInfoAccountVo getUserInfoAccount() throws Exception {
        return LBankJavaApiSdkServiceGenerator.executeSync(
            apiService.userInfoAccount()
        );
    }
    
    // Wrapper methods for compatibility
    public TickerResponse getTicker(String symbol) {
        try {
            BigDecimal price = getLatestPrice(symbol);
            TickerResponse response = new TickerResponse();
            TickerResponse.TickerData data = new TickerResponse.TickerData();
            data.setSymbol(symbol);
            data.setLatest(price);
            response.setData(data);
            response.setResult("true");
            return response;
        } catch (Exception e) {
            log.error("Failed to get ticker for {}", symbol, e);
            TickerResponse response = new TickerResponse();
            response.setResult("false");
            return response;
        }
    }
    
    
    /**
     * 특정 코인의 현재 가격 조회 (팔로우 코인용)
     * @param coinSymbol 코인 심볼 (예: "ltc_usdt", "trx_usdt")
     * @return 현재 가격, 조회 실패시 null
     */
    public BigDecimal getFollowCoinPrice(String coinSymbol) {
        try {
            BigDecimal price = getLatestPrice(coinSymbol);
            if (price != null) {
                log.debug("팔로우 코인 {} 가격 조회 성공: {}", coinSymbol, price);
            }
            return price;
        } catch (Exception e) {
            log.error("팔로우 코인 {} 가격 조회 실패", coinSymbol, e);
            return null;
        }
    }
    
    public OrderBookResponse getOrderBook(String symbol, int limit, String dummy) {
        try {
            // 실제 호가창 데이터 조회
            ResDepthVo depthVo = getOrderBook(symbol, limit);
            
            OrderBookResponse response = new OrderBookResponse();
            OrderBookResponse.OrderBookData data = new OrderBookResponse.OrderBookData();
            
            List<List<BigDecimal>> askList = new ArrayList<>();
            List<List<BigDecimal>> bidList = new ArrayList<>();
            
            if (depthVo != null && depthVo.getData() != null) {
                // asks: [[가격, 수량], ...]
                if (depthVo.getData().getAsks() != null) {
                    for (List<Double> ask : depthVo.getData().getAsks()) {
                        List<BigDecimal> askItem = new ArrayList<>();
                        askItem.add(BigDecimal.valueOf(ask.get(0))); // 가격
                        askItem.add(BigDecimal.valueOf(ask.get(1))); // 수량
                        askList.add(askItem);
                    }
                }
                
                // bids: [[가격, 수량], ...]  
                if (depthVo.getData().getBids() != null) {
                    for (List<Double> bid : depthVo.getData().getBids()) {
                        List<BigDecimal> bidItem = new ArrayList<>();
                        bidItem.add(BigDecimal.valueOf(bid.get(0))); // 가격
                        bidItem.add(BigDecimal.valueOf(bid.get(1))); // 수량
                        bidList.add(bidItem);
                    }
                }
            }
            
            data.setAsks(askList);
            data.setBids(bidList);
            response.setData(data);
            response.setResult("true");
            
            log.debug("호가창 조회 완료 - 매도호가: {}개, 매수호가: {}개", askList.size(), bidList.size());
            
            return response;
        } catch (Exception e) {
            log.error("Failed to get order book for {}", symbol, e);
            OrderBookResponse response = new OrderBookResponse();
            response.setResult("false");
            return response;
        }
    }
    
    public PlaceOrderResponse createOrder(String symbol, String type, String price, String quantity) {
        try {
            ResCreateOrderVo order = placeOrder(symbol, type, price, quantity);
            PlaceOrderResponse response = new PlaceOrderResponse();
            PlaceOrderResponse.OrderData data = new PlaceOrderResponse.OrderData();
            
            // 실제 주문 결과 사용
            if (order != null && order.getResult() && order.getData() != null) {
                String orderId = order.getData().get("order_id");
                data.setOrderId(orderId);
                response.setData(data);
                response.setResult("true");
                log.info("주문 성공: orderId={}", orderId);
            } else {
                response.setResult("false");
                response.setErrorCode(order != null ? Integer.parseInt(order.getError_code()) : -1);
                log.error("주문 실패: {}", order);
            }
            
            return response;
        } catch (Exception e) {
            log.error("Failed to create order", e);
            PlaceOrderResponse response = new PlaceOrderResponse();
            response.setResult("false");
            response.setErrorCode(-1);
            return response;
        }
    }
    
    /**
     * 모든 미체결 주문 조회
     */
    public OpenOrdersResponse getAllOpenOrders(String symbol) {
        List<OpenOrdersResponse.Order> allOrders = new ArrayList<>();
        int currentPage = 1;
        int pageSize = 200; // API 최대값
        int totalRecords = 0;
        
        try {
            while (true) {
                ResOrderHistoryVo orderHistory = LBankJavaApiSdkServiceGenerator.executeSync(
                    apiService.getOrdersInfoNoDeal(symbol, String.valueOf(currentPage), String.valueOf(pageSize))
                );
                
                if (orderHistory == null || !orderHistory.getResult() || orderHistory.getData() == null) {
                    break;
                }
                
                List<com.act.ldk.external.lbank.module.Order> orders = orderHistory.getData().getOrders();
                if (orders == null || orders.isEmpty()) {
                    break;
                }
                
                // 주문 변환
                for (var orderInfo : orders) {
                    OpenOrdersResponse.Order order = new OpenOrdersResponse.Order();
                    order.setOrderId(orderInfo.getOrder_id());
                    order.setSymbol(orderInfo.getSymbol());
                    order.setType(orderInfo.getType());
                    order.setPrice(new BigDecimal(orderInfo.getPrice()));
                    order.setAmount(new BigDecimal(orderInfo.getAmount()));
                    order.setDealAmount(new BigDecimal(orderInfo.getDeal_amount()));
                    try {
                        order.setCreateTime(Long.parseLong(orderInfo.getCreate_time()));
                    } catch (NumberFormatException e) {
                        order.setCreateTime(System.currentTimeMillis());
                    }
                    order.setStatus(Integer.parseInt(orderInfo.getStatus()));
                    allOrders.add(order);
                }
                
                totalRecords = orderHistory.getData().getTotal();
                log.info("미체결 주문 조회 - 전체: {}, 현재 페이지: {}, 누적: {}", 
                    totalRecords, currentPage, allOrders.size());
                
                // 모든 페이지를 가져왔는지 확인
                if (allOrders.size() >= totalRecords) {
                    break;
                }
                
                currentPage++;
            }
            
            // 응답 생성
            OpenOrdersResponse response = new OpenOrdersResponse();
            OpenOrdersResponse.OrdersData data = new OpenOrdersResponse.OrdersData();
            data.setOrders(allOrders);
            data.setTotal(totalRecords);
            data.setCurrentPage(1);
            data.setPageLength(allOrders.size());
            response.setData(data);
            response.setResult("true");
            
            log.info("전체 미체결 주문 조회 완료: {} 개", allOrders.size());
            return response;
            
        } catch (Exception e) {
            log.error("Failed to get all open orders", e);
            OpenOrdersResponse response = new OpenOrdersResponse();
            response.setResult("false");
            return response;
        }
    }
    
    /**
     * 페이지 단위 미체결 주문 조회
     */
    public OpenOrdersResponse getOpenOrders(String symbol, String page, String pageSize) {
        try {
            // 실제 API 호출 - executeSync 사용
            ResOrderHistoryVo orderHistory = LBankJavaApiSdkServiceGenerator.executeSync(
                apiService.getOrdersInfoNoDeal(symbol, page, pageSize)
            );
            
            OpenOrdersResponse response = new OpenOrdersResponse();
            OpenOrdersResponse.OrdersData data = new OpenOrdersResponse.OrdersData();
            
            if (orderHistory != null && orderHistory.getResult() && orderHistory.getData() != null) {
                List<OpenOrdersResponse.Order> orders = new ArrayList<>();
                
                // ResOrderHistoryVo의 주문 데이터를 OpenOrdersResponse.Order로 변환
                if (orderHistory.getData().getOrders() != null) {
                    for (var orderInfo : orderHistory.getData().getOrders()) {
                        OpenOrdersResponse.Order order = new OpenOrdersResponse.Order();
                        order.setOrderId(orderInfo.getOrder_id());
                        order.setSymbol(orderInfo.getSymbol());
                        order.setType(orderInfo.getType());
                        order.setPrice(new BigDecimal(orderInfo.getPrice()));
                        order.setAmount(new BigDecimal(orderInfo.getAmount()));
                        order.setDealAmount(new BigDecimal(orderInfo.getDeal_amount()));
                        // create_time은 String이므로 Long으로 변환 시도
                        try {
                            order.setCreateTime(Long.parseLong(orderInfo.getCreate_time()));
                        } catch (NumberFormatException e) {
                            order.setCreateTime(System.currentTimeMillis());
                        }
                        order.setStatus(Integer.parseInt(orderInfo.getStatus()));
                        orders.add(order);
                    }
                }
                
                data.setOrders(orders);
                data.setTotal(orderHistory.getData().getTotal());
                data.setCurrentPage(orderHistory.getData().getCurrent_page());
                data.setPageLength(orderHistory.getData().getPage_length());
                
                log.info("열린 주문 조회 성공: {} 개의 주문", orders.size());
            } else {
                data.setOrders(new ArrayList<>());
                data.setTotal(0);
                data.setCurrentPage(Integer.parseInt(page));
                data.setPageLength(Integer.parseInt(pageSize));
                log.warn("열린 주문 조회 실패 또는 주문 없음");
            }
            
            response.setData(data);
            response.setResult("true");
            return response;
        } catch (Exception e) {
            log.error("Failed to get open orders", e);
            OpenOrdersResponse response = new OpenOrdersResponse();
            response.setResult("false");
            return response;
        }
    }
}