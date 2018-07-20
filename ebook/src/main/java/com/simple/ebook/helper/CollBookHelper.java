package com.simple.ebook.helper;


import android.content.Context;

import com.simple.ebook.bean.CollBookBean;
import com.simple.ebook.utils.FileUtils;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by Liang_Lu on 2017/12/1.
 */

public class CollBookHelper {
    public static String BOOK_COLLECTION_PATH ;

    private static volatile CollBookHelper sInstance;

    public static CollBookHelper getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (CollBookHelper.class) {
                if (sInstance == null) {
                    sInstance = new CollBookHelper(context);
                }
            }
        }
        return sInstance;
    }

    public CollBookHelper(Context context) {
        BOOK_COLLECTION_PATH = FileUtils.getBookCachePath(context) + File.separator + "collect" + File.separator;
    }

    /**
     * 保存一本书籍 同步
     *
     * @param collBookBean
     */
    public void saveBook(CollBookBean collBookBean) {
        if (collBookBean == null) {
            return;
        }
        File dir = new File(BOOK_COLLECTION_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(BOOK_COLLECTION_PATH + File.separator + collBookBean.get_id());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        FileUtils.serializeObject(file.getPath(), collBookBean);
    }

    /**
     * 获取书籍
     *
     * @param bookId
     */
    public CollBookBean getBook(String bookId) {
        CollBookBean collBookBean =  null;
        File file = new File(BOOK_COLLECTION_PATH + File.separator + bookId);
        if (file.exists()) {
            collBookBean = (CollBookBean) FileUtils.unserializeObject(file.getPath());
        }
        return collBookBean;
    }



}
