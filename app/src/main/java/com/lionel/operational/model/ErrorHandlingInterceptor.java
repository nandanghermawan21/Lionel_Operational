package com.lionel.operational.model;

import android.content.Context;

import com.lionel.operational.R;
import com.lionel.operational.util.SoundPlayer;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ErrorHandlingInterceptor implements Interceptor {

    private Context context; // Pastikan Anda memiliki akses ke Context

    public ErrorHandlingInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        try {
            response = chain.proceed(request);
        } catch (IOException e) {
            // Mainkan suara kesalahan di sini
            playErrorSound();
            throw e; // Lepaskan kembali IOException agar aplikasi dapat menangani kesalahan dengan benar
        }
        return response;
    }

    private void playErrorSound() {
        // Memainkan suara kesalahan, Anda dapat menggunakan implementasi SoundPlayer seperti yang dijelaskan sebelumnya
        SoundPlayer soundPlayer = new SoundPlayer();
        soundPlayer.playSound(context, R.raw.api_error_2);
    }
}