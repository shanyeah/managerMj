package com.imovie.mogic.chat.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.imovie.mogic.R;
import com.imovie.mogic.chat.adapter.LocationListAdapter;
import com.imovie.mogic.widget.YSBPageListView;
import com.imovie.mogic.widget.interfaces.IPageList;

import java.util.ArrayList;
import java.util.List;

public class LocationListFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public YSBPageListView lvAccountList;
    public ArrayList<PoiInfo> list;
    public LocationListAdapter adapter;

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    AdapterView.OnItemClickListener onItemClickListener;


    public LocationListFragment() {

    }

    public static LocationListFragment newInstance(String param1, String param2) {
        LocationListFragment fragment = new LocationListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.clock_fragment, container, false);
//        initView(v);
//        setView();
        return v;
    }

    private void initView(View view) {
//        lvAccountList = (YSBPageListView) view.findViewById(R.id.lvClockList);

    }

    private void setView() {
        list = new ArrayList<>();
        adapter = new LocationListAdapter(getContext(),list);
        lvAccountList.setAdapter(adapter);
        lvAccountList.setHaveMoreData(false);
        lvAccountList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (i == SCROLL_STATE_IDLE) {
//
//                    //列表处于最上方
//                    if (absListView.getChildAt(0).getTop() == 0) {
//                        ff_list.setFlexible(true);
//
//                    } else {
//                        ff_list.setFlexible(false);
//                    }
//                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        lvAccountList.setOnPageListener(new IPageList.OnPageListener() {
            @Override
            public void onLoadMoreItems() {
//                getAccountLogList(1,20);
            }
        });

        if (onItemClickListener != null) {
            lvAccountList.setOnItemClickListener(onItemClickListener);
        }

    }

    public void setLocationList(List<PoiInfo> locationList) {
        adapter.list = locationList;
        adapter.notifyDataSetChanged();
    }
    public void chooseItem(int position) {//使某个item 被选中
        adapter.checkPos = position;
        adapter.notifyDataSetChanged();
    }

    public void unchooseItem() { //取消选中
        adapter.checkPos = -1;
        adapter.notifyDataSetChanged();
    }

    public PoiInfo getLocationAtPostion(int postion) {
        try {
            return adapter.list.get(postion);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
