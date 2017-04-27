package com.simple.mydemo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by hych on 2017/4/27 14:48.
 */

public class ShortcutManager {


    //添加快捷方式
    public static final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    //移除快捷方式
    public static final String ACTION_REMOVE_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";

    private Context context;

    public ShortcutManager(Context context) {
//        context = AppProvide.applicationContext();
        this.context = context;
    }

    protected void addShortcut(String name) {
        Intent addShortcutIntent = new Intent(ACTION_ADD_SHORTCUT);

        // 不允许重复创建
        addShortcutIntent.putExtra("duplicate", false);// 经测试不是根据快捷方式的名字判断重复的
        // 应该是根据快链的Intent来判断是否重复的,即Intent.EXTRA_SHORTCUT_INTENT字段的value
        // 但是名称不同时，虽然有的手机系统会显示Toast提示重复，仍然会建立快链
        // 屏幕上没有空间时会提示
        // 注意：重复创建的行为MIUI和三星手机上不太一样，小米上似乎不能重复创建快捷方式

        // 名字
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

        // 图标
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context,
                        R.mipmap.ic_launcher));

        // 设置关联程序
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.setClass(context, PeopleActivity.class);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        addShortcutIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);
        // 发送广播
        context.sendBroadcast(addShortcutIntent);
    }

    protected void removeShortcut(String name) {
        // remove shortcut的方法在小米系统上不管用，在三星上可以移除
        Intent intent = new Intent(ACTION_REMOVE_SHORTCUT);

        // 名字
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

        // 设置关联程序
        Intent launcherIntent = new Intent(context,
                PeopleActivity.class).setAction(Intent.ACTION_MAIN);

        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);

        // 发送广播
        context.sendBroadcast(intent);
    }

    protected String getAuthorityFromPermission() {
        // 先得到默认的Launcher
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        PackageManager mPackageManager = context.getPackageManager();
        ResolveInfo resolveInfo = mPackageManager.resolveActivity(intent, 0);
        if (resolveInfo == null) {
            return null;
        }

        List<ProviderInfo> info = mPackageManager.queryContentProviders(resolveInfo.activityInfo.packageName,
                resolveInfo.activityInfo.applicationInfo.uid, PackageManager.GET_PROVIDERS);

        if (info != null) {
            for (int j = 0; j < info.size(); j++) {
                ProviderInfo provider = info.get(j);
                if (provider.readPermission == null) {
                    continue;
                }
                if (Pattern.matches(".*launcher.*READ_SETTINGS", provider.readPermission)) {
                    return provider.authority;
                }
            }
        }
        return null;
    }


    protected boolean hasShortcut(String appName) {
        long start = System.currentTimeMillis();
        String authority = getAuthorityFromPermission();
        if (authority == null) {
            return false;
        }
        long end = System.currentTimeMillis() - start;
        String url = "content://" + authority + "/favorites?notify=true";
        try {
            Uri CONTENT_URI = Uri.parse(url);
            Cursor c = context.getContentResolver().query(CONTENT_URI, null, " title= ? ", new String[]{appName}, null);
            if (c != null && c.moveToNext()) {
                c.close();
                return true;
            }
        } catch (Exception e) {

        }

        return false;
    }

}
