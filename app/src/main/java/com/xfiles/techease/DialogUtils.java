package com.xfiles.techease;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import ak.sh.ay.musicwave.MusicWave;

import static android.content.Context.AUDIO_SERVICE;
import static android.media.ToneGenerator.MAX_VOLUME;

public class DialogUtils {

    public static Visualizer mVisualizer;
    public static MusicWave musicWave;


    public static MediaPlayer mediaPlayer;
    public static Handler durationHandler = new Handler();

    public static double timeElapsed = 0, finalTime = 0;
    public static int forwardTime = 2000, backwardTime = 2000;


    public static void MediaPlayerDialog(LayoutInflater inflater, final Context context, String string) {


//        float log1=(float)(Math.log(maxVolume-currVolume)/Math.log(maxVolume));
//        yourMediaPlayer.setVolume(1-log1);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final View dialogView = inflater.inflate(R.layout.mediaplayer_layout, null);
        dialogView.setBackgroundResource(android.R.color.transparent);
        dialogBuilder.setView(dialogView);
        final AlertDialog dialog = dialogBuilder.create();

        musicWave = dialogView.findViewById(R.id.musicWave);

        mediaPlayer = new MediaPlayer();

        String strPath = "/storage/emulated/0/XFiles/" + string;



        try {
            mediaPlayer.setDataSource(strPath);
            prepareVisualizer();
            mVisualizer.setEnabled(true);
            mediaPlayer.prepare();
            mediaPlayer.start();


            finalTime = mediaPlayer.getDuration();
            timeElapsed = mediaPlayer.getCurrentPosition();


            AudioManager am = (AudioManager) context.getSystemService(AUDIO_SERVICE);
            int volume_level = am.getStreamVolume(AudioManager.STREAM_MUSIC);

        } catch (IOException e) {
            e.printStackTrace();
        }


        TextView tvTitile = dialogView.findViewById(R.id.tv_title);
        Button btnStop = dialogView.findViewById(R.id.btn_stiop);

        tvTitile.setText(string);

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.stop();
                mVisualizer.setEnabled(false);
                dialog.dismiss();
            }
        });


        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        doKeepDialog(dialog);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mVisualizer.setEnabled(false);
                }
                return false;
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                dialog.dismiss();
                mVisualizer.setEnabled(false);
                mediaPlayer.reset();
                mediaPlayer.stop();

            }
        });

    }

    private static void prepareVisualizer() {

        mVisualizer = new Visualizer(mediaPlayer.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    public void onWaveFormDataCapture(Visualizer visualizer,
                                                      byte[] bytes, int samplingRate) {
                        musicWave.updateVisualizer(bytes);
                    }

                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] bytes, int samplingRate) {
                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, false);
        mVisualizer.setEnabled(true);
    }

    private static void doKeepDialog(Dialog dialog) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
    }


}
