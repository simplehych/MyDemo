package com.simple.ebook.ui;

import android.content.Context;

import com.simple.ebook.Interfaces.IBookChapters;
import com.simple.ebook.base.BaseViewModel;
import com.simple.ebook.widget.theme.page.TxtChapter;

import java.util.List;

/**
 * @author hych
 * @date 2018/7/11 14:01
 */
public abstract class BookContentModel extends BaseViewModel {

    IBookChapters iBookChapters;

    public BookContentModel(Context mContext, IBookChapters iBookChapters) {
        super(mContext);
        this.iBookChapters = iBookChapters;
    }

    public abstract void loadChapters(String bookId);

    public abstract void loadContent(String bookId, List<TxtChapter> bookChapterList);
}
