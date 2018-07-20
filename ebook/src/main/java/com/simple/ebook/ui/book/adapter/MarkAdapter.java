package com.simple.ebook.ui.book.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.simple.ebook.R;
import com.simple.ebook.widget.theme.page.TxtChapter;

import java.util.List;

/**
 * @author hych
 * @date 2018/7/20 14:57
 */
public class MarkAdapter extends BaseQuickAdapter<TxtChapter, BaseViewHolder> {


    public MarkAdapter(@Nullable List<TxtChapter> data) {
        super(R.layout.item_catalog, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TxtChapter item) {
        //首先判断是否该章已下载
        Drawable drawable = null;

        //TODO:目录显示设计的有点不好，需要靠成员变量是否为null来判断。
        //如果没有链接地址表示是本地文件
        if (item.getLink() == null) {
            drawable = ContextCompat.getDrawable(mContext, R.drawable.selector_category_load);
        } else {
            if (item.getBookId() != null) {
                drawable = ContextCompat.getDrawable(mContext, R.drawable.selector_category_load);
            } else {
                drawable = ContextCompat.getDrawable(mContext, R.drawable.selector_category_unload);
            }
        }

        TextView category_tv_chapter = helper.getView(R.id.category_tv_chapter);
        category_tv_chapter.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        category_tv_chapter.setSelected(item.isSelect());
        category_tv_chapter.setText(item.getTitle());
        if (item.isSelect()) {
            category_tv_chapter.setTextColor(ContextCompat.getColor(mContext, R.color.color_ec4a48));
        } else {
            category_tv_chapter.setTextColor(ContextCompat.getColor(mContext, R.color.black));
        }

    }
}
