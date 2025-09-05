package com.act.ldk.external.lbank.security;

import com.act.ldk.external.lbank.utils.SdkUtil;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

/**
 * @param apiKey 用户apikey
 * @param secret 用户私钥
 */
public record AuthenticationInterceptor(String apiKey, String secret, String method) implements Interceptor {

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Long times = System.currentTimeMillis();

        Request original = chain.request();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        //请求头Header增加三个固定参数，时间戳和签名方式和随机数（长度在30-40之间）
        Request.Builder newRequestBuilder = original.newBuilder().
                addHeader("timestamp", times + "").
                addHeader("echostr", uuid).
                addHeader("signature_method", method);

        System.out.println(times);
        //参数拦截器只在非get请求中添加
        String oriMethod = original.method();
        if (StringUtils.equals("GET", oriMethod)) {
            return chain.proceed(original);
        }
        //加密请求参数获取数字签名
        String payload = original.url().query();

        StringBuilder buffer;
        //v2版本新增参数 timestamp/signature_method
        if (StringUtils.isBlank(payload)) {
            buffer = new StringBuilder();
            buffer.append("timestamp=").append(times);
        } else {
            buffer = new StringBuilder(payload);
            buffer.append("&timestamp=").append(times);
        }
        buffer.append("&echostr=").append(uuid);
        buffer.append("&signature_method=").append(method);
        String sign = SdkUtil.getSignForHmacSha256(buffer.toString(), apiKey, secret);
        HttpUrl signedUrl = original.url().newBuilder().
                addQueryParameter("api_key", apiKey).
                addQueryParameter("sign", sign).build();
        newRequestBuilder.url(signedUrl);
        Request newRequest = newRequestBuilder.build();
        return chain.proceed(newRequest);

    }

}