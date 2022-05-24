package cn.zdn.easycar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

import cn.zdn.easycar.config.Api;
import cn.zdn.easycar.config.Constants;
import cn.zdn.easycar.dialog.LoadingDialog;
import cn.zdn.easycar.okhttp.OkUtil;
import cn.zdn.easycar.util.SPUtil;
import cn.zdn.easycar.viewmodel.UserViewModel;

public class LoginActivity extends AppCompatActivity {

    private AppCompatEditText mUsername;
    private AppCompatEditText mPassword;
    private UserViewModel mUserViewModel;
    private LoadingDialog loginProgress;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

    }

    private void init(){
        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        loginProgress = new LoadingDialog(this, R.string.dialog_loading_login);

        try {
            String saveName = SPUtil.build().getString(Constants.SP_USER_NAME);
            mUsername.setText(saveName);
            mUsername.setSelection(saveName.length());
        }catch (Exception e){
            Log.i("无本地保存用户名",e.toString());
        }
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
            SPUtil.build().putBoolean(Constants.SP_BEEN_LOGIN, true);
            SPUtil.build().putString(Constants.SP_USER_ID, userInfo.getId());
            SPUtil.build().putString(Constants.SP_USER_NAME, userInfo.getUsername());
            SPUtil.build().putInt(Constants.SP_USER_STATE, userInfo.getState());
            SPUtil.build().putString(Constants.SP_USER_PHONE, userInfo.getPhone());

            OkUtil.newInstance().addCommonHeader(Api.X_APP_TOKEN, userInfo.getId());
            goHome();
        });
    }

    public void login(View view) {
        String username = Objects.requireNonNull(mUsername.getText()).toString().trim();
        String password = Objects.requireNonNull(mPassword.getText()).toString().trim();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(view.getContext(), R.string.toast_login_null, Toast.LENGTH_SHORT).show();
            return;
        }

        mUserViewModel.doLogin(username, password, loginProgress);

    }

    public void updatePwd(View view) {
        Intent intent = new Intent(this, ResetPwdActivity.class);
        startActivity(intent);
    }

    private void goHome() {
        Intent intent = new Intent(this, MainActivity.class);
        //未读api
        intent.putExtra(Constants.PASSED_UNREAD_NUM, 0);
        startActivity(intent);
        finish();
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        if (loginProgress.isShowing()) {
            loginProgress.dismiss();
        }
        super.onDestroy();
    }
}