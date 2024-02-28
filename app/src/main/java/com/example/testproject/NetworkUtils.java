package com.example.testproject;

import okhttp3.OkHttpClient;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.nio.charset.StandardCharsets;

public class NetworkUtils {
    private static String userAgent;

    public NetworkUtils(Context context) {
        userAgent = userAgentString(context);
    }

    public OkHttpClient getOkHttpClient(String apiKey) {
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> chain.proceed(
                        chain.request().newBuilder()
                                .removeHeader("User-Agent")
                                .header("MapIr-SDK", userAgent)
                                .header("x-api-key", apiKey)
                                .build()
                ))
                .authenticator((route, response) -> {
                    Log.e(
                            "MAPIR",
                            "Mapir APIKEY Not provided or expired, Please visit https://corp.map.ir/registration/ to get new APIKEY or extend yours"
                    );
                    return null;
                })
                .build();
    }

    private String userAgentString(Context context) {
        String appName = getApplicationName(context);
        String encodedAppName = StandardCharsets.US_ASCII.newEncoder().canEncode(appName) ? appName : new String(appName.getBytes(), StandardCharsets.US_ASCII);
        return String.format(
                "Android/%s(%s)(%s)-MapSdk/%s-%s(%s)/%s-(%s)",
                Build.VERSION.SDK_INT,
                Build.VERSION.RELEASE,
                Build.CPU_ABI,
                "6.0.0",  // Assuming "6.0.0" is the MAPIR_SDK_VERSION
                context.getPackageName(),
                encodedAppName,
                Build.BRAND,
                Build.MODEL
        );
    }

    private String getApplicationName(Context context) {
        return context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
    }
}

