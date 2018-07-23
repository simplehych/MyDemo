package com.simple.ebook.bean;

import java.io.Serializable;

/**
 * @author hych
 * @date 2018/7/23 10:42
 */
public class BookMarkBean implements Serializable {
    /**
     * 所属的书的id
     */
    private String bookId;
    /**
     * 阅读到了第几章
     */
    private int chapterPos;
    /**
     * 当前的页码
     */
    private int pagePos;

    private String markTitle;

    private String markChapterName;

    private long markTime;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public int getChapterPos() {
        return chapterPos;
    }

    public void setChapterPos(int chapterPos) {
        this.chapterPos = chapterPos;
    }

    public int getPagePos() {
        return pagePos;
    }

    public void setPagePos(int pagePos) {
        this.pagePos = pagePos;
    }

    public String getMarkTitle() {
        return markTitle;
    }

    public void setMarkTitle(String markTitle) {
        this.markTitle = markTitle;
    }

    public String getMarkChapterName() {
        return markChapterName;
    }

    public void setMarkChapterName(String markChapterName) {
        this.markChapterName = markChapterName;
    }

    public long getMarkTime() {
        return markTime;
    }

    public void setMarkTime(long markTime) {
        this.markTime = markTime;
    }
}
