package com.df.androidviewtest;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    RippleView rippleView;
    TunaRipple ripple;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ripple = (TunaRipple) findViewById(R.id.tunaRipple);
        ripple.play();


        rippleView = (RippleView) findViewById(R.id.view2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        rippleView.play();
        ripple.play();
        handler.removeMessages(111);
        handler.removeMessages(222);
        handler.sendEmptyMessageDelayed(111, 2000);
        handler.sendEmptyMessageDelayed(222, 10000);
    }

    private long lastTime = System.currentTimeMillis();

    android.os.Handler handler = new android.os.Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            switch (msg.what) {
            case 222:
                rippleView.setRippleInnerAnimationColor(Color.parseColor("#ff9000"), Color.parseColor("#ff6600"));
                rippleView.setRippleOuterAnimationColor(Color.parseColor("#ff9000"), Color.parseColor("#ff6600"));
                break;
            case 111:

                long t = System.currentTimeMillis();
                Log.d("time", "time=" + (t - lastTime));
                lastTime = t;


                rippleView.play();
                ripple.play();
                this.sendEmptyMessageDelayed(111, 2000);
                break;
            }

        }


    };

}
