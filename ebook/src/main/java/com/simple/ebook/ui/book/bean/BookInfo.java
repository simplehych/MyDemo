package com.simple.ebook.ui.book.bean;

/**
 * 书籍信息
 *
 * @author hych
 * @date 2018/7/16 15:02
 */
public class BookInfo {

    /**
     * 书籍唯一id
     */
    private String bookId;
    /**
     * 书籍名称
     */
    private String bookName;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
}
