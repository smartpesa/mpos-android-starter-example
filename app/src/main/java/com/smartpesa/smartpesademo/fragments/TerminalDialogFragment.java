package com.smartpesa.smartpesademo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import smartpesa.sdk.ServiceManager;
import smartpesa.sdk.devices.SpTerminal;

public class TerminalDialogFragment extends BluetoothDialogFragment<SpTerminal> {

    ServiceManager mServiceManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mServiceManager = ServiceManager.get(getActivity());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mServiceManager.stopScan();
                getActivity().finish();
            }
        });
    }
}
