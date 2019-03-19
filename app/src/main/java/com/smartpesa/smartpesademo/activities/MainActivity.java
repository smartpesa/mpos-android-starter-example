package com.smartpesa.smartpesademo.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.smartpesa.smartpesademo.R;
import com.smartpesa.smartpesademo.fragments.SaleFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import smartpesa.sdk.ServiceManager;
import smartpesa.sdk.core.error.SpException;
import smartpesa.sdk.models.operator.LogoutCallback;

public class MainActivity extends AppCompatActivity {

    private static final long MENU_LOGOUT = 2;
    private static final long MENU_SALE = 1;
    @Bind(R.id.toolbar) Toolbar mToolbar;

    Drawer drawer;
    ServiceManager mServiceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //get serviceManager instance
        mServiceManager = ServiceManager.get(MainActivity.this);

        //setup toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setUpDrawer();

        if (savedInstanceState == null) {
            // on first time display view for first nav item
           displayFragment(MENU_SALE);
        }
    }

    private void setUpDrawer() {
        //create the navigation drawer
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        displayFragment(drawerItem.getIdentifier());
                        return false;
                    }
                })
                .build();

        PrimaryDrawerItem Sale = new PrimaryDrawerItem().withName("Sale").withIdentifier(MENU_SALE);
        PrimaryDrawerItem logout = new PrimaryDrawerItem().withName("Logout").withIdentifier(MENU_LOGOUT);
        drawer.addItem(Sale);
        drawer.addItem(logout);
    }

    private void displayFragment(long menuId) {
        Fragment fragment = null;
        switch ((int) menuId){
            case (int) MENU_SALE:
                fragment = SaleFragment.newInstance();
                break;
            case (int) MENU_LOGOUT:
                logoutUser();
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container_body, fragment).commitAllowingStateLoss();
        }
    }

    private void logoutUser() {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Logging out user, please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        mServiceManager.logout(new LogoutCallback() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                progressDialog.dismiss();
                //on successful login take user to the splash screen
                finish();
                startActivity(new Intent(MainActivity.this, SplashActivity.class));
            }

            @Override
            public void onError(SpException exception) {

            }

        });
    }

}
