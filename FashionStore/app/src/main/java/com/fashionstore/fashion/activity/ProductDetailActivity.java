package com.fashionstore.fashion.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.fashionstore.fashion.R;
import com.fashionstore.fashion.adapter.MoreImageAdapter;
import com.fashionstore.fashion.database.ProductDataBase;
import com.fashionstore.fashion.databinding.ActivityProductDetailBinding;
import com.fashionstore.fashion.listener.ReloadListCartEvent;
import com.fashionstore.fashion.model.Product;
import com.fashionstore.fashion.utils.Constant;
import com.fashionstore.fashion.utils.GlideUtils;
import com.fashionstore.fashion.utils.LoadImageProduct;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {
    private ActivityProductDetailBinding binding;
    private Product product;
    private ImageView btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getDataIntent();
        setDataFoodDetail();
        initListener();
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            product = (Product) bundle.get(Constant.KEY_INTENT_OBJECT);
        }
    }



    private void setDataFoodDetail() {
        if (product == null) {
            return;
        }



        LoadImageProduct.loadUrl(product.getImage(), binding.imageFood);
        if (product.getSale() <= 0) {
            binding.tvSaleOff.setVisibility(View.GONE);
            binding.tvPrice.setVisibility(View.GONE);

            String strPrice = product.getPrice() + "";
            DecimalFormat formatter = new DecimalFormat("#,### đ");
            String formattedOldPrice = formatter.format(Double.parseDouble(strPrice));
            binding.tvPriceSale.setText(formattedOldPrice);
        } else {
            binding.tvSaleOff.setVisibility(View.VISIBLE);
            binding.tvPrice.setVisibility(View.VISIBLE);

            String strSale = "Giảm " + product.getSale() + "%";
            binding.tvSaleOff.setText(strSale);

            String strPriceOld = product.getPrice() + "";
            String strRealPrice = product.getRealPrice() + "";
            DecimalFormat formatter = new DecimalFormat("#,### đ");
            String formattedOldPrice = formatter.format(Double.parseDouble(strPriceOld));
            binding.tvPrice.setText(formattedOldPrice);
            binding.tvPrice.setPaintFlags(binding.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            String formattedRealPrice = formatter.format(Double.parseDouble(strRealPrice));
            binding.tvPriceSale.setText(formattedRealPrice);
        }
        binding.tvFoodName.setText(product.getName());
        binding.tvFoodDescription.setText(product.getDescription());

        displayListMoreImages();

        setStatusButtonAddToCart();
    }
    private void displayListMoreImages() {
        if (product.getImages() == null || product.getImages().isEmpty()) {
            binding.tvMoreImageLabel.setVisibility(View.GONE);
            return;
        }
        binding.tvMoreImageLabel.setVisibility(View.VISIBLE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.rcvImages.setLayoutManager(gridLayoutManager);

        MoreImageAdapter moreImageAdapter = new MoreImageAdapter(product.getImages());
        binding.rcvImages.setAdapter(moreImageAdapter);
    }
    private boolean isProductInCart() {
        List<Product> list = ProductDataBase.getInstance(this).productDAO().checkProductInCart(product.getId());
        return list != null && !list.isEmpty();
    }

    private void initListener() {
        binding.tvAddToCart.setOnClickListener(v -> onClickAddToCart());


    }
    private void setStatusButtonAddToCart() {
        if (isProductInCart()) {
            binding.tvAddToCart.setBackgroundResource(R.drawable.bg_disable_shape_corner_6);
            binding.tvAddToCart.setText(getString(R.string.added_to_cart));
            binding.tvAddToCart.setTextColor(ContextCompat.getColor(this, R.color.textColorPrimary));

        } else {
            binding.tvAddToCart.setBackgroundResource(R.drawable.bg_green_shape_corner_6);
            binding.tvAddToCart.setText(getString(R.string.add_to_cart));
            binding.tvAddToCart.setTextColor(ContextCompat.getColor(this, R.color.white));
        }
    }

    public void onClickAddToCart() {
        if (isProductInCart()) {
            return;
        }

        @SuppressLint("InflateParams") View viewDialog = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_cart, null);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(viewDialog);

        ImageView imgCart = viewDialog.findViewById(R.id.img_product_cart);
        TextView tvNameCart = viewDialog.findViewById(R.id.tv_product_name_cart);
        TextView tvPriceCart = viewDialog.findViewById(R.id.tv_product_price_cart);
        TextView tvSubtractCount = viewDialog.findViewById(R.id.tv_product_price_cart);
        RadioGroup rg_size = viewDialog.findViewById(R.id.rg_size);
        RadioGroup rg_topping = viewDialog.findViewById(R.id.rg_color);


        TextView tvCancel = viewDialog.findViewById(R.id.tv_cancel);
        TextView tvAddCart = viewDialog.findViewById(R.id.tv_add_cart);

        GlideUtils.loadUrl(product.getImage(), imgCart);
        tvNameCart.setText(product.getName());



        //        check size have null

        if (product.getSize() == null || product.getSize().isEmpty()) {
            viewDialog.findViewById(R.id.vt_size).setVisibility(View.GONE);
        } else {
            for (int i = 0; i < product.getSize().size(); i++) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(product.getSize().get(i));
                radioButton.setTag(i);
                rg_size.addView(radioButton);
                if (i == 0) {
                    radioButton.setChecked(true);
                }
            }
        }

//        check topping have null

        if (product.getColor() == null || product.getColor().isEmpty()) {
            viewDialog.findViewById(R.id.tv_topping).setVisibility(View.GONE);

        } else {
            for (int i = 0; i < product.getColor().size(); i++) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(product.getColor().get(i));
                radioButton.setTag(i);
                rg_topping.addView(radioButton);
                if (i == 0) {
                    radioButton.setChecked(true);
                }
            }
        }


        //        total default

        int totalPrice = product.getRealPrice();
        String strTotalPrice = totalPrice + "";
        DecimalFormat formatter = new DecimalFormat("#,### đ");
        String formattedOldPrice = formatter.format(Double.parseDouble(strTotalPrice));
        tvPriceCart.setText(formattedOldPrice);
        product.setTotalPrice(totalPrice);


