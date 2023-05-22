package com.fashionstore.fashion.fragment.ShopFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.fashionstore.fashion.R;
import com.fashionstore.fashion.activity.ProductDetailActivity;
import com.fashionstore.fashion.adapter.ProductAdapter;
import com.fashionstore.fashion.database.ControllerApplication;
import com.fashionstore.fashion.databinding.FragmentKidsBinding;
import com.fashionstore.fashion.model.Product;
import com.fashionstore.fashion.utils.Constant;
import com.fashionstore.fashion.utils.GlobalFuntion;
import com.fashionstore.fashion.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;


public class KidsFragment extends Fragment {

    private FragmentKidsBinding fragmentShopBinding;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentShopBinding = com.fashionstore.fashion.databinding.FragmentKidsBinding.inflate(inflater, container, false);

        recyclerView = fragmentShopBinding.rcvProduct;

        // set up the adapter and layout manager
        loadDataFromApi("");

        return fragmentShopBinding.getRoot();
    }



    private void displayListFoodSuggest() {


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
//        fragmentShopBinding.rcvProduct.setLayoutManager(gridLayoutManager);


        productAdapter = new ProductAdapter(productList, this::goToProductDetail);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(productAdapter);

    }

    private void loadDataFromApi(String key) {
        if (getActivity() == null) {
            return;
        }
        ControllerApplication.get(getActivity().getApplication()).getKidsDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                productList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product == null) {
                        return;
                    }

                    if (StringUtil.isEmpty(key)) {
                        productList.add(0, product);
                    } else {
                        if (GlobalFuntion.getTextSearch(product.getName()).toLowerCase().trim()
                                .contains(GlobalFuntion.getTextSearch(key).toLowerCase().trim())) {
                            productList.add(0, product);
                        }
                    }
                }
                displayListFoodSuggest();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                GlobalFuntion.showToastMessage(getActivity(), getString(R.string.msg_get_date_error));
            }
        });
    }


    private void goToProductDetail(@NonNull Product product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_OBJECT, product);
        startActivity(requireContext(), ProductDetailActivity.class, bundle);
    }
    public static void startActivity(Context context, Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(context, clz);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}