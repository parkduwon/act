package com.act.ldk.external.lbank.client;

import com.act.ldk.external.lbank.constant.Constant;
import com.act.ldk.external.lbank.security.AuthenticationInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

/**
 * @author steel.cheng
 */
public class LBankJavaApiSdkServiceGenerator {

    static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    /**
     * 初始化
     */
    private static final Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    /**
     * 实例化
     */
    public static <S> S createService(Class<S> serviceClass, String apiKey, String secret, String signMethod) {
        if (!StringUtils.isEmpty(apiKey) && !StringUtils.isEmpty(secret) && !StringUtils.isEmpty(signMethod)) {
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(apiKey, secret, signMethod);
            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);
                //添加日志拦截器
                httpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC));
                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }
        return retrofit.create(serviceClass);
    }

    /**
     * 解析响应参数
     */
    public static <T> T executeSync(Call<T> call) throws Exception {
        try {
            Response<T> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                throw new Exception("error");
            }
        } catch (IOException e) {
            throw new Exception(e);
        }
    }
}