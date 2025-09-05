package com.act.ldk;

import com.act.ldk.external.lbank.response.*;
import com.act.ldk.external.lbank.service.LBankApiService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(properties = {
    "auth.lbank.apikey=2e63924c-0bbe-4035-a918-24719040ed35",
    "auth.lbank.secret=44EF395A8D7DC01A07F130AF4E5D8797"
})
public class LbankApiTest {

    @Autowired
    private LBankApiService lBankApiService;
    
    private static final String TEST_SYMBOL = "ldk_usdt";
    
    @Test
    public void testLatestPrice() {
        try {
            BigDecimal price = lBankApiService.getLatestPrice(TEST_SYMBOL);
            System.out.println("Latest price for " + TEST_SYMBOL + ": " + price);
            assertNotNull(price, "Price should not be null");
        } catch (Exception e) {
            System.err.println("Error getting latest price: " + e.getMessage());
        }
    }
    
    @Test
    public void testOrderBook() {
        try {
            ResDepthVo orderBook = lBankApiService.getOrderBook(TEST_SYMBOL, 10);
            System.out.println("Order book retrieved");
            assertNotNull(orderBook, "Order book should not be null");
            
            if (orderBook.getData() != null) {
                System.out.println("Asks: " + orderBook.getData().getAsks().size());
                System.out.println("Bids: " + orderBook.getData().getBids().size());
            }
        } catch (Exception e) {
            System.err.println("Error getting order book: " + e.getMessage());
        }
    }
    
    @Test
    public void testUserInfoAccount() {
        try {
            ResUserInfoAccountVo userInfoAccount = lBankApiService.getUserInfoAccount();
            System.out.println("User info account retrieved");
            assertNotNull(userInfoAccount, "User info account should not be null");
            
            if (userInfoAccount.getData() != null) {
                System.out.println("UID: " + userInfoAccount.getData().getUid());
                System.out.println("Can Trade: " + userInfoAccount.getData().getCanTrade());
                System.out.println("Can Withdraw: " + userInfoAccount.getData().getCanWithdraw());
                System.out.println("Can Deposit: " + userInfoAccount.getData().getCanDeposit());
                
                if (userInfoAccount.getData().getBalances() != null) {
                    userInfoAccount.getData().getBalances().stream()
                        .filter(balance -> "ldk".equalsIgnoreCase(balance.getAsset()) || 
                                         "usdt".equalsIgnoreCase(balance.getAsset()))
                        .forEach(balance -> System.out.println("Balance - " + balance.getAsset().toUpperCase() +
                                         ": free=" + balance.getFree() +
                                         ", locked=" + balance.getLocked()));
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting user info account: " + e.getMessage());
        }
    }
    
    @Test
    @Disabled
    public void testPlaceOrder() {
        try {
            // Place a test sell order with high price to avoid execution
            ResCreateOrderVo order = lBankApiService.placeOrder(
                "ldk_usdt",
                "sell", 
                "0.014000", 
                "1"
            );
            System.out.println("Order placed: " + order);
            assertNotNull(order, "Order response should not be null");
            
            if (order.getData() != null) {
                System.out.println("Order data: " + order.getData());
            }
        } catch (Exception e) {
            System.err.println("Error placing order: " + e.getMessage());
        }
    }
    
    @Test
    public void testQueryOrder() {
        try {
            String testOrderId = "70c2c994-99c4-4944-9b98-aba2e4c5fd69";
            ResOrderVo order = lBankApiService.queryOrder(TEST_SYMBOL, testOrderId);
            System.out.println("Order query result: " + order);
            
            if (order != null && order.getData() != null) {
                System.out.println("Order data: " + order.getData());
            }
        } catch (Exception e) {
            System.err.println("Error querying order: " + e.getMessage());
        }
    }
    
    @Test
    public void testCancelOrder() {
        try {
            String testOrderId = "70c2c994-99c4-4944-9b98-aba2e4c5fd69";
            ResCancelOrderVo result = lBankApiService.cancelOrder(TEST_SYMBOL, testOrderId);
            System.out.println("Cancel order result: " + result);
            
            if (result != null) {
                System.out.println("Cancel success: " + result.getResult());
            }
        } catch (Exception e) {
            System.err.println("Error canceling order: " + e.getMessage());
        }
    }
    
    @Test
    public void testOpenOrders() {
        try {
            OpenOrdersResponse openOrders = lBankApiService.getOpenOrders(TEST_SYMBOL, "1", "20");
            System.out.println("Open orders retrieved");
            assertNotNull(openOrders, "Open orders should not be null");
            
            if (openOrders.getData() != null) {
                System.out.println("Open orders data: " + openOrders.getData());
            }
        } catch (Exception e) {
            System.err.println("Error getting open orders: " + e.getMessage());
        }
    }
    
    @Test
    public void testTransactionHistory() {
        try {
            ResTransactionHistoryVo history = lBankApiService.getTransactionHistory(
                TEST_SYMBOL, 
                "2024-07-05 21:00:00", 
                null
            );
            System.out.println("Transaction history retrieved");
            
            if (history != null && history.getData() != null) {
                System.out.println("Transaction history data: " + history.getData());
            }
        } catch (Exception e) {
            System.err.println("Error getting transaction history: " + e.getMessage());
        }
    }
}