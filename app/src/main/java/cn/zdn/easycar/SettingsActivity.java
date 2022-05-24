package cn.zdn.easycar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import cn.zdn.easycar.config.Api;
import cn.zdn.easycar.config.Constants;
import cn.zdn.easycar.okhttp.OkUtil;
import cn.zdn.easycar.okhttp.ResultCallback;
import cn.zdn.easycar.pojo.CarInfo;
import cn.zdn.easycar.result.Result;
import cn.zdn.easycar.result.ResultConstant;
import cn.zdn.easycar.ui.home.HomeFragment;
import cn.zdn.easycar.util.SPUtil;
import cn.zdn.easycar.util.SendNotification;
import okhttp3.Call;

public class SettingsActivity extends AppCompatActivity {
    String s = "系统繁忙";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("信息设置");

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch switcher = findViewById(R.id.switch_reminder);
        try{
            switcher.setChecked(SPUtil.build().getBoolean(Constants.SP_NOTIFICATION));
        }catch (Exception e){
            SPUtil.build().putBoolean(Constants.SP_NOTIFICATION, true);
        }
            switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //打开提醒
                    SPUtil.build().putBoolean(Constants.SP_NOTIFICATION, true);
                    SendNotification.showNotification(SettingsActivity.this);
                } else {
                    SPUtil.build().putBoolean(Constants.SP_NOTIFICATION, false);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void checkCarInfo(View view) {
        checkCarInfoHttp();
    }
    public void getInfo(CarInfo carInfo){
      //  s = carInfo.toString();
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(this);
        alertdialogbuilder.setMessage(carInfo.toString());
        alertdialogbuilder.setPositiveButton("确定",  new DialogInterface.OnClickListener() {
            //添加"Yes"按钮
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        final AlertDialog alertdialog = alertdialogbuilder.create();
        alertdialog.show();

    }


    public void checkCarInfoHttp(){
        OkUtil.post()
                .url(Api.userCar)
                .addParam("userId", SPUtil.build().getString(Constants.SP_USER_ID))
                .execute(new ResultCallback<Result<CarInfo>>(){
                    @Override
                    public void onSuccess(Result<CarInfo> response) {
                        String code = response.getCode();
                        //Log.i("SettingsActivity",response.getData().toString());
                        if (ResultConstant.CODE_SUCCESS.equals(code)) {
                            getInfo(response.getData());
                        } else {
                            Toast.makeText(SettingsActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(SettingsActivity.this, "网络错误" , Toast.LENGTH_SHORT).show();
                    }
                });
    }



    public void setCarInfo(View view) {
        try {
            String getCarId = SPUtil.build().getString(Constants.SP_USER_CAR_LICENCE);

            if(!getCarId.isEmpty()){
                Toast.makeText(SettingsActivity.this, getCarId+" 已设置车辆信息" , Toast.LENGTH_SHORT).show();

            }else {
                Intent intent = new Intent(this, SetCarInfoActivity.class);
                startActivity(intent);

            }

        }catch (Exception e){
            Intent intent = new Intent(this, SetCarInfoActivity.class);
            startActivity(intent);
        }

    }
}