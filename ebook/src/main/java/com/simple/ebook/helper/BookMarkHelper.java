package com.simple.ebook.helper;

import android.content.Context;

import com.simple.ebook.bean.BookMarkBean;
import com.simple.ebook.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.domain.Book;

/**
 * @author hych
 * @date 2018/7/23 10:46
 */
public class BookMarkHelper {


    private static volatile BookMarkHelper instance;
    private static String BOOK_MARK_PATH;

    private BookMarkHelper(Context context) {
        BOOK_MARK_PATH = FileUtils.getBookCachePath(context) + File.separator + "mark";
    }

    public static BookMarkHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (BookMarkHelper.class) {
                if (instance == null) {
                    instance = new BookMarkHelper(context);
                }
            }
        }
        return instance;
    }

    public void saveMarks(List<BookMarkBean> markList) {
        if (!(markList != null && markList.size() > 0)) {
            return;
        }
        File dir = new File(BOOK_MARK_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(BOOK_MARK_PATH + File.separator + markList.get(0).getBookId());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        FileUtils.serializeObject(file.getPath(), markList);
    }

    public List<BookMarkBean> getMarks(String bookId) {
        List<BookMarkBean> markList = (List<BookMarkBean>) FileUtils.unserializeObject(BOOK_MARK_PATH + File.separator + bookId);
        if (markList == null) {
            markList = new ArrayList<>();
        }
        return markList;
    }
}
