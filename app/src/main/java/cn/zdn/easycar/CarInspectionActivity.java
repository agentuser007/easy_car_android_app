package cn.zdn.easycar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.zdn.easycar.config.Api;
import cn.zdn.easycar.config.Constants;
import cn.zdn.easycar.okhttp.OkUtil;
import cn.zdn.easycar.okhttp.ResultCallback;
import cn.zdn.easycar.pojo.CarInfo;
import cn.zdn.easycar.result.Result;
import cn.zdn.easycar.result.ResultConstant;
import cn.zdn.easycar.ui.home.HomeFragment;
import cn.zdn.easycar.util.PagerPosition;
import cn.zdn.easycar.util.SPUtil;
import cn.zdn.easycar.util.SendNotification;
import cn.zdn.easycar.webDoc.WebDocOneActivity;
import okhttp3.Call;

public class CarInspectionActivity extends AppCompatActivity {
    String saveCarId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_inspection);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("办年检");
        init();
    }

    public void init(){
        TextView textView = findViewById(R.id.vehicle_inspection);
        String cI = getResources().getString(R.string.vehicle_inspection);

        try {
            saveCarId = SPUtil.build().getString(Constants.SP_USER_CAR_LICENCE);
        }catch (Exception e){
            Toast.makeText(CarInspectionActivity.this, "请先设置车辆信息" , Toast.LENGTH_SHORT).show();
        }
        //获取数据
        if(!saveCarId.isEmpty()){
            //String carInsDay = checkCarInfoHttp();
            checkCarInfoHttp();
            //cI = String.format(cI, carInsDay);
           // textView.setText(cI);
        }else {
            Toast.makeText(CarInspectionActivity.this, "请先设置车辆信息", Toast.LENGTH_SHORT).show();
            textView.setText("请先设置车辆信息");

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void one(View view) {
        openWebDoc(1);
/*
        Intent intent = new Intent(view.getContext(), WebViewActivity.class);
        intent.putExtra("url", PagerPosition.inspection_advice_1);
        startActivity(intent);
*/
    }

    public void two(View view) {
        openWebDoc(2);
    }

    public void three(View view) {
        Intent intent = new Intent(view.getContext(), WebViewActivity.class);
        intent.putExtra("url", PagerPosition.inspection_advice_3);
        startActivity(intent);

    }

    public void advice(View view) {
        openWebDoc(4);

    }

    //发送请求
    public void checkCarInfoHttp(){
        OkUtil.post()
                .url(Api.userCar)
                .addParam("userId", SPUtil.build().getString(Constants.SP_USER_ID))
                .execute(new ResultCallback<Result<CarInfo>>(){
                    @Override
                    public void onSuccess(Result<CarInfo> response) {
                        String code = response.getCode();
                        if (ResultConstant.CODE_SUCCESS.equals(code)) {
                            show(days(response.getData()));
                        } else {
                            show("xx");
                            Toast.makeText(CarInspectionActivity.this, "请先设置车辆信息" , Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(CarInspectionActivity.this, "网络错误" , Toast.LENGTH_SHORT).show();
                        show("xx");
                    }
                });
    }

    public void show(String s){
        TextView textView = findViewById(R.id.vehicle_inspection);
        String cI = getResources().getString(R.string.vehicle_inspection);
        cI = String.format(cI, s);
        textView.setText(cI);

    }

    public String days(CarInfo date){
        String s = date.getPermitTime().toString();

        try{
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new java.text.SimpleDateFormat(
                    "EEE MMM ddHH:mm:ss 'GMT+08:00' yyyy", Locale.US);
            Date endDate = format.parse(s);
            long now = System.currentTimeMillis();
            Date nowDate = new Date(now);
            long days = (endDate.getTime() - nowDate.getTime()) / (24 * 60 * 60 * 1000) ;
            if(days < 31 &&
                    SPUtil.build().getBoolean(Constants.SP_NOTIFICATION, true)){
                SendNotification.showCarInsNotification(CarInspectionActivity.this);

            }
            return String.valueOf(days);
        }catch (Exception e){
            Log.e("DateFormat",e.toString());
        }
        return "出错了";
    }

    public void openWebDoc(int num){
        Intent intent = new Intent(this, WebDocOneActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("sel", num);
        intent.putExtras(bundle);
        startActivity(intent);

    }

}