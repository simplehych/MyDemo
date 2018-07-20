package com.simple.ebook.ui.book.bean;

/**
 * 章节信息
 *
 * @author hych
 * @date 2018/7/16 15:03
 */
public class ChapterInfo {
    /**
     * ePub 解析内容示例：
     * <navMap>
     * <navPoint id="chapter_181000246767" playOrder="1"><navLabel><text>楔子 刀剑笑狂沙</text></navLabel><content src="chapter_181000246767.xhtml"/></navPoint>
     * <navPoint id="chapter_181000246768" playOrder="2"><navLabel><text>第一章 任性的温家主人</text></navLabel><content src="chapter_181000246768.xhtml"/></navPoint>
     * <navPoint id="chapter_181000246769" playOrder="3"><navLabel><text>第二章 梦幻火鼎的增殖</text></navLabel><content src="chapter_181000246769.xhtml"/></navPoint>
     * <navPoint id="chapter_181000246770" playOrder="4"><navLabel><text>第三章 日月宝典</text></navLabel><content src="chapter_181000246770.xhtml"/></navPoint>
     * </navMap>
     */

    /**
     * 章节唯一id
     * eg：chapter_181000246770
     */
    private String chapterId;
    /**
     * 章节名称
     * eg：第三章 日月宝典
     */
    private String chapterName;
    /**
     * 章节顺序，ePub解析内容1开始
     * eg：4
     */
    private String chapterPlayOrder;
    /**
     * 章节资源名称
     * eg：chapter_181000246770.xhtml
     */
    private String chapterSrc;

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getChapterPlayOrder() {
        return chapterPlayOrder;
    }

    public void setChapterPlayOrder(String chapterPlayOrder) {
        this.chapterPlayOrder = chapterPlayOrder;
    }

    public String getChapterSrc() {
        return chapterSrc;
    }

    public void setChapterSrc(String chapterSrc) {
        this.chapterSrc = chapterSrc;
    }
}
