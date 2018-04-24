package com.imovie.mogic.home.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.imovie.mogic.R;
import com.imovie.mogic.myRank.fragment.ChargeRecordFragment;
import com.imovie.mogic.myRank.fragment.OrderFragment;
import com.imovie.mogic.myRank.fragment.OrderRecordFragment;
import com.imovie.mogic.myRank.fragment.MovieFragment;
import com.imovie.mogic.myRank.widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DiscoveryFragmentOld extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private PagerSlidingTabStrip pstTabTitle;
    private ViewPager mViewPager;
    private final List<String> titles = Arrays.asList("电影","活动","兑换","任务");
    private List<Fragment> mFragments = new ArrayList<>();
    public FragmentPagerAdapter adapter;


    public DiscoveryFragmentOld() {

    }

    public static DiscoveryFragmentOld newInstance(String param1, String param2) {
        DiscoveryFragmentOld fragment = new DiscoveryFragmentOld();
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
        View v=inflater.inflate(R.layout.fragment_discovery_old, container, false);
        pstTabTitle = (PagerSlidingTabStrip)v.findViewById(R.id.pst_myorder_tabTitle);
        mViewPager = (ViewPager)v.findViewById(R.id.vPager);
        initView();
        return v;
    }


    private void fixUI() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        LinearLayout.LayoutParams tabtitleParams = (LinearLayout.LayoutParams) pstTabTitle.getLayoutParams();
        tabtitleParams.height = 90 * screenWidth/640;
        pstTabTitle.setLayoutParams(tabtitleParams);
        pstTabTitle.setTextSize(16);
    }

    private void initView() {
        fixUI();
//        viewHolder.flexibleFrameLayout.setFlexView(viewHolder.llAddress);
//        viewHolder.flexibleFrameLayout.setFlexible(true);

        mFragments.add(new MovieFragment());
        mFragments.add(new OrderFragment());
        mFragments.add(new ChargeRecordFragment());
        mFragments.add(new OrderRecordFragment());
        adapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int i) {
                return mFragments.get(i);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public int getItemPosition(Object object) {
                return PagerAdapter.POSITION_NONE;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                return super.instantiateItem(container, position);
            }
        };
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(4);
        pstTabTitle.setViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
    }
}
