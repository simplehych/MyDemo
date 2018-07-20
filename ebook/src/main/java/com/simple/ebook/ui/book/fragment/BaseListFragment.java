package com.simple.ebook.ui.book.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.simple.ebook.R;
import com.simple.ebook.widget.theme.page.TxtChapter;

import java.util.List;

/**
 * @author hych
 * @date 2018/7/20 13:44
 */
public abstract class BaseListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mRecyclerAdapter;
    private OnItemClickListener mOnItemClickListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_list, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fg_list_recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerAdapter = getAdapter();
        if (mRecyclerAdapter != null) {
            mRecyclerView.setAdapter(mRecyclerAdapter);
            mRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position);
                    }
                }
            });
        }
        return view;
    }

    protected abstract BaseQuickAdapter getAdapter();

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setData(List<TxtChapter> data) {
        if (mRecyclerAdapter != null) {
            mRecyclerAdapter.replaceData(data);
            mRecyclerAdapter.notifyDataSetChanged();
        }
    }
}
