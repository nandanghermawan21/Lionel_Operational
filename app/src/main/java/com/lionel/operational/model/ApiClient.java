package com.lionel.operational.model;
import static com.lionel.operational.model.Constant.BASE_URL;
import static com.lionel.operational.model.Constant.NOUNCE;
import static com.lionel.operational.model.Constant.SECRET;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class ApiClient {
    private static Retrofit retrofit;

    public static Retrofit getInstant() {
        if (retrofit == null) {

            //create X-Auth-Token header
            String token = createToken();

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new HeaderInterceptor("X-Authorization", token))
                    .addInterceptor(new HeaderInterceptor("X-Nonce", NOUNCE))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static String createToken() {
        String combinedString = NOUNCE + SECRET;

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");

            byte[] hashBytes = digest.digest(combinedString.getBytes());

            String token = Base64.getEncoder().encodeToString(hashBytes);

            return token;
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error: Algorithm MD5 not available.");
            return null;
        }
    }
}
