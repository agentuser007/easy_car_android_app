package cn.zdn.easycar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.tbruyelle.rxpermissions3.RxPermissions;

import java.util.List;

import cn.zdn.easycar.config.Api;
import cn.zdn.easycar.config.Constants;
import cn.zdn.easycar.okhttp.OkUtil;
import cn.zdn.easycar.okhttp.ResultCallback;
import cn.zdn.easycar.pojo.Vio;
import cn.zdn.easycar.result.Result;
import cn.zdn.easycar.result.ResultConstant;
import cn.zdn.easycar.ui.home.HomeFragment;
import cn.zdn.easycar.util.PagerPosition;
import cn.zdn.easycar.util.SPUtil;


public class SplashActivity extends AppCompatActivity {

    private boolean isLogin;
 //   private boolean tokenNull;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        isLogin = SPUtil.build().getBoolean(Constants.SP_BEEN_LOGIN);
        requestPermission();
    }

    private void requestPermission() {
        final RxPermissions rxPermissions = new RxPermissions(this); // where this is an Activity or Fragment instance

            rxPermissions
                    .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) { // Always true pre-M
                            // I can control the camera now
                         //   Toast.makeText(SplashActivity.this, "允许了权限!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Oups permission denied
                            Toast.makeText(SplashActivity.this, "未授权权限，部分功能不能使用", Toast.LENGTH_SHORT).show();
                        }
                        init();
                    });

    }

    private void init() {
        Log.i( "init " , String.valueOf(isLogin));

 //       String token = SPUtil.build().getString(Api.X_APP_TOKEN);
        // 此版本校验token
        //tokenNull = TextUtils.isEmpty(token);
        //tokenNull = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLogin) {
                    goHome(0);
                    //getUnRead();
                } else {
                    goLogin();
                }
            }
        }, 150);
    }

    public void getUnRead() {
        String licence = SPUtil.build().getString(Constants.SP_USER_CAR_LICENCE);

        OkUtil.post()
                .url(Api.unread)
                .addParam("licence", licence)
                .execute(new ResultCallback<Result<List<Vio>>>() {
                    @Override
                    public void onSuccess(Result<List<Vio>> response) {
                        goHome(ResultConstant.CODE_SUCCESS.equals(response.getCode()) ? response.getData().size() : 0);
                    }
                    @Override
                    public void onError(okhttp3.Call call, Exception e) {
                        goHome(0);

                    }
                });

    }

    /**
     * 前往主页
     */
    private void goHome(int num) {
        new PagerPosition().setUnReadNum(num);
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.putExtra(Constants.PASSED_UNREAD_NUM, num);
        startActivity(intent);
        finish();
    }

    /**
     * 前往登录
     */
    private void goLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}