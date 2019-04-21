package com.app.lionnews.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.lionnews.R;
import com.app.lionnews.util.ScreenUtils;

public class StartUpActivity extends AppCompatActivity {


    private Button bonusButton;

    private View rootView;

    double screenWidth;
    double screenHeight;
    SharedPreferences mSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSP = getPreferences(MODE_PRIVATE);

        String saveText = mSP.getString("save","");
        Toast.makeText(this,saveText,Toast.LENGTH_SHORT).show();
        if(saveText == "moder")
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

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
                startActivity(new Intent(getApplicationContext(), BrowserActivity.class));
            }
        });

    }
}