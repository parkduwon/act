package com.act.ldk.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
public class TradeUtils {

    /**
     * 팔로우 코인 비율 계산
     * 메인 코인 가격 / 팔로우 코인 가격
     * @param followPrice 팔로우 코인 가격
     * @param mainPrice 메인 코인 가격
     * @return 비율
     */
    public static BigDecimal calculateFollowRatio(BigDecimal followPrice, BigDecimal mainPrice) {
        if (followPrice == null || followPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        return mainPrice.divide(followPrice, 8, RoundingMode.HALF_UP);
    }
}