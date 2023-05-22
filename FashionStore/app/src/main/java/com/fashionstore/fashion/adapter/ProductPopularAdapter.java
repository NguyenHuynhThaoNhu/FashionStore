package com.fashionstore.fashion.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fashionstore.fashion.databinding.ItemProductPopularBinding;
import com.fashionstore.fashion.listener.ClickItemListener;
import com.fashionstore.fashion.model.Product;
import com.fashionstore.fashion.utils.GlideUtils;

import java.util.List;

public class ProductPopularAdapter extends RecyclerView.Adapter<ProductPopularAdapter.FoodPopularViewHolder> {

    private final List<Product> productList;
    public final ClickItemListener iOnClickFoodItemListener;

    public ProductPopularAdapter(List<Product> mListFoods, ClickItemListener iOnClickFoodItemListener) {
        this.productList = mListFoods;
        this.iOnClickFoodItemListener = iOnClickFoodItemListener;
    }

    @NonNull
    @Override
    public FoodPopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductPopularBinding itemProductPopularBinding = ItemProductPopularBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FoodPopularViewHolder(itemProductPopularBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodPopularViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product == null) {
            return;
        }
        GlideUtils.loadUrlBanner(product.getBanner(), holder.itemProductPopularBinding.imageFood);
        if (product.getSale() <= 0) {
            holder.itemProductPopularBinding.tvSaleOff.setVisibility(View.GONE);
        } else {
            holder.itemProductPopularBinding.tvSaleOff.setVisibility(View.VISIBLE);
            String strSale = "Giảm " + product.getSale() + "%";
            holder.itemProductPopularBinding.tvSaleOff.setText(strSale);
        }
        holder.itemProductPopularBinding.layoutItem.setOnClickListener(v -> iOnClickFoodItemListener.onClickItem(product));
    }

    @Override
    public int getItemCount() {
        if (productList != null) {
            return productList.size();
        }
        return 0;
    }

    public static class FoodPopularViewHolder extends RecyclerView.ViewHolder {

        private final ItemProductPopularBinding itemProductPopularBinding;

        public FoodPopularViewHolder(@NonNull ItemProductPopularBinding itemProductPopularBinding) {
            super(itemProductPopularBinding.getRoot());
            this.itemProductPopularBinding = itemProductPopularBinding;
        }
    }
}
