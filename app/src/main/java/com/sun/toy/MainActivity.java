package com.sun.toy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btn1)
    Button btnSingleCalendar;

    @Bind(R.id.btn2)
    Button btnMultiCalendar;

    @OnClick({R.id.btn1, R.id.btn2})
    void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn1) {
            Intent intent = new Intent(this, SingleCalendarActivity.class);
            startActivity(intent);
        } else if (id == R.id.btn2) {
            Intent intent = new Intent(this, MultiCalendarActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


}
