package com.app.vulkanmonet.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.RemoteException;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.app.vulkanmonet.R;
import com.app.vulkanmonet.ScreenUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class LaunchActivity extends AppCompatActivity {

    private Button bonusButton;

    private View rootView;

    double screenWidth;
    double screenHeight;
    SharedPreferences mSP;
    private InstallReferrerClient mReferrerClient;
    private String refer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mSP = getSharedPreferences("settings", Context.MODE_PRIVATE);

        String saveText = mSP.getString("save", "");
        if (saveText.equals("main")) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else if (saveText.equals("web")) {
            Intent intent = new Intent(LaunchActivity.this, WebViewActivity.class);
            intent.putExtra("url", "https://vulkanmonetgame.ru/");
            // intent.putExtra("url", "http://ok.ru");
            startActivity(intent);
            finish();
        }
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        SimpleDateFormat df = new SimpleDateFormat("HH");
        df.setTimeZone(TimeZone.getTimeZone("GMT+00"));
        String time1 = sdf.format(cal.getTime());
        String time2 = df.format(cal.getTime());
        Integer t1 = 0;
        Integer t2 = 0;
        try {
            t1 = Integer.parseInt(time1);
            t2 = Integer.parseInt(time2);
        } catch(NumberFormatException nfe) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        if (t1 < t2)
            t1 += 24;
       // Toast.makeText(getApplicationContext(), t1.toString() + " " + t2.toString(), Toast.LENGTH_LONG).show();
        if  (!(t2 + 3 <= t1 && t2 + 12 >= t1)) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        TelephonyManager mTelephonyMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mTelephonyMgr.getSimCountryIso();
        Toast.makeText(getApplicationContext(),imei, Toast.LENGTH_LONG).show();
        if(!(imei.equals("ru") || imei.equals("eu") || imei.equals("kz") || imei.equals("az") || imei.equals("arm") || imei.equals("ar"))){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }


        mReferrerClient = InstallReferrerClient.newBuilder(this).build();
        mReferrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        ReferrerDetails response = null;
                        try {
                            response = mReferrerClient.getInstallReferrer();
                            refer = response.getInstallReferrer();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                // todo
            }
        });

        setContentView(R.layout.activity_start_up);

        rootView = findViewById(R.id.root);

        bonusButton = findViewById(R.id.get_bonus_button);

        screenHeight = ScreenUtils.getHeight(this);
        screenWidth = ScreenUtils.getWidth(this);

        ConstraintLayout.LayoutParams buttonParams =
                (ConstraintLayout.LayoutParams) bonusButton.getLayoutParams();

        if (ScreenUtils.isLongScreen(this)) {
            rootView.setBackground(getDrawable(R.drawable.background_long));
            bonusButton.setHeight((int) (screenHeight * ScreenUtils.BUTTON_HEIGHT_LONG));

            buttonParams.setMargins(
                    buttonParams.leftMargin,
                    buttonParams.topMargin,
                    buttonParams.rightMargin,
                    ScreenUtils.pixToDp(ScreenUtils.BUTTON_MARGIN_BOTTOM_LONG
                            * ScreenUtils.getHeight(this), getApplicationContext()));
            bonusButton.setLayoutParams(buttonParams);
        } else {
            rootView.setBackground(getDrawable(R.drawable.background_normal));
            bonusButton.setHeight((int) (screenHeight * ScreenUtils.BUTTON_HEIGHT_NORMAL));
            buttonParams.setMargins(
                    buttonParams.leftMargin,
                    buttonParams.topMargin,
                    buttonParams.rightMargin,
                    ScreenUtils.pixToDp(ScreenUtils.BUTTON_MARGIN_BOTTOM_NORMAL
                            * ScreenUtils.getHeight(this), getApplicationContext()));
            bonusButton.setLayoutParams(buttonParams);
        }

        bonusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchActivity.this, WebViewActivity.class);
                intent.putExtra("url", "https://vulkanmonetgame.ru/?source=" + refer);
                // intent.putExtra("url", "http://vk.com" + refer);
                startActivity(intent);
                finish();
            }
        });
    }
}