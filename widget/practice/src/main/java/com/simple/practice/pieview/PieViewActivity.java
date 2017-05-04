package com.simple.practice.pieview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by hych on 2017/5/4 10:42
 */

public class PieViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PieView pieView = new PieView(this);
        setContentView(pieView);

        ArrayList<PieData> datas = new ArrayList<>();
        PieData pieData = new PieData("Simple", 60);
        PieData pieData2 = new PieData("Simple", 30);
        PieData pieData3 = new PieData("Simple", 40);
        PieData pieData4 = new PieData("Simple", 20);
        PieData pieData5 = new PieData("Simple", 20);
        datas.add(pieData);
        datas.add(pieData2);
        datas.add(pieData3);
        datas.add(pieData4);
        datas.add(pieData5);
        pieView.setPieData(datas);


    }
}
