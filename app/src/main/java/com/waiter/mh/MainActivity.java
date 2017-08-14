package com.waiter.mh;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.waiter.mh.model.AppVersionInfo;
import com.waiter.mh.model.ResponseInfo;
import com.waiter.mh.utils.Config;
import com.waiter.mh.utils.HttpUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar mToolbar;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //从Android 7.0开始，一个应用提供自身文件给其它应用使用时，如果给出一个file://格式的URI的话，应用会抛出FileUriExposedException。
        //置入一个不设防的VmPolicy,避免在自动更新安装的时候报错
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        //悬浮按钮
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
        getAppVersion();//检查版本更新
        autoLogin();//自动登录
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
        if (id == R.id.action_logout) {
            mEditor.clear(); //清除保存在SharedPreferences里的账号密码信息,并跳转到登陆界面
            mEditor.commit();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
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
                HttpUtil.getInstance().login(userCode, userPass, new HttpUtil.SuccessCallback() {
                    @Override
                    public void onSuccess(String result) {
                        Type type = new TypeToken<ResponseInfo<String>>() {
                        }.getType();
                        ResponseInfo<String> response = new Gson().fromJson(result, type);//Json转成对象
                        //登录失败，跳转到登录界面
                        if (response == null || (!response.getStatus().equals(Config.STATUS_SUCCESS))) {
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            //登录成功
                        }
                    }
                }, new HttpUtil.FailCallback() {
                    @Override
                    public void onFail(String failMsg) {
                        Toast.makeText(MainActivity.this, "登录异常" + failMsg, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                });
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        } else {
            //登录界面登录成功了
        }
    }

    private void getAppVersion() {
        HttpUtil.getInstance().getAppVersion(getPackageName(), new HttpUtil.SuccessCallback() {
            @Override
            public void onSuccess(String result) {

                Type type = new TypeToken<ResponseInfo<AppVersionInfo>>() {
                }.getType();
                ResponseInfo<AppVersionInfo> response = new Gson().fromJson(result, type);//Json转成对象
                //存在新版本发布
                if (response != null && response.getStatus().equals(Config.STATUS_SUCCESS)
                        && response.getDatas() != null && response.getDatas().get(0) != null
                        && response.getDatas().get(0).getVERSION_CODE() > getVersionCode()) {
                    showUpdateDialog(response.getDatas().get(0));
                }
            }
        }, new HttpUtil.FailCallback() {
            @Override
            public void onFail(String failMsg) {
//                Toast.makeText(MainActivity.this, "获取新版本异常" + failMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取app本地版本号
     *
     * @return
     */
    private int getVersionCode() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    //显示进度条
    private void showUpdateDialog(final AppVersionInfo appVersion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("请升级APP至版本" + appVersion.getVERSION_NAME());
        builder.setMessage(appVersion.getVERSION_DESC());
        builder.setCancelable(false);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    downFile(appVersion.getDOWNLOAD_URL());//下载APP
                } else {
                    Toast.makeText(MainActivity.this, "SD卡不可用，请插入SD卡", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }

        });
        builder.create().show();
    }

    //下载文件
    private void downFile(String url) {
        final ProgressDialog mBar = new ProgressDialog(MainActivity.this);    //进度条，在下载的时候实时更新进度，提高用户友好度
        mBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mBar.setTitle("正在下载");
        mBar.setMessage("请稍候...");
        mBar.setProgress(0);
        mBar.show();

        final Request request = new Request.Builder().url(url).build();
        final Call call = new OkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mBar.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                FileOutputStream fos = null;
                try {
                    int length = (int) response.body().contentLength();
                    mBar.setMax(length);//设置进度条的总长度
                    int process = 0;
                    int len = 0;
                    is = response.body().byteStream();
                    File file = new File(Environment.getExternalStorageDirectory(), "mh.apk");
                    fos = new FileOutputStream(file);
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        process += len;
                        mBar.setProgress(process);       //这里就是关键的实时更新进度了！
                    }
                    fos.flush();
                    mBar.cancel();
                    installApp();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        fos.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                }
            }
        });
    }

    //安装文件，一般固定写法
    private void installApp() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "mh.apk")),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }


    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {

                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }
}
