package com.fashionstore.fashion.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fashionstore.fashion.R;
import com.fashionstore.fashion.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import com.fashionstore.fashion.activity.ProductDetailActivity;
import com.fashionstore.fashion.adapter.ProductAdapter;
import com.fashionstore.fashion.adapter.ProductPopularAdapter;
import com.fashionstore.fashion.database.ControllerApplication;

import com.fashionstore.fashion.model.Product;
import com.fashionstore.fashion.utils.Constant;
import com.fashionstore.fashion.utils.GlobalFuntion;
import com.fashionstore.fashion.utils.StringUtil;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {


    private FragmentHomeBinding binding;

    private List<Product> mListFood;
    private List<Product> mListFoodPopular;
    private RecyclerView rcv_Women,rcv_Men,rcv_Kids;
    private ProductAdapter productAdapter;
    private ProductPopularAdapter productPopularAdapter;
    private final Handler mHandlerBanner = new Handler();
    private final Runnable mRunnableBanner = new Runnable() {
        @Override
        public void run() {
            if (mListFoodPopular == null || mListFoodPopular.isEmpty()) {
                return;
            }
            if (binding.viewpager2.getCurrentItem() == mListFoodPopular.size() - 1) {
                binding.viewpager2.setCurrentItem(0);
                return;
            }
            binding.viewpager2.setCurrentItem(binding.viewpager2.getCurrentItem() + 1);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        getListFoodFromFirebase("");

        rcv_Women = binding.rcvWomen;
        rcv_Men = binding.rcvMen;
        rcv_Kids = binding.rcvKids;
        loadDataWomenFromApi("");
        loadDataMenFromApi("");
        loadDataKidFromApi("");
        return binding.getRoot();
    }

    private void getListFoodFromFirebase(String key) {
        if (getActivity() == null) {
            return;
        }
        ControllerApplication.get(getActivity()).getWomenDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.layoutContent.setVisibility(View.VISIBLE);
                mListFood = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product food = dataSnapshot.getValue(Product.class);
                    if (food == null) {
                        return;
                    }

                    if (StringUtil.isEmpty(key)) {
                        mListFood.add(0, food);
                    } else {
                        if (GlobalFuntion.getTextSearch(food.getName()).toLowerCase().trim()
                                .contains(GlobalFuntion.getTextSearch(key).toLowerCase().trim())) {
                            mListFood.add(0, food);
                        }
                    }
                }
                displayListFoodPopular();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                GlobalFuntion.showToastMessage(getActivity(), getString(R.string.msg_get_date_error));
            }
        });
    }




    private void displayListFoodPopular() {
        productPopularAdapter = new ProductPopularAdapter(getListFoodPopular(), this::goToProductDetail);
        binding.viewpager2.setAdapter(productPopularAdapter);
        binding.indicator3.setViewPager(binding.viewpager2);

        binding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mHandlerBanner.removeCallbacks(mRunnableBanner);
                mHandlerBanner.postDelayed(mRunnableBanner, 3000);
            }
        });
    }

    private List<Product> getListFoodPopular() {
        mListFoodPopular = new ArrayList<>();
        if (mListFood == null || mListFood.isEmpty()) {
            return mListFoodPopular;
        }
        for (Product food : mListFood) {
            if (food.isPopular()) {
                mListFoodPopular.add(food);
            }
        }
        return mListFoodPopular;
    }


    private void displayListWomenSuggest() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        productAdapter = new ProductAdapter(mListFood, this::goToProductDetail);
        rcv_Women.setLayoutManager(gridLayoutManager);
        rcv_Women.setAdapter(productAdapter);
    }
    private void displayListMenSuggest() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        productAdapter = new ProductAdapter(mListFood, this::goToProductDetail);
        rcv_Men.setLayoutManager(gridLayoutManager);
        rcv_Men.setAdapter(productAdapter);
    }
    private void displayListKidsSuggest() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
//        fragmentShopBinding.rcvProduct.setLayoutManager(gridLayoutManager);


        productAdapter = new ProductAdapter(mListFood, this::goToProductDetail);
        rcv_Kids.setLayoutManager(gridLayoutManager);
        rcv_Kids.setAdapter(productAdapter);
    }

    private void loadDataWomenFromApi(String key) {
        if (getActivity() == null) {
            return;
        }
        mListFood = new ArrayList<>(); // initialize productList
        ControllerApplication.get(getActivity().getApplication()).getWomenDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> allProducts = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product != null) {
                        allProducts.add(product);
                    }
                }
                if (!allProducts.isEmpty()) {
                    int randomCount = 2 + (int) (Math.random() * 3);
                    Collections.shuffle(allProducts);
                    List<Product> limitedProducts = allProducts.subList(0, Math.min(allProducts.size(), randomCount));

                    mListFood.clear();
                    mListFood.addAll(limitedProducts);
                    displayListWomenSuggest();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                GlobalFuntion.showToastMessage(getActivity(), getString(R.string.msg_get_date_error));
            }
        });
    }


    private void loadDataMenFromApi(String key) {
        if (getActivity() == null) {
            return;
        }
        mListFood = new ArrayList<>(); // initialize productList
        ControllerApplication.get(getActivity().getApplication()).getMenDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> allProducts = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product != null) {
                        allProducts.add(product);
                    }
                }
                if (!allProducts.isEmpty()) {
                    int randomCount = 2 + (int) (Math.random() * 3);
                    Collections.shuffle(allProducts);
                    List<Product> limitedProducts = allProducts.subList(0, Math.min(allProducts.size(), randomCount));

                    mListFood.clear();
                    mListFood.addAll(limitedProducts);
                    displayListMenSuggest();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                GlobalFuntion.showToastMessage(getActivity(), getString(R.string.msg_get_date_error));
            }
        });
    }

    private void loadDataKidFromApi(String key) {
        if (getActivity() == null) {
            return;
        }
        mListFood = new ArrayList<>(); // initialize productList
        ControllerApplication.get(getActivity().getApplication()).getKidsDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> allProducts = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product != null) {
                        allProducts.add(product);
                    }
                }
                if (!allProducts.isEmpty()) {
                    int randomCount = 2 + (int) (Math.random() * 3);
                    Collections.shuffle(allProducts);
                    List<Product> limitedProducts = allProducts.subList(0, Math.min(allProducts.size(), randomCount));

                    mListFood.clear();
                    mListFood.addAll(limitedProducts);
                    displayListKidsSuggest();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                GlobalFuntion.showToastMessage(getActivity(), getString(R.string.msg_get_date_error));
            }
        });
    }




    private void goToProductDetail(@NonNull Product food) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_OBJECT, food);
        GlobalFuntion.startActivity(getActivity(), ProductDetailActivity.class, bundle);
    }
}