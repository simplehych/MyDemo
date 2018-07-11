package com.simple.ebook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.util.Log;
import android.view.View;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;
import com.simple.ebook.api.BookService;
import com.simple.ebook.bean.BookBean;
import com.simple.ebook.bean.BookChapterBean;
import com.simple.ebook.bean.ChapterContentBean;
import com.simple.ebook.ui.ReadActivity;
import com.simple.ebook.utils.FileUtils;
import com.simple.ebook.utils.StringUtils;
import com.simple.ebook.utils.rxhelper.RxObserver;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.MediaType;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.service.MediatypeService;

import static com.simple.ebook.base.BaseViewModel.tokenMap;

public class MainActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private BookBean mBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        findViewById(R.id.activity_test_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookInfo("5abd2636ee91a0c734b748a7");
            }
        });
        findViewById(R.id.activity_test_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpReadActivity();
            }
        });
        findViewById(R.id.activity_test_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void jumpReadActivity() {
        Intent intent = new Intent(MainActivity.this, ReadActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ReadActivity.EXTRA_COLL_BOOK, mBook.getCollBookBean());
        bundle.putBoolean(ReadActivity.EXTRA_IS_COLLECTED, false);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void bookInfo(String bookid) {
        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(BookService.class).bookInfo(bookid)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<BookBean>() {
                    @Override
                    protected void onError(String errorMsg) {
                    }

                    @Override
                    protected void onSuccess(BookBean bookBean) {
                        mBook = bookBean;
                    }
                });
    }
}
