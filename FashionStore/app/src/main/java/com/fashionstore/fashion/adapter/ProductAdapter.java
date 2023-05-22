package com.fashionstore.fashion.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fashionstore.fashion.databinding.ItemLayoutProductBinding;
import com.fashionstore.fashion.listener.ClickItemListener;
import com.fashionstore.fashion.model.Product;
import com.fashionstore.fashion.utils.GlideUtils;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    public ClickItemListener clickItemListener;

    public ProductAdapter(List<Product> productList, ClickItemListener clickItemListener) {
        this.productList = productList;
        this.clickItemListener = clickItemListener;

    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLayoutProductBinding itemLayoutProductBinding = ItemLayoutProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(itemLayoutProductBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        Product product = productList.get(position);
        if (product == null) {
            return;
        }

        GlideUtils.loadUrl(product.getImage(), holder.itemLayoutProductBinding.imgProduct);
        if (product.getSale() <= 0) {
            holder.itemLayoutProductBinding.tvSaleOff.setVisibility(View.GONE);
            holder.itemLayoutProductBinding.tvPrice.setVisibility(View.GONE);

            String strPrice = product.getPrice() + "";
            DecimalFormat formatter = new DecimalFormat("#,### đ");
            String formattedPrice = formatter.format(Double.parseDouble(strPrice));
            holder.itemLayoutProductBinding.tvPriceSale.setText(formattedPrice);
        } else {
            holder.itemLayoutProductBinding.tvSaleOff.setVisibility(View.VISIBLE);
            holder.itemLayoutProductBinding.tvPrice.setVisibility(View.VISIBLE);

            String strSale = "Sale " + product.getSale() + "%";
            holder.itemLayoutProductBinding.tvSaleOff.setText(strSale);

            String strOldPrice = product.getPrice() + "";
            DecimalFormat formatter = new DecimalFormat("#,### đ");
            String formattedOldPrice = formatter.format(Double.parseDouble(strOldPrice));
            holder.itemLayoutProductBinding.tvPrice.setText(formattedOldPrice);
            holder.itemLayoutProductBinding.tvPrice.setPaintFlags(holder.itemLayoutProductBinding.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            String strRealPrice = product.getRealPrice() + "";
            String formattedRealPrice = formatter.format(Double.parseDouble(strRealPrice));
            holder.itemLayoutProductBinding.tvPriceSale.setText(formattedRealPrice);
        }
        holder.itemLayoutProductBinding.tvProductName.setText(product.getName());
        holder.itemLayoutProductBinding.layoutItem.setOnClickListener(v -> clickItemListener.onClickItem(product));


    }

    @Override
    public int getItemCount() {
        return null == productList ? 0 : productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ItemLayoutProductBinding itemLayoutProductBinding;

        public ProductViewHolder(ItemLayoutProductBinding itemLayoutProductBinding) {
            super(itemLayoutProductBinding.getRoot());
            this.itemLayoutProductBinding = itemLayoutProductBinding;
        }
    }
}