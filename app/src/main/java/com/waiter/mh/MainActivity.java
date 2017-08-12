package com.waiter.mh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.waiter.mh.model.ResponseInfo;
import com.waiter.mh.utils.Config;
import com.waiter.mh.utils.HttpUtil;

import java.lang.reflect.Type;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar mToolbar;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//                startActivity(new Intent(MainActivity.this, ScanActivity.class));
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mPreferences = getSharedPreferences(Config.ACCOUNT_PASSWORD, MODE_PRIVATE);
        mEditor = mPreferences.edit();
        autoLogin();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_box_prod_link) {
            //盒子商品关联
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new BoxProdLinkFragment()).commit();
            mToolbar.setTitle(R.string.nav_box_prod_link);
        } else if (id == R.id.nav_box_receive) {
            //送达网点
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new BoxReceiveFragment()).commit();
            mToolbar.setTitle(R.string.nav_box_receive);
        } else if (id == R.id.nav_box_recover) {
            //网点回收
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new BoxRecoverFragment()).commit();
            mToolbar.setTitle(R.string.nav_box_recover);
        } else if (id == R.id.nav_box_prod_clear) {
            //清空盒子商品
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new BoxProdClearFragment()).commit();
            mToolbar.setTitle(R.string.nav_box_prod_clear);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void autoLogin() {
        //如果是登陆界面启动的mainactivity,这里不需要在登陆了,
        if (getIntent().getBooleanExtra(LoginActivity.NEED_LOGIN_AGAIN, true)) {

            final String userCode = mPreferences.getString(Config.USER_CODE, null);
            final String userPass = mPreferences.getString(Config.USER_PASS, null);

            if (!TextUtils.isEmpty(userCode) && !TextUtils.isEmpty(userPass)) {
                HttpUtil.getInstance().Login(userCode, userPass, new HttpUtil.ResultCallback() {
                    @Override
                    public void onResult(String result) {
                        Type type = new TypeToken<ResponseInfo<String>>() {
                        }.getType();
                        ResponseInfo<String> response = new Gson().fromJson(result, type);//Json转成对象
                        //登录失败，跳转到登录界面
                        if (response == null || (!response.getStatus().equals(Config.STATUS_SUCCESS))) {
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                });
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        } else {
            //// TODO: 2017/8/8  
        }
    }
}
