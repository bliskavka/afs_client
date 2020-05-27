package com.example.afs_client.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.afs_client.BluetoothManager;
import com.example.afs_client.Measurement;
import com.example.afs_client.R;

public class MonitorFragment extends Fragment {

    BluetoothManager manager;

    TextView speedText;
    TextView dynamicText;
    TextView staticText;
    TextView tempText;

    public static MonitorFragment newInstance() {
        return new MonitorFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            manager = BluetoothManager.getInstance(getActivity());
        } catch (Exception e) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monitor, container, false);
        speedText = view.findViewById(R.id.spd_tv);
        dynamicText = view.findViewById(R.id.dm_tv);
        staticText = view.findViewById(R.id.st_tv);
        tempText = view.findViewById(R.id.temp_tv);

        manager.getMeasuredData().observe(getViewLifecycleOwner(), data -> {
            speedText.setText(String.valueOf(data.getSpeed()));
            dynamicText.setText(String.valueOf(data.getDynamicPressure()));
            staticText.setText(String.valueOf(data.getStaticPressure()));
            tempText.setText(String.valueOf(data.getTemperature()));
        });
        return view;
    }
}
