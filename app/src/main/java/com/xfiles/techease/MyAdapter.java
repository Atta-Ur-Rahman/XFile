package com.xfiles.techease;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechSettings;

import java.io.File;
import java.io.IOException;
import java.util.List;

import br.com.joinersa.oooalertdialog.Animation;
import br.com.joinersa.oooalertdialog.OnClickListener;
import br.com.joinersa.oooalertdialog.OoOAlertDialog;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


    private List<FileHelper> fileHelpers;
    private Context context;
    private LayoutInflater inflater;
    private String strPath;

    public MyAdapter(List<FileHelper> fileHelpers, Context context, LayoutInflater inflaters) {
        this.fileHelpers = fileHelpers;
        this.context = context;
        this.inflater = inflaters;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recording_file, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {

        final FileHelper fileHelper = fileHelpers.get(position);
        holder.tvRecordingFiles.setText(fileHelper.getStrRecordingFilePath());
        holder.tvRecordingDuration.setText(fileHelper.getStrRecordingDuration());
        holder.tvRecordingDateTime.setText(fileHelper.getStrRecordingTimeDate());

        holder.tvRecordingFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String string = fileHelper.getStrRecordingFilePath();
                DialogUtils.MediaPlayerDialog(inflater, context, string);

            }
        });

        holder.tvRecordingDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = fileHelper.getStrRecordingFilePath();
                DialogUtils.MediaPlayerDialog(inflater, context, string);


            }
        });

        holder.ivText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strPath = "/storage/emulated/0/XFiles/" + fileHelper.getStrRecordingFilePath();
                Utilities.putValueInEditor(context).putString("record_path", strPath).commit();
                Utilities.connectFragment(context, new TranscriberFragment());
            }
        });

        holder.btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TestSpeech().execute();
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strPath = "/storage/emulated/0/XFiles/" + fileHelper.getStrRecordingFilePath();


                new OoOAlertDialog.Builder((Activity) context)
                        .setTitle("XFile")
                        .setMessage("Do you want to delete Recording?")
                        .setAnimation(Animation.ZOOM)
                        .setPositiveButton("Delete", new OnClickListener() {
                            @Override
                            public void onClick() {

                                DeleteRecordingFile(strPath);

                            }
                        })
                        .setNegativeButton("Cancel", new OnClickListener() {
                            @Override
                            public void onClick() {

                            }
                        }).build();


            }
        });
    }

    @Override
    public int getItemCount() {
        return fileHelpers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvRecordingFiles, tvRecordingDuration, tvRecordingDateTime;
        LinearLayout linearLayout;
        ImageView ivDelete, ivText;
        Button btnConvert;

        public ViewHolder(View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.layout);

            tvRecordingFiles = itemView.findViewById(R.id.tv_recording_file);
            tvRecordingDuration = itemView.findViewById(R.id.tv_recording_duration);
            tvRecordingDateTime = itemView.findViewById(R.id.tv_recording_date_time);
            btnConvert = itemView.findViewById(R.id.btn_convert);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            ivText = itemView.findViewById(R.id.iv_text);

        }

    }

    private void DeleteRecordingFile(String strPath) {
        File file = new File(strPath);
        file.delete();
        if (file.exists()) {
            try {
                file.getCanonicalFile().delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (file.exists()) {
                context.getApplicationContext().deleteFile(file.getName());
            }

        }

        Utilities.connectFragmentWithOutBackStack(context, new RecordingViewFragment());
    }

    public class TestSpeech extends AsyncTask<Void, Void, RecognizeResponse> {
        RecognizeResponse response;

        @Override
        protected RecognizeResponse doInBackground(Void... voids) {

            try (SpeechClient speechClient = SpeechClient.create(SpeechSettings.newBuilder().setCredentialsProvider(null).build())) {
                RecognitionConfig.AudioEncoding encoding = RecognitionConfig.AudioEncoding.FLAC;
                int sampleRateHertz = 44100;
                String languageCode = "en-US";
                RecognitionConfig config = RecognitionConfig.newBuilder()
                        .setEncoding(encoding)
                        .setSampleRateHertz(sampleRateHertz)
                        .setLanguageCode(languageCode)
                        .build();
                String uri = Uri.parse("android.resource:///com.xfiles.techease/" + R.raw.audio).toString();
                RecognitionAudio audio = RecognitionAudio.newBuilder()
                        .setUri(uri)
                        .build();
                response = speechClient.recognize(config, audio);
                Log.d("ads", "Asd");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(RecognizeResponse recognizeResponse) {
            super.onPostExecute(recognizeResponse);
            if (recognizeResponse != null) {
                Log.d("asd", "asd");
            } else {
                Log.d("asd", "asd");
            }
        }
    }

}