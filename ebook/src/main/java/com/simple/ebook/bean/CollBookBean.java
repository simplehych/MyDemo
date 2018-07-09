package com.simple.ebook.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Liang_Lu on 2017/11/21.
 */
public class CollBookBean implements Serializable {
    private static final long serialVersionUID = 56423411312L;
    private String _id;//如果是本地文件，那么id为所在的地址

    private String title;
    private String author;
    private String shortIntro;
    private String cover;
    private boolean hasCp;
    private int latelyFollower;
    private double retentionRatio;
    //最新更新日期
    private String updated;
    //最新阅读日期
    private String lastRead;
    private int chaptersCount;
    private String lastChapter;
    //是否更新或未阅读
    private boolean isUpdate = true;
    //是否是本地文件
    private boolean isLocal = false;
    private List<BookChapterBean> bookChapterList;

    public CollBookBean(String _id, String title, String author, String shortIntro,
                        String cover, boolean hasCp, int latelyFollower, double retentionRatio,
                        String updated, String lastRead, int chaptersCount, String lastChapter,
                        boolean isUpdate, boolean isLocal) {
        this._id = _id;
        this.title = title;
        this.author = author;
        this.shortIntro = shortIntro;
        this.cover = cover;
        this.hasCp = hasCp;
        this.latelyFollower = latelyFollower;
        this.retentionRatio = retentionRatio;
        this.updated = updated;
        this.lastRead = lastRead;
        this.chaptersCount = chaptersCount;
        this.lastChapter = lastChapter;
        this.isUpdate = isUpdate;
        this.isLocal = isLocal;
    }

    public CollBookBean() {
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    public List<BookChapterBean> getBookChapterList() {
        if (bookChapterList == null) {
//            final DaoSession daoSession = this.daoSession;
//            if (daoSession == null) {
//                throw new DaoException("Entity is detached from DAO context");
//            }
//            BookChapterBeanDao targetDao = daoSession.getBookChapterBeanDao();
//            List<BookChapterBean> bookChapterListNew = targetDao
//                    ._queryCollBookBean_BookChapterList(_id);
//            synchronized (this) {
//                if (bookChapterList == null) {
//                    bookChapterList = bookChapterListNew;
//                }
//            }
        }
        return bookChapterList;
    }

    public void setBookChapterList(List<BookChapterBean> bookChapterList) {
        this.bookChapterList = bookChapterList;
    }


    public void setBookChapters(List<BookChapterBean> beans) {
        bookChapterList = beans;
//        for (BookChapterBean bean : bookChapterList){
//            bean.setBookId(get_id());
//        }
    }

    public List<BookChapterBean> getBookChapters() {
//        if (daoSession == null) {
//            return bookChapterList;
//        } else {
        return getBookChapterList();
//        }
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getShortIntro() {
        return shortIntro;
    }

    public void setShortIntro(String shortIntro) {
        this.shortIntro = shortIntro;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public boolean isHasCp() {
        return hasCp;
    }

    public void setHasCp(boolean hasCp) {
        this.hasCp = hasCp;
    }

    public int getLatelyFollower() {
        return latelyFollower;
    }

    public void setLatelyFollower(int latelyFollower) {
        this.latelyFollower = latelyFollower;
    }

    public double getRetentionRatio() {
        return retentionRatio;
    }

    public void setRetentionRatio(double retentionRatio) {
        this.retentionRatio = retentionRatio;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getLastRead() {
        return lastRead;
    }

    public void setLastRead(String lastRead) {
        this.lastRead = lastRead;
    }

    public int getChaptersCount() {
        return chaptersCount;
    }

    public void setChaptersCount(int chaptersCount) {
        this.chaptersCount = chaptersCount;
    }

    public String getLastChapter() {
        return lastChapter;
    }

    public void setLastChapter(String lastChapter) {
        this.lastChapter = lastChapter;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public boolean getHasCp() {
        return this.hasCp;
    }

    public boolean getIsUpdate() {
        return this.isUpdate;
    }

    public void setIsUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public boolean getIsLocal() {
        return this.isLocal;
    }

    public void setIsLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }

    public synchronized void resetBookChapterList() {
        bookChapterList = null;
    }

    public void delete() {
//        if (myDao == null) {
//            throw new DaoException("Entity is detached from DAO context");
//        }
//        myDao.delete(this);
    }

    public void refresh() {
//        if (myDao == null) {
//            throw new DaoException("Entity is detached from DAO context");
//        }
//        myDao.refresh(this);
    }

    public void update() {
//        if (myDao == null) {
//            throw new DaoException("Entity is detached from DAO context");
//        }
//        myDao.update(this);
    }

//    public void __setDaoSession(DaoSession daoSession) {
//        this.daoSession = daoSession;
//        myDao = daoSession != null ? daoSession.getCollBookBeanDao() : null;
//    }


}
