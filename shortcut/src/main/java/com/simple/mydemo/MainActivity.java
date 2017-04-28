package com.simple.mydemo;

import android.app.AliasActivity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.simple.mydemo.helper.ShortcutSuperUtils;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static final String INSTITUTION_ID = "institution_id";
    public static final String INSTITUTION_URL = "institution_url";
    public static final String INSTITUTION_NAME = "institution_name";
    public static final String TYPE = "type";

    private Bitmap logobitmap;

    private String cut3 = "";
    private String cut4 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logobitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        Button people1 = (Button) findViewById(R.id.people1);
        Button people2 = (Button) findViewById(R.id.people2);

        Button test3 = (Button) findViewById(R.id.test3);
        Button test4 = (Button) findViewById(R.id.test4);

        people1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                peopleCut("people1", "people1", "people1", "people1", "people1", 1);
            }
        });

        people2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                peopleCut("people2", "people2", "people2", "people2", "people2", 2);
            }
        });

        test3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cut3("cut3", 3);
            }
        });

        test4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cut3("cut4", 4);
            }
        });

    }

    /**
     * 创建快捷图标
     */
    private void peopleCut(String id, final String name, String wap_url, String logo, String likes, int i) {
        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        //TODO 待考证必须有以上两行，否则跳转Activity的时候getIntent获取不到，或者只是获取之前的设置好的值，

        shortcutIntent.setClass(this, PeopleActivity.class);

        shortcutIntent.putExtra(INSTITUTION_ID, id);
        shortcutIntent.putExtra(INSTITUTION_NAME, name);
        shortcutIntent.putExtra(INSTITUTION_URL, wap_url);
        shortcutIntent.putExtra(TYPE, "0");
        // 安装的Intent
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 快捷名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        // 快捷图标
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON, logobitmap);

//			Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.icon_gov_default);
//			shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

        // 快捷图标是允许重复
        shortcut.putExtra("duplicate", false);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        // 发送广播
        sendBroadcast(shortcut);
    }

    public void cut3(String name, int i) {
        Intent shortcutIntent = new Intent();
        shortcutIntent.setAction(Intent.ACTION_VIEW);
//        shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        shortcutIntent.setData(Uri.parse("peopledailyphone://gov?institution_id=2"));
        //TODO 使用别名的方式，创建快捷方式，部分机型启动不同的ACitvity可以，但是相同Activity只能创建一个快捷方式，但是显示在manifest注册Activity的时候，过于复杂，而且创建个数不确定
        //查看动态代理的模式
//        shortcutIntent.setComponent(new ComponentName(this.getPackageName(), "com.simple.mydemo.AliasCutActivity" + i));
        /**
         * 创建一个Bundle对象让其保存将要传递的值
         */
//        Bundle bundle = new Bundle();
//        bundle.putString(INSTITUTION_NAME, name);
//        shortcutIntent.putExtras(bundle);
        /**
         * TODO
         * 设置这条属性，可以使点击快捷方式后关闭其他的任务栈的其他activity，然后创建指定的acticity
         */
//        shortcutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Log.e("Simple", "--");
        Log.e("Simple", "--");
        Log.e("Simple", "--");
        Log.e("Simple", "--");
        Log.e("Simple", "--");
        Log.e("Simple", "--");
        Log.e("Simple", "---------- 发送前 : ");
        ShortcutSuperUtils.isShortCutExist(this, name, shortcutIntent);

        Intent shortcut = new Intent(Intent.ACTION_CREATE_SHORTCUT);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.ic_launcher);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        shortcut.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        this.sendBroadcast(shortcut);
        Log.e("Simple", "--");
        Log.e("Simple", "--");
        Log.e("Simple", "--");
        Log.e("Simple", "--");
        Log.e("Simple", "--");
        Log.e("Simple", "--");
        Log.e("Simple", "---------- 发送后 : ");
        ShortcutSuperUtils.isShortCutExist(this, name, shortcutIntent);

    }

    public void cut4(String name) {

        Intent shortcutIntent = new Intent();
        shortcutIntent.setAction(Intent.ACTION_VIEW);
        shortcutIntent.setData(Uri.parse("peopledailyphone://gov?institution_id=2"));

        /**
         * 创建一个Bundle对象让其保存将要传递的值
         */
        Bundle bundle = new Bundle();
        bundle.putString(INSTITUTION_NAME, name);
        shortcutIntent.putExtras(bundle);
        /**
         * 设置这条属性，可以使点击快捷方式后关闭其他的任务栈的其他activity，然后创建指定的acticity
         */
        shortcutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        Intent shortcut = new Intent(Intent.ACTION_CREATE_SHORTCUT);
        shortcut.putExtra("duplicate", false);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.ic_launcher);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        shortcut.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        this.sendBroadcast(shortcut);

    }

}
