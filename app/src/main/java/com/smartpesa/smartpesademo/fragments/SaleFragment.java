package com.smartpesa.smartpesademo.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smartpesa.smartpesademo.R;
import com.smartpesa.smartpesademo.activities.MainActivity;
import com.smartpesa.smartpesademo.activities.PaymentProgressActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import smartpesa.sdk.ServiceManager;
import smartpesa.sdk.core.error.SpException;
import smartpesa.sdk.models.operator.ActivateSoftPOSCallback;
import smartpesa.sdk.models.softpos.RegisterSoftPOS;
import smartpesa.sdk.models.softpos.RegisterSoftPOSCallback;

public class SaleFragment extends Fragment {

    @BindView(R.id.sales_continue_btn) Button payBtn;
    @BindView(R.id.amount_et) EditText amountEt;
    @BindView(R.id.registerBtn) Button registerBtn;
    @BindView(R.id.activateBtn) Button activateBtn;

    ServiceManager mServiceManager;

    public static SaleFragment newInstance(){
        return new SaleFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mServiceManager = ServiceManager.get(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_sales, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payAmount();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSoftPOS();
            }
        });
    }

    private void registerSoftPOS() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(R.string.app_name);
        progressDialog.setMessage(getString(R.string.registering_device));
        progressDialog.show();

//      *** register SoftPOS
        mServiceManager.get(getActivity()).registerSoftPOS(new RegisterSoftPOSCallback() {
            @Override
            public void onSuccess(RegisterSoftPOS registerSoftPOS) {
                if (null == getActivity()) return;

                progressDialog.dismiss();
                activateDevice();
            }

            @Override
            public void onError(SpException exception) {
                if (null == getActivity()) return;

                progressDialog.dismiss();
                Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void activateDevice() {
//      *** activate SoftPOS
        mServiceManager.get(getActivity()).activateSoftPOS("PASS-YOUR-OTP-HERE", new ActivateSoftPOSCallback() {
            @Override
            public void onSuccess(String s) {
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(SpException e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void payAmount() {
        String amount = amountEt.getText().toString();
        if(TextUtils.isEmpty(amount)){
            Toast.makeText(getActivity(), "Please enter amount", Toast.LENGTH_LONG).show();
        }else{
            proceedToPayment(Double.valueOf(amount));
        }
    }

    private void proceedToPayment(double amount) {
        Bundle bundle = new Bundle();
        bundle.putDouble(PaymentProgressActivity.KEY_AMOUNT, amount);
        Intent i = new Intent(getActivity(), PaymentProgressActivity.class);
        i.putExtras(bundle);
        startActivity(i);
    }

    @Override
    public void onResume() {
        super.onResume();
        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.title_sales));
        }
    }
}
