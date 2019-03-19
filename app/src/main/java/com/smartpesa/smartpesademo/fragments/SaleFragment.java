package com.smartpesa.smartpesademo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smartpesa.smartpesademo.R;
import com.smartpesa.smartpesademo.activities.PaymentProgressActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SaleFragment extends Fragment {

    @Bind(R.id.sales_continue_btn) Button payBtn;
    @Bind(R.id.amount_et) EditText amountEt;

    public static SaleFragment newInstance(){
        return new SaleFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
