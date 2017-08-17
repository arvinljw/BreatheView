package net.arvin.breatheview.sample;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String BG_COLOR = "bg_color";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_to_next).setOnClickListener(this);
        findViewById(R.id.tv_to_third).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Class clazz = SecondActivity.class;
        int bgColor = Color.WHITE;
        switch (v.getId()) {
            case R.id.tv_to_next:
                clazz = SecondActivity.class;
                break;
            case R.id.tv_to_third:
                clazz = ThirdActivity.class;
                break;
        }
        Intent intent = new Intent(MainActivity.this, clazz);
        intent.putExtra(BG_COLOR, bgColor);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
