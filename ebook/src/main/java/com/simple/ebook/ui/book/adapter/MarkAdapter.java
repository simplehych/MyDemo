package com.simple.ebook.ui.book.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.simple.ebook.R;
import com.simple.ebook.bean.BookMarkBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author hych
 * @date 2018/7/20 14:57
 */
public class MarkAdapter extends BaseQuickAdapter<BookMarkBean, BaseViewHolder> {


    public MarkAdapter(@Nullable List<BookMarkBean> data) {
        super(R.layout.item_mark, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BookMarkBean item) {
        if (item == null) {
            return;
        }

        TextView markTitle = helper.getView(R.id.item_mark_title);
        TextView markChapterTv = helper.getView(R.id.item_mark_chapter);
        TextView markTime = helper.getView(R.id.item_mark_time);

        String replace = item.getMarkTitle().replace(" ", "");
        markTitle.setText(replace);
        markTime.setText(item.getMarkTime() + "");
        int pagePos = item.getPagePos() + 1;
        String markChapterName = item.getMarkChapterName();
        String chapter = "";
        if (markChapterName.contains("章")) {
            chapter = markChapterName.substring(0, markChapterName.indexOf("章") + 1);
        } else if (markChapterName.contains(" ")) {
            chapter = markChapterName.substring(0, markChapterName.indexOf(" ") + 1);
        } else {
            chapter = markChapterName;
        }
        String markChapter = String.format("%1$s 第%2$s页", chapter, pagePos);
        markChapterTv.setText(markChapter);
        markTime.setText(getTime(item.getMarkTime()));
    }

    private String getTime(long second) {
        Date createDate = new Date(second);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return df.format(createDate);
    }
}
