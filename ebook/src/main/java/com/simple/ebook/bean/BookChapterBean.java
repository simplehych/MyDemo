package com.simple.ebook.bean;

import java.io.Serializable;

import nl.siegmann.epublib.domain.Resource;

/**
 * Created by Liang_Lu on 2017/11/21.
 */
public class BookChapterBean implements Serializable {
    private static final long serialVersionUID = 56423411313L;
    /**
     * title : 第一章 他叫白小纯
     * link : http://read.qidian.com/chapter/rJgN8tJ_cVdRGoWu-UQg7Q2/6jr-buLIUJSaGfXRMrUjdw2
     * unreadble : false
     */
    //链接是唯一的
    private String link;

    private String title;

    //所属的下载任务
    private String taskName;
    //所属的书籍
    private String bookId;

    private boolean unreadble;

    private Resource resource;

    public BookChapterBean(Resource resource) {
        this.resource = resource;
        this.bookId = resource.getId();
        this.title = resource.getTitle();
        this.link = resource.getHref();
    }

    public BookChapterBean(String link, String title, String taskName, String bookId,
                           boolean unreadble) {
        this.link = link;
        this.title = title;
        this.taskName = taskName;
        this.bookId = bookId;
        this.unreadble = unreadble;
    }

    public BookChapterBean() {
    }


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public boolean isUnreadble() {
        return unreadble;
    }

    public void setUnreadble(boolean unreadble) {
        this.unreadble = unreadble;
    }

    public boolean getUnreadble() {
        return this.unreadble;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "BookChapterBean{" +
                "link='" + link + '\'' +
                ", title='" + title + '\'' +
                ", taskName='" + taskName + '\'' +
                ", bookId='" + bookId + '\'' +
                ", unreadble=" + unreadble +
                ", resource=" + resource +
                '}';
    }
}
