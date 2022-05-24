package cn.zdn.easycar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.zdn.easycar.config.Api;
import cn.zdn.easycar.config.Constants;
import cn.zdn.easycar.okhttp.OkUtil;
import cn.zdn.easycar.okhttp.ResultCallback;
import cn.zdn.easycar.pojo.Vio;
import cn.zdn.easycar.result.Result;
import cn.zdn.easycar.result.ResultConstant;
import cn.zdn.easycar.util.PagerPosition;
import cn.zdn.easycar.util.SPUtil;
import cn.zdn.easycar.util.SendNotification;

public class MainActivity extends AppCompatActivity {

    private TextView badgeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        initBadge();

        Timer timer = null;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //在这里写要循环实现的功能
                getUnRead();
                Log.i("timerTask","");
            }
        };

        if(null == timer)
            timer = new Timer();
        timer.schedule(timerTask,0,15000);//延迟1秒，间隔15秒循环执行timeTask的run方法

    }


    public void getUnRead() {
        String licence = SPUtil.build().getString(Constants.SP_USER_CAR_LICENCE);
        OkUtil.post()
                .url(Api.unread)
                .addParam("licence", licence)
                .execute(new ResultCallback<Result<List<Vio>>>() {
                    @Override
                    public void onSuccess(Result<List<Vio>> response) {
                        int num = response.getData().size();
                        PagerPosition p = new PagerPosition();
                        if(p.getUnReadNum() != num){
                            p.setUnReadNum(num);
                            if(SPUtil.build().getBoolean(Constants.SP_NOTIFICATION, true)){
                                sendNoti();
                            }
                            initBadge();
                        }
                        // SendNotification.showNotification(SettingsActivity.this);
                    }
                    @Override
                    public void onError(okhttp3.Call call, Exception e) {
                    }
                });
    }
    public void sendNoti(){
        SendNotification.showNotification(MainActivity.this);
    }


    public void writeFeed(View view) {
        Intent intent = new Intent(this, PublishActivity.class);
        startActivity(intent);

    }

    private void initBadge() {
        goneBadge();

/*
        Intent intent = getIntent();
        if (intent == null) return;
        BottomNavigationView navigationView = findViewById(R.id.nav_view);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView)navigationView.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(2);
        View badge = LayoutInflater.from(this).inflate(R.layout.main_menu_badge, menuView, false);
        itemView.addView(badge);
        badgeView = badge.findViewById(R.id.tv_msg_count);
        if (new PagerPosition().getUnReadNum() > 0) {
            Constants.isRead = false;
            visibleBadge();
        } else {
            goneBadge();
        }
*/
    }

    public void goneBadge() {
        if (badgeView != null) {
            badgeView.setVisibility(View.GONE);
        }
    }

    public void visibleBadge() {
        if (badgeView != null) {
            badgeView.setVisibility(View.VISIBLE);
        }

    }
}