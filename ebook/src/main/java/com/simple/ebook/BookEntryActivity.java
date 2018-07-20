package com.simple.ebook;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.RelativeLayout;

import com.simple.ebook.ui.book.EBookActivity;
import com.simple.ebook.utils.FileUtils;
import com.simple.ebook.utils.NetWorkUtils;
import com.simple.ebook.utils.ToastUtils;

import java.io.File;

import static com.simple.ebook.ui.book.EBookActivity.PARAM_EBOOK_EPUB_PATH;

public class BookEntryActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    public static final String BOOK_URL = "book_url";
    private String mEPubPath;
    private RelativeLayout mProgressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_activity_entry);

        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }

        mProgressLayout = (RelativeLayout) findViewById(R.id.loading_progress_layout);

        findViewById(R.id.activity_test_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBook();
            }
        });
    }

    public static void start(Activity activity, String bookUrl) {
        Intent intent = new Intent();
        intent.setClass(activity, BookEntryActivity.class);
        intent.putExtra(BOOK_URL, bookUrl);
        activity.startActivity(intent);
    }

    private void openBook() {
        String bookUrl = getIntent().getStringExtra(BOOK_URL);
//        if (!bookUrl.endsWith(".epub") && bookUrl.startsWith("http")) {
//            ToastUtils.show("书籍地址错误，请提交反馈");
//            return;
//        }
        bookUrl = "http://peopleimg.img-cn-beijing.aliyuncs.com/2018/07/02/775bb13397697c081f68657c3248e13c4730.epub";
        final String bookIdName = bookUrl.substring(bookUrl.lastIndexOf("/") + 1, bookUrl.length());
        mEPubPath = FileUtils.getDownloadFilePath(this) + File.separator + bookIdName;

        File bookEPubFile = new File(mEPubPath);
        if (bookEPubFile.exists()) {
            jumpBookActivity(mEPubPath);
        } else {
            if (!NetWorkUtils.isNetConnected(this)) {
                ToastUtils.show(this, "当前网络不可用,请检查");
                finish();
                return;
            }

            if (!NetWorkUtils.isWifiConnected(this)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("当前网络非Wifi状态，是否继续阅读");
                final String finalBookUrl = bookUrl;
                builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        downloadBook(finalBookUrl, bookIdName);
                    }
                }).setNegativeButton("离开", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                AlertDialog alertDialog = builder.show();
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#222222"));
                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#222222"));
            } else {
                downloadBook(bookUrl, bookIdName);
            }
        }
    }


    private void jumpBookActivity(String ePubPath) {
        Intent intent = new Intent(BookEntryActivity.this, EBookActivity.class);
        intent.putExtra(PARAM_EBOOK_EPUB_PATH, ePubPath);
        startActivity(intent);
        finish();
    }

    private long mReference = -1;
    private DownloadManager downloadManager;

    private void downloadBook(String downloadUrl, String bookIdName) {

        //注册广播接收器
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(receiver, filter);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));

        //下载网络需求  手机数据流量、wifi
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

        //设置是否允许漫游网络 建立请求 默认true
        request.setAllowedOverRoaming(true);

        //设置通知类型
        setNotification(request);

        //设置下载路径
        setDownloadFilePath(request, bookIdName);

        /*在默认的情况下，通过Download Manager下载的文件是不能被Media Scanner扫描到的 。
        进而这些下载的文件（音乐、视频等）就不会在Gallery 和  Music Player这样的应用中看到。
        为了让下载的音乐文件可以被其他应用扫描到，我们需要调用Request对象的
         */
        request.allowScanningByMediaScanner();

        /*如果我们希望下载的文件可以被系统的Downloads应用扫描到并管理，
        我们需要调用Request对象的setVisibleInDownloadsUi方法，传递参数true。*/
        request.setVisibleInDownloadsUi(true);

        //设置请求的Mime
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        request.setMimeType(mimeTypeMap.getMimeTypeFromExtension(downloadUrl));

        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        if (mReference != -1) {
            /*
            下载管理器中有很多下载项，怎么知道一个资源已经下载过，避免重复下载呢？
            我的项目中的需求就是apk更新下载，用户点击更新确定按钮，第一次是直接下载，
             后面如果用户连续点击更新确定按钮，就不要重复下载了。
             可以看出来查询和操作数据库查询一样的
              */
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(mReference);
            Cursor cursor = downloadManager.query(query);
            if (!cursor.moveToFirst()) {
                // 没有记录
                startDownload(request);
            } else {
                //有记录
            }
        } else {
            startDownload(request);
        }
    }

    private void startDownload(DownloadManager.Request request) {
        showProgress();
        //开始下载
        mReference = downloadManager.enqueue(request);
    }

    /**
     * 设置状态栏中显示Notification
     */
    void setNotification(DownloadManager.Request request) {
        //设置Notification的标题
        request.setTitle("");
        //设置描述
        request.setDescription("");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
    }

    /**
     * 设置下载文件存储目录
     */
    void setDownloadFilePath(DownloadManager.Request request, String bookIdName) {
        /**
         * 方法1:
         * 目录: Android -> data -> com.app -> files -> Download -> 微信.apk
         * 这个文件是你的应用所专用的,软件卸载后，下载的文件将随着卸载全部被删除
         */
        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, bookIdName);

        /**
         * 方法2:
         * 下载的文件存放地址  SD卡 download文件夹，pp.jpg
         * 软件卸载后，下载的文件会保留
         */
        //在SD卡上创建一个文件夹
        //request.setDestinationInExternalPublicDir(  "/mydownfile/"  , "weixin.apk" ) ;

        /**
         * 方法3:
         * 如果下载的文件希望被其他的应用共享
         * 特别是那些你下载下来希望被Media Scanner扫描到的文件（比如音乐文件）
         */
        //request.setDestinationInExternalPublicDir( Environment.DIRECTORY_MUSIC,  "笨小孩.mp3" );

        /**
         * 方法4
         * 文件将存放在外部存储的确实download文件内，如果无此文件夹，创建之，如果有，下面将返回false。
         * 系统有个下载文件夹，比如小米手机系统下载文件夹  SD卡--> Download文件夹
         */
        //创建目录
