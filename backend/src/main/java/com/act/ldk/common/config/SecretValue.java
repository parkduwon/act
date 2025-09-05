package com.act.ldk.common.config;

import com.act.ldk.external.lbank.constant.LBankSecret;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SecretValue {
    @Value("${auth.lbank.apikey}")
    private String lBankApiKey;
    @Value("${auth.lbank.secret}")
    private String lBankSecret;

    @PostConstruct
    void init() {
        LBankSecret.API_KEY = lBankApiKey;
        LBankSecret.API_SECRET = lBankSecret;
    }
}
