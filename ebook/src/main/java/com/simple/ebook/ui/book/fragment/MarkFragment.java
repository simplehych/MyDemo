package com.simple.ebook.ui.book.fragment;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.simple.ebook.ui.book.adapter.MarkAdapter;

/**
 * @author hych
 * @date 2018/7/20 13:44
 */
public class MarkFragment extends BaseListFragment {

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new MarkAdapter(null);
    }
}
