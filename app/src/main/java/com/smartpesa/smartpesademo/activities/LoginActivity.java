package com.smartpesa.smartpesademo.activities;

import com.smartpesa.smartpesademo.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import smartpesa.sdk.ServiceManager;
import smartpesa.sdk.error.SpException;
import smartpesa.sdk.error.SpSessionException;
import smartpesa.sdk.models.merchant.VerifiedMerchantInfo;
import smartpesa.sdk.models.merchant.VerifyMerchantCallback;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.merchant_code_et) EditText mMerchantCodeEt;
    @Bind(R.id.operator_code_et) EditText mOperatorCodeEt;
    @Bind(R.id.operator_pin_et) EditText mOperatorPinEt;
    @Bind(R.id.login_btn) Button loginBtn;
    ServiceManager mServiceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //get the serverManager instance
        mServiceManager = ServiceManager.get(LoginActivity.this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String merchantCode = mMerchantCodeEt.getText().toString();
                String operatorCode = mOperatorCodeEt.getText().toString();
                String operatorPin = mOperatorPinEt.getText().toString();

                //check if all the fields are filled
                if (TextUtils.isEmpty(merchantCode) || TextUtils.isEmpty(operatorCode) || TextUtils.isEmpty(operatorPin)) {
                    showToast("Please fill in all fields");
                } else {
                    performVerifyMerchant(merchantCode, operatorCode, operatorPin);
                }
            }
        });
    }

    private void performVerifyMerchant(String merchantCode, String operatorCode, String operatorPin) {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Performing verifyMerchant, please wait..");
        progressDialog.setTitle("SmartPesa Login");
        progressDialog.setCancelable(false);
        progressDialog.show();

        mServiceManager.verifyMerchant(merchantCode, operatorCode, operatorPin, new VerifyMerchantCallback() {
            @Override
            public void onSuccess(VerifiedMerchantInfo verifiedMerchantInfo) {
                progressDialog.dismiss();
                showToast("Login Success");
                showMainActivity();
            }

            @Override
            public void onError(SpException exception) {
                if(exception instanceof SpSessionException) {
                    progressDialog.dismiss();
                    showToast("Session Expired, proceeding to getVersion i.e. SplashActivity");
                    showSplashActivity();
                } else {
                    progressDialog.dismiss();
                    showToast(exception.getMessage());
                }
            }
        });
    }

    private void showSplashActivity() {
        startActivity(new Intent(LoginActivity.this, SplashActivity.class));
        finish();
    }

    private void showMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void showToast(String message){
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
