package com.smartpesa.smartpesademo.activities;

import com.smartpesa.smartpesademo.R;
import com.smartpesa.smartpesademo.fragments.BluetoothDialogFragment;
import com.smartpesa.smartpesademo.fragments.TerminalDialogFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import smartpesa.sdk.ServiceManager;
import smartpesa.sdk.SmartPesa;
import smartpesa.sdk.core.error.SpException;
import smartpesa.sdk.devices.SpTerminal;
import smartpesa.sdk.error.SpCardTransactionException;
import smartpesa.sdk.error.SpTransactionException;
import smartpesa.sdk.interfaces.TransactionCallback;
import smartpesa.sdk.interfaces.TransactionData;
import smartpesa.sdk.models.currency.Currency;
import smartpesa.sdk.models.loyalty.Loyalty;
import smartpesa.sdk.models.loyalty.LoyaltyTransaction;
import smartpesa.sdk.models.merchant.TransactionType;
import smartpesa.sdk.models.transaction.Balance;
import smartpesa.sdk.models.transaction.Card;
import smartpesa.sdk.models.transaction.Transaction;
import smartpesa.sdk.scanner.TerminalScanningCallback;

public class PaymentProgressActivity extends AppCompatActivity {

    public static final String KEY_AMOUNT = "amount";
    private static final String BLUETOOTH_FRAGMENT_TAG = "bluetooth";

    @Bind(R.id.amount_tv) TextView amountTv;
    @Bind(R.id.progress_tv) TextView progressTv;
    double amount;
    ServiceManager mServiceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_progress);
        ButterKnife.bind(this);

        //initialise service manager
        mServiceManager = ServiceManager.get(PaymentProgressActivity.this);

        amount = getIntent().getExtras().getDouble(KEY_AMOUNT);
        amountTv.setText("Amount: "+amount);
        progressTv.setText("Enabling blueetooth..");

        //scan for bluetooth device
        mServiceManager.scanTerminal(new TerminalScanningCallback() {
            @Override
            public void onDeviceListRefresh(Collection<SpTerminal> collection) {
                displayBluetoothDevice(collection);
            }

            @Override
            public void onScanStopped() {

            }

            @Override
            public void onScanTimeout() {

            }

            @Override
            public void onEnablingBluetooth(String s) {

            }

            @Override
            public void onBluetoothPermissionDenied(String[] strings) {

            }

        }, this);
    }

    private void performPayment(SpTerminal spTerminal) {

        SmartPesa.TerminalTransactionParam.Builder builder = SmartPesa.TerminalTransactionParam.newBuilder(spTerminal)
                .transactionType(TransactionType.InternalType.PAYMENT.getEnumId())
                .amount(new BigDecimal(Double.valueOf(amount)))
                .cardMode(SmartPesa.CardMode.SWIPE_OR_INSERT_OR_TAP)
                .from(SmartPesa.AccountType.DEFAULT)
                .to(SmartPesa.AccountType.DEFAULT)
                .cashBack(BigDecimal.ZERO);

        SmartPesa.TransactionParam param = builder.build();

        mServiceManager.performTransaction(param, new TransactionCallback() {
            @Override
            public void onProgressTextUpdate(String s) {
                progressTv.setText(s);
            }

            @Override
            public void onTransactionFinished(TransactionType transactionType, boolean isSuccess, @Nullable Transaction transaction, @Nullable SmartPesa.Verification verification, @Nullable SpCardTransactionException exception) {

            }

            @Override
            public void onTransactionApproved(TransactionData data) {
                progressTv.setText("Transaction approved");
            }

            @Override
            public void onTransactionDeclined(SpTransactionException exception, TransactionData data) {
                progressTv.setText(exception.getMessage());
            }

            @Override
            public void onError(SpException exception) {
                progressTv.setText(exception.getMessage());
            }

            @Override
            public void onDeviceConnected(SpTerminal spTerminal) {

            }

            @Override
            public void onDeviceDisconnected(SpTerminal spTerminal) {

            }

            @Override
            public void onBatteryStatus(SmartPesa.BatteryStatus batteryStatus) {

            }

            @Override
            public void onShowSelectApplicationPrompt(List<String> list) {

            }

            @Override
            public void onShowSelectTIDPrompt(List<String> tidList) {

            }

            @Override
            public void onWaitingForCard(String s, SmartPesa.CardMode cardMode) {
                progressTv.setText("Insert/swipe card");
            }

            @Override
            public void onShowInsertChipAlertPrompt() {
                progressTv.setText("Insert chip card");
            }

            @Override
            public void onReadCard(Card card) {

            }

            @Override
            public void onShowPinAlertPrompt(int tryCounter) {
                progressTv.setText("Enter PIN on pesaPOD");
            }

            @Override
            public void onPinEntered(int tryCounter) {

            }

            @Override
            public void onShowInputPrompt() {

            }

            @Override
            public void onReturnInputStatus(SmartPesa.InputStatus inputStatus, String s) {

            }

            @Override
            public void onShowConfirmAmountPrompt() {
                progressTv.setText("Confirm amount on pesaPOD");
            }

            @Override
            public void onAmountConfirmed(boolean b) {

            }


            @Override
            public void onStartPostProcessing(String providerName, Transaction transaction) {

            }

            @Override
            public void onReturnLoyaltyBalance(Loyalty loyalty) {

            }

            @Override
            public void onShowLoyaltyRedeemablePrompt(LoyaltyTransaction loyaltyTransaction) {

            }

            @Override
            public void onLoyaltyCancelled() {

            }

            @Override
            public void onLoyaltyApplied(LoyaltyTransaction loyaltyTransaction) {

            }

            @Override
            public void onShowConfirmFeePrompt(TransactionType.FeeChargeType feeChargeType, Currency currency, BigDecimal feeAmount, BigDecimal finalAmount) {

            }

            @Override
            public void onRequestForInput() {

            }

            @Override
            public void onShowBalance(Balance balance) {

            }

            @Override
            public void onShowPinPass(String pin) {

            }
        }, this);
    }

    //display the list of bluetooth devices
    public void displayBluetoothDevice(Collection<SpTerminal> devices) {
        TerminalDialogFragment dialog;
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(BLUETOOTH_FRAGMENT_TAG);
        if (fragment == null) {
            dialog = new TerminalDialogFragment();
            dialog.show(getSupportFragmentManager(), BLUETOOTH_FRAGMENT_TAG);
        } else {
            dialog = (TerminalDialogFragment) fragment;
        }
        dialog.setSelectedListener(new DeviceSelectedListenerImpl());
        dialog.updateDevices(devices);
    }

    //start the transaction when the bluetooth device is selected
    private class DeviceSelectedListenerImpl implements BluetoothDialogFragment.DeviceSelectedListener<SpTerminal> {
        @Override
        public void onSelected(SpTerminal device) {
            performPayment(device);
        }
    }

}
