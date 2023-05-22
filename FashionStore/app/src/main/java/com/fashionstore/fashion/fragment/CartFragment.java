package com.fashionstore.fashion.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fashionstore.fashion.R;
import com.fashionstore.fashion.databinding.FragmentCartBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.fashionstore.fashion.adapter.CartAdapter;
import com.fashionstore.fashion.database.ControllerApplication;
import com.fashionstore.fashion.database.ProductDataBase;

import com.fashionstore.fashion.listener.ReloadListCartEvent;
import com.fashionstore.fashion.model.Order;
import com.fashionstore.fashion.model.Product;
import com.fashionstore.fashion.utils.Constant;
import com.fashionstore.fashion.utils.GlobalFuntion;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;


public class CartFragment extends Fragment {

    private FragmentCartBinding binding;
    private CartAdapter cartAdapter;
    private List<Product> productList;
    private int mAmount;
    private DatabaseReference userDatabaseReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        displayListInCart();
        binding.tvOrderCart.setOnClickListener(v -> onClickOrderCart());

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        return binding.getRoot();
    }

    private void displayListInCart() {
        if (getActivity() == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.rcvCart.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        binding.rcvCart.addItemDecoration(itemDecoration);
        initDataCart();
    }

    private void initDataCart() {
        productList = ProductDataBase.getInstance(getActivity()).productDAO().list();
        if (productList == null || productList.isEmpty()) {
            return;
        }

        cartAdapter = new CartAdapter(productList, new CartAdapter.ClickItemListener() {
            @Override
            public void clickDelete(Product product, int position) {
                deleteFromCart(product, position);
            }

            @Override
            public void clickUpdate(Product product, int position) {
                ProductDataBase.getInstance(getActivity()).productDAO().update(product);
                cartAdapter.notifyItemChanged(position);

                calculateTotalPrice();
            }
        });
        binding.rcvCart.setAdapter(cartAdapter);

        calculateTotalPrice();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void clearCart() {
        if (productList != null) {
            productList.clear();
        }
        cartAdapter.notifyDataSetChanged();
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        List<Product> listFoodCart = ProductDataBase.getInstance(getActivity()).productDAO().list();
        if (listFoodCart == null || listFoodCart.isEmpty()) {
            String strZero = 0 + "";
            binding.tvTotalPrice.setText(strZero);
            mAmount = 0;
            return;
        }

        int totalPrice = 0;
        for (Product food : listFoodCart) {
            totalPrice = totalPrice + food.getTotalPrice();
        }


        String strTotalPrice = totalPrice + "";
        DecimalFormat formatter = new DecimalFormat("#,### đ");
        String formattedOldPrice = formatter.format(Double.parseDouble(strTotalPrice));
        binding.tvTotalPrice.setText(formattedOldPrice);
        mAmount = totalPrice;
    }

    private void deleteFromCart(Product product, int position) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.confirm_delete))
                .setMessage(getString(R.string.message_delete))
                .setPositiveButton(getString(R.string.delete), (dialog, which) -> {
                    ProductDataBase.getInstance(getActivity()).productDAO().delete(product);
                    productList.remove(position);
                    cartAdapter.notifyItemRemoved(position);

                    calculateTotalPrice();
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReloadListCartEvent event) {
        displayListInCart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    public void onClickOrderCart() {
        if (getActivity() == null) {
            return;
        }
        if (productList == null || productList.isEmpty()) {
            return;
        }
        @SuppressLint("InflateParams") View viewDialog = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_order, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(viewDialog);
        bottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        TextView tvFoodsOrder = viewDialog.findViewById(R.id.tv_foods_order);
        TextView tvPriceOrder = viewDialog.findViewById(R.id.tv_price_order);
        TextView tvCancelOrder = viewDialog.findViewById(R.id.tv_cancel_order);
        TextView tvCreateOrder = viewDialog.findViewById(R.id.tv_create_order);

        tvFoodsOrder.setText(getStringListOrder());
        tvPriceOrder.setText(formatPrice(binding.tvTotalPrice.getText().toString()));
        tvCancelOrder.setOnClickListener(v -> bottomSheetDialog.dismiss());

        tvCreateOrder.setOnClickListener(v -> {
            long id = System.currentTimeMillis();

            Order order = new Order(id, mAmount, getStringListOrder(), Constant.PAYMENT_METHOD_CASH);
            ControllerApplication.get(getActivity()).getBookingDatabaseReference()
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(String.valueOf(id))
                    .setValue(order, (error1, ref1) -> {
                        GlobalFuntion.showToastMessage(getActivity(), getString(R.string.msg_order_success));
                        GlobalFuntion.hideSoftKeyboard(getActivity());
                        bottomSheetDialog.dismiss();
                        ProductDataBase.getInstance(getActivity()).productDAO().deleteAllFood();
                        clearCart();
                    });
        });

        bottomSheetDialog.show();
    }

    private String formatPrice(String price) {
        try {
            double amount = Double.parseDouble(price);
            DecimalFormat formatter = new DecimalFormat("#,### đ");
            return formatter.format(amount);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return price;
        }
    }

    private String getStringListOrder() {
        if (productList == null || productList.isEmpty()) {
            return "";
        }
        StringBuilder resultBuilder = new StringBuilder();
        for (Product product : productList) {
            String result = "- " + product.getName() + " (" + formatPrice(String.valueOf(product.getRealPrice())) + ") "
                    + "- " + " " + product.getCount()
                    + "- " + getString(R.string.color) + " " + product.getSaveColor()
                    + "- " + getString(R.string.size) + " " + product.getSaveSize();
            if (resultBuilder.length() > 0) {
                resultBuilder.append("\n");
            }
            resultBuilder.append(result);
        }
        return resultBuilder.toString();
    }
}
