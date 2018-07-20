package com.simple.ebook.helper;

import android.content.Context;

import com.simple.ebook.bean.BookRecordBean;
import com.simple.ebook.utils.FileUtils;

import java.io.File;

/**
 * Created by Liang_Lu on 2017/12/1.
 * 阅读记录数据库工具类
 */

public class BookRecordHelper {
    public static String BOOK_RECORD_PATH ;
    private static volatile BookRecordHelper sInstance;

    public static BookRecordHelper getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (BookRecordHelper.class) {
                if (sInstance == null) {
                    sInstance = new BookRecordHelper(context);
                }
            }
        }
        return sInstance;
    }

    private BookRecordHelper(Context context) {
        BOOK_RECORD_PATH = FileUtils.getBookCachePath(context) + File.separator + "record" + File.separator;
    }

    /**
     * 保存阅读记录
     */
    public void saveRecordBook(BookRecordBean bookRecordBean) {
        if (bookRecordBean == null) {
            return;
        }
        File dir = new File(BOOK_RECORD_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(BOOK_RECORD_PATH + File.separator + bookRecordBean.getBookId());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        FileUtils.serializeObject(file.getPath(), bookRecordBean);
    }

    /**
     * 删除书籍记录
     */
    public void removeBook(String bookId) {
        FileUtils.deleteFile(BOOK_RECORD_PATH + File.separator + bookId);
    }


    /**
     * 查询阅读记录
     */
    public BookRecordBean findBookRecordById(String bookId) {
        File file = new File(BOOK_RECORD_PATH + File.separator + bookId);
        if (!file.exists()) {
            return null;
        } else {
            return (BookRecordBean) FileUtils.unserializeObject(file.getPath());
        }
    }
}
