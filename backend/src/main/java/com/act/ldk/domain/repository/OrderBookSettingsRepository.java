package com.act.ldk.domain.repository;

import com.act.ldk.domain.entity.OrderBookSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OrderBookSettingsRepository extends JpaRepository<OrderBookSettings, Long> {
    Optional<OrderBookSettings> findBySymbol(String symbol);
    Optional<OrderBookSettings> findBySymbolAndEnabled(String symbol, Boolean enabled);
}