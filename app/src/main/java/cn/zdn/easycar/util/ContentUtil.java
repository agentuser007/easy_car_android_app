package cn.zdn.easycar.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cn.zdn.easycar.config.Constants;

public class ContentUtil {

    /**
     * 加载动态图片
     */
    public static void loadFeedImage(ImageView imageView, String url) {
        url = Constants.IMG_URL + url;
        Glide.with(imageView.getContext())
                .load(url)
                .centerCrop()
                .into(imageView);
    }

    /**
     * 加载图片
     */
    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .centerCrop()
                .into(imageView);
    }
}
