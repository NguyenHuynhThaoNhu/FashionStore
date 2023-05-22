package com.fashionstore.fashion.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.fashionstore.fashion.R;
import com.fashionstore.fashion.adapter.ShopFragmentsAdapter;
import com.fashionstore.fashion.widget.DisableViewPager;


public class ShopFragment extends Fragment {


    private TabLayout tabLayout_shop;
    private DisableViewPager vpg_shop_fm;

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_shop, container, false);

        tabLayout_shop = mView.findViewById(R.id.tabLayout_shop);
        vpg_shop_fm = mView.findViewById(R.id.vpg_shop_fm);

        ShopFragmentsAdapter shopFragmentsAdapter = new ShopFragmentsAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpg_shop_fm.setAdapter(shopFragmentsAdapter);
        vpg_shop_fm.setViewPagerEnabled(false);

        tabLayout_shop.setupWithViewPager(vpg_shop_fm);

        return mView;

    }
}