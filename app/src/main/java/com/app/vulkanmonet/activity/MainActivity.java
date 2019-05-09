package com.app.vulkanmonet.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.vulkanmonet.R;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView count;
    private Integer lvl = 0;
    private int money[] = {1, 5, 10, 20, 50, 100, 500, 1000, 10000, 1000000};
    private int cost[] = {99, 499, 1499, 4449, 12990, 32990, 99990, 199990, 699990, 0};
    private Random random = new Random();
    private ImageView dollar1;
    private ImageView dollar2;
    private ImageView dollar3;
    private ImageView dollar4;
    private int cnt = 0;
    private SharedPreferences mSP;
    private SharedPreferences.Editor ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final ImageButton button = findViewById(R.id.button);
        FloatingActionButton update = findViewById(R.id.update);
        count = findViewById(R.id.count);
        dollar1 = findViewById(R.id.img1);
        dollar1.setVisibility(View.INVISIBLE);
        dollar2 = findViewById(R.id.img2);
        dollar2.setVisibility(View.INVISIBLE);
        dollar3 = findViewById(R.id.img3);
        dollar3.setVisibility(View.INVISIBLE);
        dollar4 = findViewById(R.id.img4);
        dollar4.setVisibility(View.INVISIBLE);

        mSP = getSharedPreferences("settings", Context.MODE_PRIVATE);
        ed = mSP.edit();
        String goldCnt = mSP.getString("gold", "0");
        String tmplvl = mSP.getString("lvl", "0");
        count.setText(goldCnt);
        lvl = (tmplvl.charAt(0) - '0');

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rnd = random.nextInt(5);
                cnt = (cnt + 1) % 4;
                Animation anim;
                if (rnd == 0)
                    anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim1);
                else if (rnd == 1)
                    anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim2);
                else if (rnd == 2)
                    anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim3);
                else if (rnd == 3)
                    anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim4);
                else
                    anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim5);

                ImageView dollar;
                if (cnt == 0)
                    dollar = dollar1;
                else if (cnt == 1)
                    dollar = dollar2;
                else if (cnt == 2)
                    dollar = dollar3;
                else
                    dollar = dollar4;
                dollar.startAnimation(anim);
                anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.vulcanim);
                button.startAnimation(anim);
                String s = count.getText().toString();
                Integer foo;
                try {
                    foo = Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    foo = 0;
                }
                int now = random.nextInt(lvl + 1);
                foo += money[now];
                ed.putString("gold", foo.toString());
                ed.apply();
                count.setText(foo.toString());
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = count.getText().toString();
                Integer cnt;
                try {
                    cnt = Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    cnt = 0;
                }
                if (cost[lvl] <= cnt) {
                    cnt -= cost[lvl];
                    if (lvl < 9) {
                        lvl++;
                        Integer tmp = money[lvl];
                        Toast.makeText(getApplicationContext(), "Вы получили новую купюру " + tmp.toString() + "$", Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(getApplicationContext(), "Игра пройдена)", Toast.LENGTH_LONG).show();
                    ed.putString("gold", cnt.toString());
                    ed.putString("lvl", lvl.toString());
                    ed.apply();
                    count.setText(cnt.toString());
                } else {
                    Integer tmp = cost[lvl];
                    Toast.makeText(getApplicationContext(), "Стоимость новой купюры: " + tmp.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
