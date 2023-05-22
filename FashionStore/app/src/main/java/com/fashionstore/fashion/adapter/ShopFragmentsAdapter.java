package com.fashionstore.fashion.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.fashionstore.fashion.fragment.ShopFragments.KidsFragment;
import com.fashionstore.fashion.fragment.ShopFragments.MenFragment;
import com.fashionstore.fashion.fragment.ShopFragments.WomenFragment;

public class ShopFragmentsAdapter extends FragmentStatePagerAdapter {
    public ShopFragmentsAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new WomenFragment();
            case 1:
                return new MenFragment();
            case 2:
                return new KidsFragment();


            default:
                return new WomenFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Women";
            case 1:
                return "Men";
            case 2:
                return "Kids";
            default:
                return "Women";
        }
    }
}
