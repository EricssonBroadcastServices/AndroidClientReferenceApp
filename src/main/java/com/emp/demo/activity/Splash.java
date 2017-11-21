package com.emp.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.ericsson.emovs.exposure.auth.EMPAuthProviderWithStorage;
import net.ericsson.emovs.exposure.interfaces.IAuthenticationListener;

import com.emp.demo.R;
import com.github.ybq.android.spinkit.style.Circle;

import net.ericsson.emovs.utilities.system.DeviceInfo;
import net.ericsson.emovs.utilities.errors.Error;

public class Splash extends Activity {

    public static final String TAG = Splash.class.getSimpleName();

    private ProgressBar mProgressBar;
    private boolean isPressBack;

    private Handler mHandler = new Handler();
    private TextView mTvCopyright;

    private TextView mTvVersion;
    private boolean isLoading;
    private TextView mTvAppName;
    private boolean isStartAnimation;
    private ImageView mImgLogo;
    protected boolean isShowingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_splash);
        this.mProgressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Circle doubleBounce = new Circle();
        mProgressBar.setIndeterminateDrawable(doubleBounce);
        mImgLogo = (ImageView) findViewById(R.id.img_logo);

        final LinearLayout splashLogoContainer = (LinearLayout) findViewById(R.id.splash_logo_spinner_layout);
        final LinearLayout loginContainer = (LinearLayout) findViewById(R.id.splash_login_form);

        mProgressBar.setVisibility(View.INVISIBLE);

        if (EMPAuthProviderWithStorage.getInstance().isAuthenticated()) {
            loginContainer.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            EMPAuthProviderWithStorage.getInstance().checkAuth(new IAuthenticationListener() {
                @Override
                public void onAuthSuccess(String sessionToken) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Intent mIntent = new Intent(Splash.this, MainActivity.class);
                    startActivity(mIntent);
                    finish();
                }

                @Override
                public void onAuthError(Error error) {
                    splashLogoContainer.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_move_logo_up));
                    loginContainer.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
        else {
            splashLogoContainer.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_move_logo_up));
        }

        Button btn = (Button) findViewById(R.id.btn_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoading = true;
                loginContainer.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);

                DeviceInfo device = DeviceInfo.collect(getApplicationContext());
                EditText usernameView = (EditText) findViewById(R.id.input_username);
                EditText passwordView = (EditText) findViewById(R.id.input_password);
                EditText mfaView = (EditText) findViewById(R.id.input_mfa);

                EMPAuthProviderWithStorage.getInstance().login(
                        true,
                        usernameView.getText().toString(),
                        passwordView.getText().toString(),
                        mfaView.getText().toString(),
                        new IAuthenticationListener() {
                            @Override
                            public void onAuthSuccess(String sessionToken) {
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBar.setVisibility(View.INVISIBLE);
                                        Intent mIntent = new Intent(Splash.this, MainActivity.class);
                                        startActivity(mIntent);
                                        finish();
                                    }
                                }, 500);
                            }

                            @Override
                            public void onAuthError(Error error) {
                                isLoading = false;
                                loginContainer.setVisibility(View.VISIBLE);
                                mProgressBar.setVisibility(View.INVISIBLE);
                            }
                        });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isPressBack) {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void removeSharedValue(String property) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("EMPDemoEnigmaTv", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(property);
        editor.commit();
    }

    private void setSharedValue(String property, String value) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("EMPDemoEnigmaTv", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(property, value);
        editor.commit();
    }

    private String getSharedValue(String property) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("EMPDemoEnigmaTv", MODE_PRIVATE);
        return pref.getString(property, null);
    }



}

