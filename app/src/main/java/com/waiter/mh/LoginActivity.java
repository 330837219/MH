package com.waiter.mh;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.waiter.mh.model.ResponseInfo;
import com.waiter.mh.utils.Config;
import com.waiter.mh.utils.HttpUtil;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 登录
 */
public class LoginActivity extends AppCompatActivity {

    public static final String NEED_LOGIN_AGAIN = "login_again";
    private EditText mUserCode, mUserPass;
    private View mProgressView;
    private View mLoginFormView;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button mEmailSignInButton = (Button) findViewById(R.id.login_btn);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mUserCode = (EditText) findViewById(R.id.userCode);
        mUserPass = (EditText) findViewById(R.id.password);
        // 获取SharedPreferences对象
        mPreferences = getSharedPreferences(Config.ACCOUNT_PASSWORD, MODE_PRIVATE);
        mEditor = mPreferences.edit();
        //SharedPreferences里保存到账号密码赋值给控件
        mUserCode.setText(mPreferences.getString(Config.USER_CODE, null));
        mUserPass.setText(mPreferences.getString(Config.USER_PASS, null));
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Store values at the time of the login attempt.
        final String userCode = mUserCode.getText().toString();
        final String userPass = mUserPass.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(userPass)) {
            mUserPass.setError(getString(R.string.error_userpass));
            focusView = mUserPass;
            cancel = true;
        }

        if (TextUtils.isEmpty(userCode)) {
            mUserCode.setError(getString(R.string.error_usercode));
            focusView = mUserCode;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            HttpUtil.getInstance().login(userCode, userPass, new HttpUtil.SuccessCallback() {
                @Override
                public void onSuccess(String str) {
                    showProgress(false);
                    Type type = new TypeToken<ResponseInfo<String>>() {
                    }.getType();
                    ResponseInfo result = new Gson().fromJson(str, type);
                    if (result == null) {
                        Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
                    } else if (result != null && result.getStatus().equals(Config.STATUS_SUCCESS)) {
                        //存储账号和密码数据到SharedPreferences
                        if (!mPreferences.contains(Config.ACCOUNT_PASSWORD)) {
                            mEditor.putString(Config.USER_CODE, userCode);
                            mEditor.putString(Config.USER_PASS, userPass);
                            // 提交所有存入的数据
                            mEditor.commit();
                        }
                        //跳转到主界面
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.putExtra(NEED_LOGIN_AGAIN, false);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, result.getDescription(), Toast.LENGTH_SHORT).show();
                        //显示失败原因
                        mUserPass.requestFocus();
                    }
                }
            }, new HttpUtil.FailCallback() {
                @Override
                public void onFail(String failMsg) {
                    showProgress(false);
                    Toast.makeText(LoginActivity.this, "登录异常" + failMsg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

