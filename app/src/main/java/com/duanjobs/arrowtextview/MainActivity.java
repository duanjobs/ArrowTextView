package com.duanjobs.arrowtextview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.duanjobs.arrowtextview.widget.ArrowTextView;

public class MainActivity extends AppCompatActivity {
    private ArrowTextView test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test = (ArrowTextView) findViewById(R.id.test1);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test.setText("在爱的幸福国度,你就是我唯一,我唯一爱的就是你");
            }
        });
    }
}
