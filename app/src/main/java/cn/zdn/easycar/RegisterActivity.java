package cn.zdn.easycar;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cn.zdn.easycar.config.Constants;
import cn.zdn.easycar.dialog.LoadingDialog;
import cn.zdn.easycar.util.SPUtil;
import cn.zdn.easycar.viewmodel.UserViewModel;

public class RegisterActivity extends AppCompatActivity {

    private UserViewModel mUserViewModel;

    private EditText mUsername;
    private EditText mPassword;
    private EditText mDoPassword;
    private EditText mPhone;

    private LoadingDialog registerProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

/*
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(R.string.action_register);
*/

        init();

    }

    private void init() {
        registerProgress = new LoadingDialog(this, R.string.dialog_loading_reset_wd);
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
                Log.i("tipMessage.getMsgId",tipMessage.getMsgId()+"");
            } else {
                Log.i("tipMessage.getMsgStr",tipMessage.getMsgStr()+"");
            }
        });
        mUserViewModel.getUserInfo().observe(this, userInfo -> {
            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
            SPUtil.build().putString(Constants.SP_USER_NAME, userInfo.getUsername());
            onBackPressed();
        });
    }

        public void goRegister(View view) {
            String uName = mUsername.getText().toString().trim();
            String uPwd = mPassword.getText().toString().trim();
            String uDoPwd = mDoPassword.getText().toString().trim();
            String uPhone = mPhone.getText().toString().trim();
            if (TextUtils.isEmpty(uName) || TextUtils.isEmpty(uPwd) || TextUtils.isEmpty(uDoPwd) || TextUtils.isEmpty(uPhone)) {
                Toast.makeText(RegisterActivity.this, "请确保信息全部填写", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!uPwd.equals(uDoPwd)) {
                Toast.makeText(RegisterActivity.this, "两次密码输入不同", Toast.LENGTH_SHORT).show();
                return;
            }
            if (uPhone.length() != 11) {
                Toast.makeText(RegisterActivity.this, "手机号码输入错误", Toast.LENGTH_SHORT).show();
                return;
            }

            mUserViewModel.doRegister(uName, uPwd, uPhone, registerProgress);
    }

    @Override
    protected void onDestroy() {
        if (registerProgress.isShowing()) {
            registerProgress.dismiss();
        }
        super.onDestroy();
    }

/*
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/
}