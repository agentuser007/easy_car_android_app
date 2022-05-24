package cn.zdn.easycar.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cn.zdn.easycar.CarInspectionActivity;
import cn.zdn.easycar.CarViolationActivity;
import cn.zdn.easycar.R;
import cn.zdn.easycar.SettingsActivity;
import cn.zdn.easycar.WebViewActivity;
import cn.zdn.easycar.config.Constants;
import cn.zdn.easycar.ui.home.FeedActivity;
import cn.zdn.easycar.util.PagerPosition;
import cn.zdn.easycar.webDoc.WebDocOneActivity;

public class DashboardFragment extends Fragment {




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);
        //View header = LayoutInflater.from(root.getContext()).inflate(R.layout.item_banner_header, null, false);

        Banner banner = root.findViewById(R.id.banner);
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(BANNER_ITEMS);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int i) {
                Toast.makeText(getContext(), "点击了第" + i + "页", Toast.LENGTH_SHORT).show();
                String url = "";
                if(i == 1) url = PagerPosition.banner_url_part_1;
                else url = PagerPosition.banner_url_part_2;
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
            Stream<String> stream = BANNER_ITEMS.stream().map(bannerItem -> bannerItem.title);
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
            banner.setBannerTitles(stream.collect(Collectors.<String>toList()));

        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(2500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

        //办年检
        LinearLayout linearLayout_ci = root.findViewById(R.id.car_inspection);
        linearLayout_ci.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(root.getContext(), CarInspectionActivity.class);
                startActivity(intent);
            }
        });

        //查违章
        LinearLayout linearLayout_cv = root.findViewById(R.id.car_vio);
        linearLayout_cv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(root.getContext(), CarViolationActivity.class);
                startActivity(intent);

            }
        });


        //违章热点
        LinearLayout linearLayout_ch = root.findViewById(R.id.car_hot);
        linearLayout_ch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(root.getContext(), HotVioActivity.class);
                startActivity(intent);
            }
        });


    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            imageView.setImageResource(((BannerItem) path).pic);

        }
    }

        public static List<BannerItem> BANNER_ITEMS = new ArrayList<BannerItem>(){{
        add(new BannerItem("年检建议", R.mipmap.banner1));
        add(new BannerItem("交通法规", R.mipmap.banner_rules));
    }};

    public static class BannerItem {

        public int pic;
        public String title;

        public BannerItem() {
        }

        public BannerItem(String title, int pic) {
            this.pic = pic;
            this.title = title;
        }
    }


}