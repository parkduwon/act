-- Initial test data for member_principal
-- Password for 'admin' user is: admin
INSERT INTO member_principal (username, password, enabled, created_date, modified_date) 
VALUES ('admin', '$2a$10$UKRUJaULeFeKWs3h6JMy.OhbXUxsYgB49O8AVvzhK5BWE7LLPKUKG', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE password = VALUES(password);

-- Insert trade settings if not exists
INSERT IGNORE INTO trade_settings (
    symbol, main_coin, quote_coin, target_price, max_trade_price, min_trade_price, 
    min_usdt_quantity, bound_dollar, random_try_percent, follow_coin_enabled, 
    enabled, created_date, modified_date
)
VALUES (
    'ldk_usdt', 'LDK', 'USDT', 0.01, 0.02, 0.005, 
    10, 100, 30, false, 
    true, NOW(), NOW()
);

-- Insert order book settings if not exists
INSERT IGNORE INTO order_book_settings (
    symbol, ask_order_book_start_price, ask_order_book_end_price, 
    ask_order_book_limit_count, ask_stopyn, bid_order_book_start_price, 
    bid_order_book_end_price, bid_order_book_limit_count, bid_stopyn, 
    enabled, created_date, modified_date
)
VALUES (
    'ldk_usdt', 0.011, 0.015, 
    10, false, 0.009, 
    0.0099, 10, false, 
    true, NOW(), NOW()
);

-- Insert force trade settings if not exists
INSERT IGNORE INTO force_trade_settings (
    symbol, force_stop_enabled, force_type, force_trade_type, 
    created_date, modified_date
)
VALUES (
    'ldk_usdt', false, 'NONE', 'NONE', 
    NOW(), NOW()
);