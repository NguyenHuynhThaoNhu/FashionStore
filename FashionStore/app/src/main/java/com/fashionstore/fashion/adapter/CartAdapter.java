package com.fashionstore.fashion.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fashionstore.fashion.databinding.ItemLayoutCartBinding;
import com.fashionstore.fashion.model.Product;
import com.fashionstore.fashion.utils.LoadImageProduct;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Product> productList;
    private ClickItemListener clickItemListener;

    public interface ClickItemListener {
        void clickDelete(Product product, int position);

        void clickUpdate(Product product, int position);
    }

    public CartAdapter(List<Product> productList, ClickItemListener clickItemListener) {
        this.productList = productList;
        this.clickItemListener = clickItemListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLayoutCartBinding itemLayoutCartBinding = ItemLayoutCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(itemLayoutCartBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product == null) {
            return;
        }
        LoadImageProduct.loadUrl(product.getImage(), holder.binding.imgProductCart);
        holder.binding.tvProductNameCart.setText(product.getName());

        DecimalFormat formatter = new DecimalFormat("#,###đ");



        holder.binding.tvProductToppingCart.setText("Topping: " + String.valueOf((product.getSaveColor())));
        holder.binding.tvProductSizeCart.setText("Size: " + String.valueOf(product.getSaveSize()));



        int Price =  (product.getTotalPrice());
        String formattedOldPrice = formatter.format(Price);
        holder.binding.tvProductPriceCart.setText(String.valueOf(formattedOldPrice + "  X" + "("+ String.valueOf(product.getCount())) + ")") ;







        holder.binding.tvDelete.setOnClickListener(v
                -> clickItemListener.clickDelete(product, holder.getAdapterPosition()));


    }

    @Override
    public int getItemCount() {
        return null == productList ? 0 : productList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private ItemLayoutCartBinding binding;
        public CartViewHolder(ItemLayoutCartBinding itemLayoutCartBinding) {
            super(itemLayoutCartBinding.getRoot());
            this.binding = itemLayoutCartBinding;
        }
    }
}