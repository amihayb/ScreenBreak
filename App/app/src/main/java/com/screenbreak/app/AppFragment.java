package com.screenbreak.app;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.screenbreak.R;
import com.screenbreak.common.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import timber.log.Timber;


public class AppFragment extends BaseFragment {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AppFragment.
     */
    public static AppFragment newInstance() {
        return new AppFragment();
    }

    public AppFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Timber.d("in onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Timber.d("in onCreateView");

        View fragmentView = inflater.inflate(R.layout.fragment_app, container, false);

        return fragmentView;
    }


}
