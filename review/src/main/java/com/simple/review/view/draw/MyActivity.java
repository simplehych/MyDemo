package com.simple.review.view.draw;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.simple.review.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hych
 * @date 2018/8/1 14:10
 */
public class MyActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_test);
//        MyListView listView = (MyListView) findViewById(R.id.act_draw_list_view);
//
//        final List<String> data = fetchData();
//        final MyAdapter myAdapter = new MyAdapter(this, 0, data);
//        listView.setAdapter(myAdapter);
//
//        listView.setOnDeleteListener(new MyListView.OnDeleteListener() {
//            @Override
//            public void onDelete(int position) {
//                data.remove(position);
//                myAdapter.notifyDataSetChanged();
//            }
//        });

    }

    private List<String> fetchData() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            data.add("hi, i am " + i);
        }
        return data;
    }
}
