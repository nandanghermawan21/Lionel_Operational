package com.lionel.operational.util;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundPlayer {
    private static MediaPlayer mediaPlayer;

    public void playSound(Context context, int soundResourceId) {
        stopSound(); // Stop any previously playing sound
        mediaPlayer = MediaPlayer.create(context, soundResourceId);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopSound(); // Release resources after sound is complete
            }
        });
        mediaPlayer.start();
    }

    public static void stopSound() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
