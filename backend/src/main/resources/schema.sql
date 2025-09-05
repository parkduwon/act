-- Drop tables if exist
DROP TABLE IF EXISTS force_trade_settings;
DROP TABLE IF EXISTS order_book_settings;
DROP TABLE IF EXISTS trade_settings;
DROP TABLE IF EXISTS member_principal;

-- Create member_principal table
CREATE TABLE member_principal (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_date DATETIME NOT NULL,
    modified_date DATETIME NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create trade_settings table  
CREATE TABLE trade_settings (
    id BIGINT NOT NULL AUTO_INCREMENT,
    symbol VARCHAR(50) NOT NULL UNIQUE,
    main_coin VARCHAR(20) NOT NULL,
    quote_coin VARCHAR(20) NOT NULL,
    target_price DECIMAL(20, 8) NOT NULL,
    max_trade_price DECIMAL(20, 8) NOT NULL,
    min_trade_price DECIMAL(20, 8) NOT NULL,
    min_usdt_quantity DECIMAL(20, 8) NOT NULL,
    bound_dollar DECIMAL(20, 8) NOT NULL,
    random_try_percent INT NOT NULL,
    follow_coin_enabled BOOLEAN NOT NULL,
    follow_coin VARCHAR(50),
    follow_coin_rate DECIMAL(20, 8),
    follow_coin_rate_formula VARCHAR(255),
    bid_trade_switch BOOLEAN NOT NULL DEFAULT TRUE,
    bid_trade_schedule_rate INT NOT NULL DEFAULT 5,
    bid_trade_dollar DECIMAL(20, 8) NOT NULL DEFAULT 5,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_date DATETIME NOT NULL,
    modified_date DATETIME NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create order_book_settings table
CREATE TABLE order_book_settings (
    id BIGINT NOT NULL AUTO_INCREMENT,
    symbol VARCHAR(50) NOT NULL UNIQUE,
    ask_order_book_start_price DECIMAL(20, 8) NOT NULL,
    ask_order_book_end_price DECIMAL(20, 8) NOT NULL,
    ask_order_book_limit_count INT NOT NULL,
    ask_stopyn BOOLEAN NOT NULL,
    bid_order_book_start_price DECIMAL(20, 8) NOT NULL,
    bid_order_book_end_price DECIMAL(20, 8) NOT NULL,
    bid_order_book_limit_count INT NOT NULL,
    bid_stopyn BOOLEAN NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_date DATETIME NOT NULL,
    modified_date DATETIME NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create force_trade_settings table
CREATE TABLE force_trade_settings (
    id BIGINT NOT NULL AUTO_INCREMENT,
    symbol VARCHAR(50) NOT NULL UNIQUE,
    force_stop_enabled BOOLEAN NOT NULL,
    force_type VARCHAR(50) NOT NULL,
    force_trade_type VARCHAR(50) NOT NULL,
    created_date DATETIME NOT NULL,
    modified_date DATETIME NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create indexes
CREATE INDEX idx_trade_settings_symbol ON trade_settings(symbol);
CREATE INDEX idx_order_book_settings_symbol ON order_book_settings(symbol);
CREATE INDEX idx_force_trade_settings_symbol ON force_trade_settings(symbol);
CREATE INDEX idx_member_principal_username ON member_principal(username);