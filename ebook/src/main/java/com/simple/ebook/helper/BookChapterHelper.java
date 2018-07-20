package com.simple.ebook.helper;

import android.content.Context;

import com.simple.ebook.bean.BookChapterBean;
import com.simple.ebook.bean.BookRecordBean;
import com.simple.ebook.utils.FileUtils;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by Liang_Lu on 2017/12/1.
 * 书籍目录数据库操作
 */

public class BookChapterHelper {
    public String BOOK_CHAPTER_PATH;

    private static volatile BookChapterHelper sInstance;

    public static BookChapterHelper getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (BookChapterHelper.class) {
                if (sInstance == null) {
                    sInstance = new BookChapterHelper(context);
                }
            }
        }
        return sInstance;
    }

    private BookChapterHelper(Context context) {
        BOOK_CHAPTER_PATH = FileUtils.getBookCachePath(context) + File.separator + "chapter" + File.separator;
    }

    /**
     * 保存书籍目录   异步
     *
     * @param bookChapterBeans
     */
    public void saveBookChaptersWithAsync(List<BookChapterBean> bookChapterBeans) {
        if (!(bookChapterBeans != null && bookChapterBeans.size() > 0)) {
            return;
        }
        File dir = new File(BOOK_CHAPTER_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(BOOK_CHAPTER_PATH + File.separator + bookChapterBeans.get(0).getBookId());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        FileUtils.serializeObject(file.getPath(), bookChapterBeans);
    }

    /**
     * 删除书籍目录
     *
     * @param bookId 书籍id
     */
    public void removeBookChapters(String bookId) {
        FileUtils.deleteFile(BOOK_CHAPTER_PATH + File.separator + bookId);
    }

    /**
     * 异步RX查询 书籍目录
     */
    public Observable<List<BookChapterBean>> findBookChaptersInRx(final String bookId) {
        return Observable.create(new ObservableOnSubscribe<List<BookChapterBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<BookChapterBean>> e) throws Exception {
                List<BookChapterBean> bookChapterBeanList = null;
                File file = new File(BOOK_CHAPTER_PATH + File.separator + bookId);
                if (file.exists()) {
                    bookChapterBeanList = (List<BookChapterBean>) FileUtils.unserializeObject(file.getPath());
                    e.onNext(bookChapterBeanList);
                }
            }
        });
    }

}
