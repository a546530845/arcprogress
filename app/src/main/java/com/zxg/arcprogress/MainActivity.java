package com.zxg.arcprogress;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zxg.arcprogress.widget.ArcProgress;

public class MainActivity extends AppCompatActivity {
    private double money;

    private ArcProgress main_fgt_arc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_fgt_arc = (ArcProgress) findViewById(R.id.main_fgt_arc);

        findViewById(R.id.tx1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money = 10000.00;
                main_fgt_arc.startDraw(20, money);
                //计算progress
            }
        });
        findViewById(R.id.tx2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money = 100000.55;
                //计算progress
                main_fgt_arc.startDraw(40, money);
            }
        });
        findViewById(R.id.tx3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money = 1000000.56;
                //计算progress
                main_fgt_arc.startDraw(60, money);
            }
        });
        findViewById(R.id.tx4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money = 1000000000.56;
                //计算progress
                main_fgt_arc.startDraw(80, money);
            }
        });
    }
}
