package com.xingyuyou.xingyuyou.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by 24002 on 2017/5/18.
 */

public class GameDetailListViewAdapter extends BaseAdapter {
    private Activity mActivity;
    private List mList;

    public GameDetailListViewAdapter(Activity activity, List list) {
        mActivity = activity;
        mList = list;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
