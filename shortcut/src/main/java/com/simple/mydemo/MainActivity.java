package com.simple.mydemo;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.simple.mydemo.helper.ShortcutSuperUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.startActivityByUri).setOnClickListener(this);
        findViewById(R.id.createShortcutToUri).setOnClickListener(this);
        findViewById(R.id.createShortcutToActivity).setOnClickListener(this);
        findViewById(R.id.createShortcutToActivityAlisa_1).setOnClickListener(this);
        findViewById(R.id.createShortcutToActivityAlisa_2).setOnClickListener(this);
        findViewById(R.id.createShortcutToScheme).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.startActivityByUri:
                //通过Uri方式启动Activity
                startActivityByUri();
                break;
            case R.id.createShortcutToUri:
                //创建桌面图标跳转至指定Uri
                createShortcutToUri();
                break;
            case R.id.createShortcutToActivity:
                //创建桌面图标跳转至指定Activity
                createShortcutToActivity("createShortcutToActivity", R.mipmap.ic_launcher);
                break;
            case R.id.createShortcutToActivityAlisa_1:
                //创建桌面图标跳转至指定Activity 别名方式 图标1 --使用createShortcutToActivityAlisa()方法和下一个使用的方法一样
                createShortcutToActivityAlisa("1 别名", 1);
                break;
            case R.id.createShortcutToActivityAlisa_2:
                //创建桌面图标跳转至指定Activity 别名方式 图标2
                createShortcutToActivityAlisa("2 别名", 2);
                break;
            case R.id.createShortcutToScheme:
                //创建桌面图标跳转至指定Activity  Scheme方式
                createShortcutToScheme("Scheme");
                break;
            default:
                break;
        }
    }

    /**
     * 通过设置Uri启动另一个网址或者页面,此为调用系统浏览器打开百度地址，项目中可以在webview打开
     */
    public void startActivityByUri() {
        Uri uri = Uri.parse("http://www.baidu.com"); //注意此处 http,在项目中也可以通过更改Scheme进行跳转网址
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);//不可以使用Intent.ACTION_MAIN，No Activity found to handle Intent
        startActivity(intent);
    }

    /**
     * 创建桌面快捷图标跳转至指定的Uri，使用此方式删除应用之后，创建的快捷图标不会删除，如果采用跳转到本App的Activity的方式生成桌面快捷图标会删除
     * 启动之后为打开另一个App，点击返回之后会生成一次按照该App的回退方式返回
     */
    public void createShortcutToUri() {
        /**
         * pending：悬而未决，即将发生。 就是创建的快捷方式点击之后跳转的Intent
         * 同样不可以使用Intent.ACTION_MAIN，点击图标之后显示未安装该应用
         */
        Uri uri = Uri.parse("http://www.baidu.com");
        Intent pendingIntent = new Intent(Intent.ACTION_VIEW, uri);
//        pendingIntent.addCategory(Intent.CATEGORY_LAUNCHER);  不可以添加该参数字段，一切与本App相关的设置都取消


        //创建图标的过程 --start
        Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "我是图标名称");//桌面快捷方式标题，最终图标的信息会存到数据库，相当于title的字段
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, R.mipmap.ic_launcher);//桌面快捷方式图标，可动态生成多个不同图标的快捷方式
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, pendingIntent);//桌面快捷方式的动作，点击图标后执行的动作，即设置好的pendingIntent
        //--end
        sendBroadcast(shortcutIntent);//发送创建图标的广播
    }

    /**
     * people 创建桌面图标跳转至本App的指定Activity，随本App卸载一起消失
     */
    public void createShortcutToActivity(String name, int shortcutIcon) {

        Intent pendingIntent = new Intent(Intent.ACTION_VIEW);//此方式ACTION_MAIN，ACTION_VIEW均可创建并跳转成功
//        pendingIntent.addCategory(Intent.CATEGORY_LAUNCHER); //测试验证可以不需要
        //TODO 待考证必须有以上两行，否则跳转Activity的时候getIntent获取不到，或者只是获取之前的设置好的值，
        pendingIntent.setClass(this, PendingActivity.class);//跳转至本App的PendingActivity_1页面
        pendingIntent.putExtra("携带参数", "我是携带的参数: " + name);

        //创建图标的过程 --start
        Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), shortcutIcon);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);//设置图标同样可以用Intent.EXTRA_SHORTCUT_ICON_RESOURCE
        shortcutIntent.putExtra("duplicate", false);//不写此字段，默认是不可以重复的，但安卓机型原生系统修改可能不支持该字段，可以进行数据库查询判断
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, pendingIntent);
        //--end
        sendBroadcast(shortcutIntent);

    }

    /**
     * 生成多个桌面图标的话，在一般手机上只要更换图标的名称就是可以的，但是最近遇到一个相当棘手的bug  如下：
     * <p>
     * 需要创建多个桌面快捷方式，但是在中兴ZTE A2015 6.0.1等部分机型上若生成快捷方式指定跳转至一个Activity的情况下(比如更换名称但都是跳转到PendingActivity_1这个界面)，
     * ，首次可以生成多个桌面图标，但是点击Home键或者返回至桌面后，只能传递数据覆盖，无法生成多个快捷方式，
     * 但是也不能在清单文件注册无限个Activity，使用代理Activity的话，也相当于替换Activity，（代理解释参考：https://zhuanlan.zhihu.com/p/21348609）
     * 同样需要手动创建多个Activity，但是不满足需求，现在的问题是 生成桌面快捷方式需要不同的Activity才能创建多个，否则只能创建一个，可能会创建N多个
     * <p>
     * 初期想到的解决办法是，使用别名的方法。在清单文件显示写入多个别名，不可能写N多个，虽有限制，但可以解决仅仅能生成一个问题，但使用该方法对于正常机型确是一种累赘
     * 实现方式如下，仅供学习，但不采用
     *
     * @param name
     * @param i
     */
    public void createShortcutToActivityAlisa(String name, int i) {
        Intent pendingIntent = new Intent();
        pendingIntent.setAction(Intent.ACTION_VIEW);
        pendingIntent.setClass(this, PendingActivity.class);
        /**
         * 注意！！！别名的name需要加上包名，否则图标不会找到，显示未安装该应用的提示
         *
         *  使用别名的方式在清单文件中注册俩个（Activity别名参考：http://www.open-open.com/lib/view/open1483148577810.html）
         *  com.simple.mydemo.PendingActivity_1_Alias_1
         *  com.simple.mydemo.PendingActivity_1_Alias_2
         *  测试方法略显粗暴！！所以就写了俩个动态传进来末尾不同的数字
         *  在实际项目总若是保存在缓存中，进行清理的时候数据可能会失效，所以需要慎重处理
         *
         */
        pendingIntent.setComponent(new ComponentName(this.getPackageName(), "com.simple.mydemo.PendingActivity_Alias_" + i));
        Bundle bundle = new Bundle();
        bundle.putString("携带参数", "我是携带的参数: " + name);
        pendingIntent.putExtras(bundle);
        /**
         * 网上搜到有这条属性，有需求可添加
         * 设置这条属性，可以使点击快捷方式后关闭其他的任务栈的其他activity，然后创建指定的acticity
         * （参考：http://blog.csdn.net/bjp000111/article/details/51363981）
         */
//        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "我是图标名称");
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, R.mipmap.ic_launcher);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, pendingIntent);
        sendBroadcast(shortcutIntent);
        /**
         * 安卓各大厂商修改ROM，可能导致有的字段不支持，详情移架医生早年写的博客，进行数据库之类的分析，下面是摘自其中utils类进行查看
         * http://www.jianshu.com/p/dc3d04337d00?utm_campaign=haruki&utm_content=note&utm_medium=reader_share&utm_source=qq
         */
        ShortcutSuperUtils.isShortCutExist(this, name, shortcutIntent);
    }

    /**
     * 精品来了，App内部通过Scheme的方式进行创建图标，缺点：每次的图标启动的页面参数写死，实际项目中，若后台参数改变则地址可能找不到
     * 但可以满足需求，也可以解决，比如携带参数进来之后，然后进入页面的时候可以更改参数重新刷一次页面
     * <p>
     * (精品地址：http://blog.csdn.net/wanggsx918/article/details/40541993)
     *
     * @param name
     */
    public void createShortcutToScheme(String name) {

        /**
         * 仅此而已，不可进行其他的与本App相关的设置，然后在AndroidManifest文件进行配置
         * 对Activity添加意图过滤器，可以添加多个intent—filter进行筛选
         *
         */
        Intent pendingIntent = new Intent();
        pendingIntent.setAction(Intent.ACTION_VIEW);//切记不可用Intent.ACTION_MAIN
        pendingIntent.setData(Uri.parse("http://www.baidu.com"));//实际项目总将http换位和后台协调好的Scheme，注意清单文件更改，在这里我使用是http所以跳转的是浏览器
        //不要纠结，以上三行等价于这个 Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("IamScheme://www.baidu.com"));
        pendingIntent.putExtra("携带参数", "我是携带的参数: " + name);//可以携带,若有别的需求不满足可在携带的参数中做文章，也可在Scheme做文章

        /**
         * 创建桌面快捷方式的图标比较单一，目前网址的资料大多数12,13年的，Android7.0以后会提供ShortcutInfo的Api，效果也比较cool
         * 之前遇到的问题，应该在6.0的部分机型会出现，但是Scheme的方法完美的解决问题
         */
        Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "我是图标名称");
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, R.mipmap.ic_launcher);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, pendingIntent);
        sendBroadcast(shortcutIntent);
    }


}
