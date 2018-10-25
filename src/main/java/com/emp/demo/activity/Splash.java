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
import android.widget.Toast;

import net.ericsson.emovs.exposure.auth.EMPAuthProviderWithStorage;
import net.ericsson.emovs.exposure.interfaces.IAuthenticationListener;

import com.emp.demo.R;
import com.emp.demo.app.AppController;
import com.emp.demo.app.Constants;
import com.github.ybq.android.spinkit.style.Circle;

import net.ericsson.emovs.exposure.metadata.EMPMetadataProvider;
import net.ericsson.emovs.playback.PlaybackProperties;
import net.ericsson.emovs.utilities.emp.EMPRegistry;
import net.ericsson.emovs.utilities.interfaces.IMetadataCallback;
import net.ericsson.emovs.utilities.models.EmpAsset;
import net.ericsson.emovs.utilities.models.EmpChannel;
import net.ericsson.emovs.utilities.models.EmpCustomer;
import net.ericsson.emovs.utilities.models.EmpProgram;
import net.ericsson.emovs.utilities.system.DeviceInfo;
import net.ericsson.emovs.utilities.errors.Error;

import java.util.Calendar;

public class Splash extends Activity {

    public static final String TAG = Splash.class.getSimpleName();

    private static final String CUSTOMER_UNIT_KEY = "CU";
    private static final String BUSINESS_UNIT_KEY = "BU";
    private static final String ENVIRONMENT_KEY = "ENV";
    private static final String TEST_KEY = "TEST";
    private static final String ASSET_ID_KEY = "assetId";
    private static final String CHANNEL_ID_KEY = "channelId";
    private static final String PROGRAM_ID_KEY = "programId";
    private static final String PLAY_FROM_KEY = "playFrom";

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

    private String mAssetId = "";
    private String mChannelId = "";
    private String mProgramId = "";
    private String mPlayFrom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_splash);

        extractIntentProps();

        this.mProgressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Circle doubleBounce = new Circle();
        mProgressBar.setIndeterminateDrawable(doubleBounce);
        mImgLogo = (ImageView) findViewById(R.id.img_logo);

        EMPMetadataProvider.getInstance().getMainJson(new IMetadataCallback<EmpCustomer>() {
            @Override
            public void onMetadata(EmpCustomer metadata) {
                final String logoUrl = metadata.getLogoUrl();
                final String serviceName = metadata.getServiceName();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AppController.PICASSO.load(logoUrl).into(mImgLogo);
                        TextView copyright = findViewById(R.id.tv_copyright);
                        int year = Calendar.getInstance().get(Calendar.YEAR);
                        copyright.setText("©" + year + " " + serviceName);
                    }
                });
            }

            @Override
            public void onError(Error error) {
                Toast.makeText(getApplicationContext(), "App logo not found.", Toast.LENGTH_SHORT).show();
            }
        });

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

                    launchMainActivity();
                    checkAndLaunchPlayerActivity();

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

                                        launchMainActivity();
                                        checkAndLaunchPlayerActivity();

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

    private void launchMainActivity() {
        Intent mIntent = new Intent(Splash.this, MainActivity.class);
        startActivity(mIntent);
    }

    private void checkAndLaunchPlayerActivity() {
        PlaybackProperties properties = null;

        if (!mPlayFrom.isEmpty()) {
            if (mPlayFrom.equals("beginning")) {
                properties = new PlaybackProperties().withPlayFrom(PlaybackProperties.PlayFrom.BEGINNING);
            } else if (mPlayFrom.equals("bookmark")) {
                properties = new PlaybackProperties().withPlayFrom(PlaybackProperties.PlayFrom.BOOKMARK);
            } else if (mPlayFrom.equals("defaultBehavior")) {
                properties = new PlaybackProperties().withPlayFrom(PlaybackProperties.PlayFrom.START_TIME_DEFAULT);
            } else if (mPlayFrom.startsWith("customPosition") || mPlayFrom.startsWith("customTime")) {
                String[] playFromParams = mPlayFrom.split(":");
                properties = new PlaybackProperties().withPlayFrom(
                        new PlaybackProperties.PlayFrom.StartTime(Long.parseLong(playFromParams[1])));
            }
        }

        if (!mAssetId.isEmpty()) {
            EmpAsset asset = new EmpAsset();
            asset.assetId = mAssetId;
            AppController.playAsset(Splash.this, asset, properties);
        } else {
            if (!mChannelId.isEmpty()) {
                if (!mProgramId.isEmpty()) {
                    EmpProgram program = new EmpProgram();
                    program.channelId = mChannelId;
                    program.programId = mProgramId;
                    AppController.playAsset(Splash.this, program, properties);
                } else {
                    EmpChannel channel = new EmpChannel();
                    channel.channelId = mChannelId;
                    AppController.playAsset(Splash.this, channel, properties);
                }
            }
        }
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


    private void extractIntentProps() {
        Intent intent = getIntent();
        if (intent != null && intent.getAction().equals("com.emp.demo.launchfrombrowser")){
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                if (bundle.containsKey(CUSTOMER_UNIT_KEY)) {
                    Constants.CUSTOMER = bundle.getString(CUSTOMER_UNIT_KEY);
                }
                if (bundle.containsKey(BUSINESS_UNIT_KEY)) {
                    Constants.BUSSINESS_UNIT = bundle.getString(BUSINESS_UNIT_KEY);;
                }
                if (bundle.containsKey(ENVIRONMENT_KEY)) {
                    String env = bundle.getString(ENVIRONMENT_KEY);
                    if ("DEV".equals(env)) {
                        Constants.API_URL = Constants.API_URL_DEV;
                    } else if ("PROD".equals(env)) {
                        Constants.API_URL = Constants.API_URL_PROD;
                    }
                }
                if (bundle.containsKey(TEST_KEY)) {
                    String test = bundle.getString(TEST_KEY);
                    if ("ALL".equals(test)) {
                        Constants.TEST_MODE = test;
                    }
                }

                EMPRegistry.bindExposureContext(Constants.API_URL, Constants.CUSTOMER, Constants.BUSSINESS_UNIT);

                if (bundle.containsKey(ASSET_ID_KEY)) {
                    mAssetId = bundle.getString(ASSET_ID_KEY);
                }

                if (bundle.containsKey(CHANNEL_ID_KEY)) {
                    mChannelId = bundle.getString(CHANNEL_ID_KEY);
                }

                if (bundle.containsKey(PROGRAM_ID_KEY)) {
                    mProgramId = bundle.getString(PROGRAM_ID_KEY);
                }

                if (bundle.containsKey(PLAY_FROM_KEY)) {
                    mPlayFrom = bundle.getString(PLAY_FROM_KEY);
                }
            }
        }
    }
}

