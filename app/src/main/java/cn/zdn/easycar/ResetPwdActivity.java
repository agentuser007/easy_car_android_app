package cn.zdn.easycar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cn.zdn.easycar.dialog.LoadingDialog;
import cn.zdn.easycar.viewmodel.UserViewModel;

public class ResetPwdActivity extends AppCompatActivity {

    private UserViewModel mUserViewModel;

    private EditText mUsername;
    private EditText mPhone;
    private EditText mPassword;
    private EditText mDoPassword;

    private LoadingDialog updateProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(R.string.action_pwd_modify);

        init();
    }

    private void init() {
        updateProgress = new LoadingDialog(this, R.string.dialog_loading_reset_wd);
        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mDoPassword = findViewById(R.id.do_password);
        mPhone = findViewById(R.id.phone);

        initViewModel();
    }

    private void initViewModel() {
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mUserViewModel.getTipMessage().observe(this, tipMessage -> {
            if (tipMessage.isRes()) {
                Toast.makeText(this, tipMessage.getMsgId(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, tipMessage.getMsgStr(), Toast.LENGTH_SHORT).show();
            }
        });
        mUserViewModel.getUserInfo().observe(this, userInfo -> {
            Toast.makeText(ResetPwdActivity.this, "修改密码成功", Toast.LENGTH_SHORT).show();
            onBackPressed();
        });
    }

    public void goUpdatePwd(View view) {
        String uName = mUsername.getText().toString().trim();
        String uPhone = mPhone.getText().toString().trim();
        String uPwd = mPassword.getText().toString().trim();
        String uDoPwd = mDoPassword.getText().toString().trim();
        if (TextUtils.isEmpty(uName) || TextUtils.isEmpty(uPwd) || TextUtils.isEmpty(uDoPwd) || TextUtils.isEmpty(uPhone)) {
            Toast.makeText(ResetPwdActivity.this, "请确保所有信息填写完毕", Toast.LENGTH_SHORT).show();
        }
        if (uPhone.length() != 11) {
            Toast.makeText(ResetPwdActivity.this, "手机号输入错误", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!uPwd.equals(uDoPwd)) {
            Toast.makeText(ResetPwdActivity.this, "两次密码输入不同", Toast.LENGTH_SHORT).show();
            return;
        }

        mUserViewModel.doResetPwd(uName, uPwd, uPhone, updateProgress);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (updateProgress.isShowing()) {
            updateProgress.dismiss();
        }
        super.onDestroy();
    }
}