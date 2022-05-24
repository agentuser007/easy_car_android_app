package cn.zdn.easycar.ui.notifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import cn.zdn.easycar.LoginActivity;
import cn.zdn.easycar.R;
import cn.zdn.easycar.config.Constants;
import cn.zdn.easycar.pojo.UserInfo;
import cn.zdn.easycar.util.SPUtil;

public class PersonalInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("个人信息");


        TextView name = findViewById(R.id.person_name);
        TextView phone = findViewById(R.id.person_phone);
        TextView state = findViewById(R.id.person_state);


        name.setText(SPUtil.build().getString(Constants.SP_USER_NAME));

        phone.setText(SPUtil.build().getString(Constants.SP_USER_PHONE));
        if(SPUtil.build().getInt(Constants.SP_USER_STATE)==1) state.setText("用户");
        else state.setText("用户");


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout(View view) {

        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(this);
        alertdialogbuilder.setMessage("确定要退出登录吗");
        alertdialogbuilder.setPositiveButton("确定",  new DialogInterface.OnClickListener() {
            //添加"Yes"按钮
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                gotoLogin();                    }
        });

        alertdialogbuilder.setNeutralButton("取消", null);
        final AlertDialog alertdialog = alertdialogbuilder.create();
        alertdialog.show();

    }

    private void gotoLogin() {
        SPUtil.build().putBoolean(Constants.SP_BEEN_LOGIN, false);
        SPUtil.build().putString(Constants.SP_USER_CAR_LICENCE, "");

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();

    }
}