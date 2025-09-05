package com.act.ldk.domain.repository;

import com.act.ldk.domain.entity.ForceTradeSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ForceTradeSettingsRepository extends JpaRepository<ForceTradeSettings, Long> {
    Optional<ForceTradeSettings> findBySymbol(String symbol);
}