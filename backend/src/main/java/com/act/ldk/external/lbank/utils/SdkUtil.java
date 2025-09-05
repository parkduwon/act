package com.act.ldk.external.lbank.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author steel.cheng
 */
public class SdkUtil {

    public static String getSignForHmacSha256(String str, String apiKey,String secretKey) {
        StringBuilder buffer = new StringBuilder();
        String result;
        if (StringUtils.isNoneBlank(str)) {
            str = str + "&api_key=" + apiKey;
            String[] split = StringUtils.split(str, "&");
            Arrays.sort(split);
            for (String s : split) {
                buffer.append(s).append("&");
            }
            result = buffer.toString();
            result = result.substring(0,result.length()-1);
        } else {
            result = buffer.append("api_key=").append(apiKey).toString();
        }
        //MD5后大写字母
        return  HMACSHA256Util.sha256_HMAC(DigestUtils.md5Hex(result).toUpperCase(),secretKey);
    }

    
}
