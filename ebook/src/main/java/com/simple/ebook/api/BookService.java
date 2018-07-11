package com.simple.ebook.api;

import com.allen.library.bean.BaseData;
import com.simple.ebook.bean.BookBean;
import com.simple.ebook.bean.BookChaptersBean;
import com.simple.ebook.bean.ChapterContentBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Liang_Lu on 2017/12/3.
 * 书籍模块接口
 */

public interface BookService {

    /**
     * 获取书籍信息
     *
     * @param bookId
     * @return
     */
    @GET(ModelPath.BOOKS + "/{bookId}")
    Observable<BaseData<BookBean>> bookInfo(@Path("bookId") String bookId);
    /**
     * 获取书籍目录
     *
     * @param bookId
     * @return
     */
    @GET(ModelPath.BOOKS + "/{bookId}/chapters")
    Observable<BaseData<BookChaptersBean>> bookChapters(@Path("bookId") String bookId);

    /**
     * 根据link获取正文
     *
     * @param link 正文链接
     * @return
     */
    @GET("http://chapterup.zhuishushenqi.com/chapter/{link}")
    Observable<ChapterContentBean> bookContent(@Path("link") String link);

}
