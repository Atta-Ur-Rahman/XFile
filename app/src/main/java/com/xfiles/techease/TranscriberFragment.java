package com.xfiles.techease;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class TranscriberFragment extends Fragment {


    public TranscriberFragment() {
        // Required empty public constructor
    }

    View parentView;
    String strPath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_transcriber, container, false);
        strPath = Utilities.getSharedPreferences(getActivity()).getString("record_path","");
        




        return parentView;
    }

}
