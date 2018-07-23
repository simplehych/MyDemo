package com.simple.ebook;

import android.app.Activity;
import android.content.Intent;

import com.simple.ebook.listener.ReadBookListener;

import static com.simple.ebook.BookEntryActivity.BOOK_URL;

/**
 * @author hych
 * @date 2018/7/23 17:30
 */
public class ReadBook {

    private static volatile ReadBook instance;
    private ReadBookListener mReadBookListener;

    private ReadBook() {
    }

    public static ReadBook getInstance() {
        if (instance == null) {
            synchronized (ReadBook.class) {
                if (instance == null) {
                    instance = new ReadBook();
                }
            }
        }
        return instance;
    }

    public void open(Activity activity, String bookUrl, ReadBookListener listener) {
        mReadBookListener = listener;
        Intent intent = new Intent();
        intent.setClass(activity, BookEntryActivity.class);
        intent.putExtra(BOOK_URL, bookUrl);
        activity.startActivity(intent);
    }

    public ReadBookListener getReadBookListener() {
        return mReadBookListener;
    }

}
