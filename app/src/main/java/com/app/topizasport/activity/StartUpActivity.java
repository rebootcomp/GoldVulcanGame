package com.app.topizasport.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.RemoteException;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.app.topizasport.R;
import com.app.topizasport.ScreenUtils;

public class StartUpActivity extends AppCompatActivity {

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
        //Toast.makeText(this, saveText, Toast.LENGTH_SHORT).show();
       // saveText = "main";
        if (saveText.equals("main")) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        else if (saveText.equals("browser")) {
//                if(!isOnline(getApplicationContext())){
//                   // Toast.makeText(this, "noInternet", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(), NoConnectionActivity.class));
//                    finish();
//                }else {
                   // Toast.makeText(this, "online", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(StartUpActivity.this, WebViewActivity.class);
                    intent.putExtra("url", "http://lovivulkanudachi.ru/");
           // intent.putExtra("url", "http://vk.com" + refer);
                    startActivity(intent);
                    finish();
              //  }
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
                        // API not available on the current Play Store app
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        // Connection could not be established
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
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
                Intent intent = new Intent(StartUpActivity.this, WebViewActivity.class);
                intent.putExtra("url", "http://lovivulkanudachi.ru/?source=" + refer);
               // intent.putExtra("url", "http://vk.com" + refer);
                startActivity(intent);
                finish();
            }
        });
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
            return true;
        return false;
    }
}