//        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir();
        //设置文件存放路径
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "weixin.apk");
    }

    /**
     * 广播接受器, 下载完成监听器
     */
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                //下载完成
                //获取当前完成任务的ID
                long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                hideProgress();
                jumpBookActivity(mEPubPath);
            }

            if (action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
                //广播被点击
            }
        }
    };

    public void onClick(View v) {
        switch (v.getId()) {
            case 1:
                //取消下载， 如果一个下载被取消了，所有相关联的文件，部分下载的文件和完全下载的文件都会被删除。
                downloadManager.remove(mReference);
                break;

            case 2:
                //查看下载状态
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(mReference);
                Cursor cursor = downloadManager.query(query);

                if (cursor == null) {
                    //不存在
                } else {
                    //以下是从游标中进行信息提取
                    cursor.moveToFirst();
                    String msg = statusMessage(cursor);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 查询状态
     *
     * @param c
     * @return
     */
    private String statusMessage(Cursor c) {
        switch (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
            case DownloadManager.STATUS_FAILED:
                return "Download failed";
            case DownloadManager.STATUS_PAUSED:
                return "Download paused";
            case DownloadManager.STATUS_PENDING:
                return "Download pending";
            case DownloadManager.STATUS_RUNNING:
                return "Download in progress!";
            case DownloadManager.STATUS_SUCCESSFUL:
                return "Download finished";
            default:
                return "Unknown Information";
        }
    }

    private void showProgress() {
        mProgressLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        mProgressLayout.setVisibility(View.GONE);
    }
}
