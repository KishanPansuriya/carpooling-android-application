package com.cybertoper.journeys.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.cybertoper.journeys.R;
import com.cybertoper.journeys.authentication.SessionManager;

/**
 * Created by DRX on 8/5/2015.
 */

public class splash extends Activity {

    Animation animFadein, animBlink;
    ImageView logo_pic, logo_name;
    SessionManager session;

    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.splash);

        session = new SessionManager(getApplicationContext());

        logo_pic = (ImageView) findViewById(R.id.logo_pic);
        logo_name = (ImageView) findViewById(R.id.logo_name);


        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        animBlink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);

        logo_name.startAnimation(animFadein);
        logo_pic.startAnimation(animBlink);

        Thread t = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    session.checkLogin();
                    /*Intent i = new Intent(getApplicationContext(), Login.class);
                    startActivity(i);*/
                }
            }
        };
        t.start();
    }

    public void onPause() {
        super.onPause();
        finish();
    }
}
