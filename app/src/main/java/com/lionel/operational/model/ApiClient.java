package com.lionel.operational.model;
import static com.lionel.operational.model.Constant.BASE_URL;
import static com.lionel.operational.model.Constant.NOUNCE;
import static com.lionel.operational.model.Constant.SECRET;

import android.util.Log;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class ApiClient {
    private static Retrofit retrofit;

    public static Retrofit getInstant() {
            //ambil time saat ini
            long time = System.currentTimeMillis();
            //create X-Auth-Token header
            String token = createToken(time);

            Log.i("NOUNCE", String.valueOf(time));
            Log.i("TOKEN", token);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new HeaderInterceptor("X-Authorization", token))
                    .addInterceptor(new HeaderInterceptor("X-Nonce", String.valueOf(time)))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        return retrofit;
    }

    public static String createToken(long time) {
        //amil time saat ini
        String token = String.valueOf(time) + SECRET;

        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //get md5 string
            byte[] hash = md.digest(token.getBytes());
            //convert byte array to signum representation
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }
            token = sb.toString();

            //convert to base64
            token = Base64.getEncoder().encodeToString(token.getBytes());

            return token;
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error: Algorithm MD5 not available.");
            return null;
        }
    }
}