//        logic
        rg_size.setOnCheckedChangeListener((group, checkedId) -> {
            int newPrice = product.getRealPrice();
            int sizeOption = (int) rg_size.findViewById(checkedId).getTag();
            if (sizeOption >= 0) {
                String selectedSize = product.getSize().get(sizeOption);
                product.setSaveSize(selectedSize);
            }




            int newTotalPrice = newPrice;



            int totalPrice1 = newTotalPrice;
            product.setTotalPrice(totalPrice1);
            String strTotalPrice1 = totalPrice1 + "";
            String formattedNewPrice = formatter.format(Double.parseDouble(strTotalPrice1));
            tvPriceCart.setText(formattedNewPrice);


        });

//        logic chose option topping
        rg_topping.setOnCheckedChangeListener((group, checkedId) -> {
            int newPrice2 = product.getRealPrice();
            int toppingOption = rg_topping.getCheckedRadioButtonId() != -1 ? (int) rg_topping.findViewById(rg_topping.getCheckedRadioButtonId()).getTag() : 0;
            if (toppingOption >= 1) {

                String selectedTopping = product.getColor().get(toppingOption);
                product.setSaveColor(selectedTopping);


            }
//            product.setSaveTopping(toppingOption);

            int newTotalPrice = newPrice2;



            int totalPrice1 = newTotalPrice;
            product.setTotalPrice(totalPrice1);
            String strTotalPrice1 = totalPrice1 + "";
            String formattedNewPrice = formatter.format(Double.parseDouble(strTotalPrice1));
            tvPriceCart.setText(formattedNewPrice);

        });


//        end login




        tvCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

        tvAddCart.setOnClickListener(v -> {
            ProductDataBase.getInstance(ProductDetailActivity.this).productDAO().insertFood(product);
            bottomSheetDialog.dismiss();
            setStatusButtonAddToCart();
            EventBus.getDefault().post(new ReloadListCartEvent());
        });

        bottomSheetDialog.show();
    }
}