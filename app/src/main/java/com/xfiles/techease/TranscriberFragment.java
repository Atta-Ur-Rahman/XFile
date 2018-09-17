package com.xfiles.techease;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class TranscriberFragment extends Fragment {


    public TranscriberFragment() {
        // Required empty public constructor
    }

    View parentView;
    String strPath;
    TextView tvTranscriber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_transcriber, container, false);
        strPath = Utilities.getSharedPreferences(getActivity()).getString("record_path","");
        tvTranscriber = parentView.findViewById(R.id.tv_transcriber);



        
        return parentView;
    }

}
