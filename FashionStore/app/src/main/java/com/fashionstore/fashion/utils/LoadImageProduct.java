package com.fashionstore.fashion.utils;

import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.fashionstore.fashion.R;


public class LoadImageProduct {

    public static boolean isEmpty(String input) {
        return input == null || input.isEmpty() || ("").equals(input.trim());
    }

    public static void loadUrl(String url, ImageView imageView) {
        if (isEmpty(url)) {
            imageView.setImageResource(R.drawable.image_no_available);
            return;
        }
        Glide.with(imageView.getContext())
                .load(url)
                .error(R.drawable.image_no_available)
                .dontAnimate()
                .into(imageView);
    }
}