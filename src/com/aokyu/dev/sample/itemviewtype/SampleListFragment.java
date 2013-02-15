/*
 * Copyright 2013 Yu AOKI
 */

package com.aokyu.dev.sample.itemviewtype;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SampleListFragment extends Fragment {

    /* package */ static final String TAG = SampleListFragment.class.getSimpleName();

    private Context mContext;

    private ListView mListView;
    private SampleAdapter mAdapter;

    private List<String> mItems = new ArrayList<String>();

    public SampleListFragment() {
        for (int i = 0; i < 100; i++) {
            mItems.add(String.valueOf(i));
        }
    }

    public static SampleListFragment newInstance() {
        SampleListFragment fragment = new SampleListFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity != null) {
            mContext = activity.getApplicationContext();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.list_panel, null);
        setupViews(contentView);
        return contentView;
    }

    private void setupViews(View rootView) {
        mListView = (ListView) rootView.findViewById(R.id.list_view);
        mAdapter = new SampleAdapter(mContext);
        mAdapter.set(mItems);
        mListView.setAdapter(mAdapter);
    }

    private static class SampleAdapter extends BaseAdapter {

        private static final class ViewType {
            public static final int NOMAL = 0;
            public static final int COLOR = 1;
            public static final int LARGE = 2;

            public static int size() {
                return 3;
            }

            private ViewType() {}
        }

        private interface ViewCache<Data> {
            public void bindView(Data data);
        }

        private static final class NormalViewCache implements ViewCache<String> {
            public final TextView textView;

            public NormalViewCache(View rootView) {
                textView = (TextView) rootView.findViewById(R.id.text_view);
            }

            @Override
            public void bindView(String data) {
                textView.setText(data);
            }
        }

        private static final class ColorViewCache implements ViewCache<String> {
            public final TextView textView;

            public ColorViewCache(View rootView) {
                textView = (TextView) rootView.findViewById(R.id.color_text_view);
            }

            @Override
            public void bindView(String data) {
                textView.setText(data);
            }
        }

        private static final class LargeViewCache implements ViewCache<String> {
            public final TextView textView;

            public LargeViewCache(View rootView) {
                textView = (TextView) rootView.findViewById(R.id.large_text_view);
            }

            @Override
            public void bindView(String data) {
                textView.setText(data);
            }
        }

        private LayoutInflater mInflater;
        private List<String> mItems;

        public SampleAdapter(Context context) {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        /* package */ void set(List<String> items) {
            if (items == null) {
                mItems = null;
                notifyDataSetChanged();
                return;
            }

            if (mItems != null) {
                mItems.clear();
                mItems.addAll(items);
            } else {
                mItems = items;
            }
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return position % ViewType.size();
        }

        @Override
        public int getViewTypeCount() {
            return ViewType.size();
        }

        @Override
        public int getCount() {
            if (mItems != null) {
                int size = mItems.size();
                return size;
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            if (mItems != null) {
                String item = mItems.get(position);
                return item;
            } else {
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = newView(position);
            }
            ViewCache cache = (ViewCache) view.getTag();
            String data = mItems.get(position);
            cache.bindView(data);

            return view;
        }

        private View newView(int position) {
            int viewType = getItemViewType(position);
            View view = null;
            ViewCache cache = null;
            switch (viewType) {
            case ViewType.NOMAL:
                view = mInflater.inflate(R.layout.normal_list_item, null);
                cache = new NormalViewCache(view);
                break;
            case ViewType.COLOR:
                view = mInflater.inflate(R.layout.color_list_item, null);
                cache = new ColorViewCache(view);
                break;
            case ViewType.LARGE:
                view = mInflater.inflate(R.layout.large_list_item, null);
                cache = new LargeViewCache(view);
                break;
            default:
                view = mInflater.inflate(R.layout.normal_list_item, null);
                cache = new NormalViewCache(view);
                break;
            }
            view.setTag(cache);
            return view;
        }
    }
}
