package com.simple.ebook.ui.book.fragment;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.simple.ebook.ui.book.adapter.CatalogAdapter;
import com.simple.ebook.widget.theme.page.TxtChapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hych
 * @date 2018/7/20 13:44
 */
public class CatalogFragment extends BaseListFragment {

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new CatalogAdapter(null);
    }
}
