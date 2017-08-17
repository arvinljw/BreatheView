package net.arvin.breatheview.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.arvin.breatheview.BreatheView;
import net.arvin.breatheview.CircleBreatheView;

public class SecondActivity extends AppCompatActivity implements BreatheView.IAnimEndListener {

    private CircleBreatheView breatheView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        breatheView = (CircleBreatheView) findViewById(R.id.circleBreatheView);
        breatheView.startScaleBigger();

        breatheView.setAnimEndListener(this);
    }

    @Override
    public void onBackPressed() {
        breatheView.setBgColor(getIntent().getIntExtra(MainActivity.BG_COLOR, Color.parseColor(BreatheView.BG_DEFAULT_COLOR)));
        breatheView.startScaleSmaller();
    }

    @Override
    public void onAnimEnd(boolean isOpen) {
        if (!isOpen) {
            finish();
            overridePendingTransition(0, 0);
        } else {
            breatheView.setVisibility(View.GONE);
        }
    }
}
