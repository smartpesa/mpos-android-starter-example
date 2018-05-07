
package com.smartpesa.smartpesademo.fragments;

import com.smartpesa.smartpesademo.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import smartpesa.sdk.devices.SpDevice;

public class BluetoothDialogFragment<T extends SpDevice> extends BaseDialogFragment {

    @Bind(R.id.cancel_btn) Button cancelBtn;
    @Bind(R.id.bluetooth_device_hint) TextView btSelectionHint;
    @Bind(R.id.list) ListView list;
    protected DeviceSelectedListener<T> mListener;
    protected BluetoothAdapter<T> adapter;
    protected List<T> data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_bluetooth_list, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);
        d.setTitle(R.string.select_bluetooth_device);
        d.setCancelable(false);
        return d;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        btSelectionHint.setText(getHintResId());

        adapter = new BluetoothAdapter<>(getActivity(), data);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onSelected(data.get(position));
                dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @StringRes protected int getHintResId() {
        return R.string.terminal_selection_hint;
    }

    public void updateDevices(Collection<T> devices) {
        data.clear();
        data.addAll(devices);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void setSelectedListener(DeviceSelectedListener<T> listener) {
        mListener = listener;
    }

    public interface DeviceSelectedListener<T> {
        void onSelected(T device);
    }

    protected static class BluetoothAdapter<T extends SpDevice> extends ArrayAdapter<T> {

        public BluetoothAdapter(Context context, List<T> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            ((TextView) view).setText(getItem(position).getName());
            return view;
        }
    }
}
