package com.sun.toy;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    Button btnSingleCalendar;
    Button btnMultiCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    @Override
    public void initView() {
        btnSingleCalendar = (Button) findViewById(R.id.btn1);
        btnMultiCalendar = (Button) findViewById(R.id.btn2);
    }

    @Override
    public void initControl() {
        super.initControl();
        btnSingleCalendar.setOnClickListener(this);
        btnMultiCalendar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn1) {
            Intent intent = new Intent(this, SingleCalendarActivity.class);
            startActivity(intent);
        } else if (id == R.id.btn2) {
            Intent intent = new Intent(this, MultiCalendarActivity.class);
            startActivity(intent);
        }
    }


}
