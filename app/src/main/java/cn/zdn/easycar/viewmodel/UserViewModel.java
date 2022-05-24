package cn.zdn.easycar.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import java.util.List;

import cn.zdn.easycar.R;
import cn.zdn.easycar.config.Api;
import cn.zdn.easycar.dialog.LoadingDialog;
import cn.zdn.easycar.okhttp.OkUtil;
import cn.zdn.easycar.okhttp.ResultCallback;
import cn.zdn.easycar.pojo.PageInfo;
import cn.zdn.easycar.pojo.TipMessage;
import cn.zdn.easycar.pojo.User;
import cn.zdn.easycar.pojo.UserInfo;
import cn.zdn.easycar.result.Result;
import cn.zdn.easycar.result.ResultConstant;
import cn.zdn.easycar.ui.home.HomeFragment;


public class UserViewModel extends ViewModel {

    private final MutableLiveData<TipMessage> mTipMessage;
    private final MutableLiveData<UserInfo> mUserInfo;
    private final MutableLiveData<PageInfo<User>> mUsers;

    public UserViewModel() {
        mTipMessage = new MutableLiveData<>();
        mUserInfo = new MutableLiveData<>();
        mUsers = new MutableLiveData<>();
    }

    public LiveData<TipMessage> getTipMessage() {
        return mTipMessage;
    }


    public LiveData<UserInfo> getUserInfo() {
        return mUserInfo;
    }

    public LiveData<PageInfo<User>> getUsers() {
        return mUsers;
    }

    /**
     * 登录
     * @param userName 用户名
     * @param userPwd 密码
     * @param loadingDialog 加载动画
     */
    public void doLogin(String userName, String userPwd, LoadingDialog loadingDialog) {
        OkUtil.post()
                .url(Api.userLogin)
                .addParam("username", userName)
                .addParam("password", userPwd)
                .setProgressDialog(loadingDialog)
                .setLoadDelay()
                .execute(new ResultCallback<Result<UserInfo>>() {
                    @Override
                    public void onSuccess(Result<UserInfo> response) {
                        String code = response.getCode();
                        if (ResultConstant.CODE_SUCCESS.equals(code)) {
                            mUserInfo.postValue(response.getData());
                        } else if("00105".equals(code)){
                            mTipMessage.postValue(TipMessage.resId(R.string.toast_user_error));

                        }
                        else {
                            mTipMessage.postValue(TipMessage.resId(R.string.toast_pwd_error));
                        }
                    }

                    @Override
                    public void onError(okhttp3.Call call, Exception e) {
                        mTipMessage.postValue(TipMessage.resId(R.string.toast_login_error));
                    }
                });
/*
        UserInfo test = new UserInfo();
        test.setId("1");
        test.setUsername("test name");
        test.setPhone("138");
        test.setState(1);
        mUserInfo.postValue(test);
*/
    }

    /**
     * 注册
     * @param userName 用户名
     * @param userPwd 密码
     * @param phone 手机号
     * @param loadingDialog 加载动画
     */
    public void doRegister(String userName, String userPwd, String phone, LoadingDialog loadingDialog) {
        OkUtil.post()
                .url(Api.userRegister)
                .addParam("username", userName)
                .addParam("password", userPwd)
                .addParam("phone", phone)
                .setProgressDialog(loadingDialog)
                .execute(new ResultCallback<Result<UserInfo>>() {

                    @Override
                    public void onSuccess(Result<UserInfo> response) {
                        String code = response.getCode();
                        switch (code) {
                            case ResultConstant.CODE_SUCCESS:
                                mUserInfo.postValue(response.getData());
                                break;
                            case "00105":
                                mTipMessage.postValue(TipMessage.resId(R.string.toast_phone_being));
                                break;
                            case "00106":
                                mTipMessage.postValue(TipMessage.resId(R.string.toast_username_being));
                                break;
                            default:
                                mTipMessage.postValue(TipMessage.resId(R.string.toast_reg_error));
                                break;
                        }
                    }

                    @Override
                    public void onError(okhttp3.Call call, Exception e) {
                        mTipMessage.postValue(TipMessage.resId(R.string.toast_reg_error));
                    }
                });
    }

    /**
     * 重置密码
     * @param userName 用户名
     * @param userPwd 密码
     * @param phone 手机号
     * @param loadingDialog 加载动画
     */
    public void doResetPwd(String userName, String userPwd, String phone, LoadingDialog loadingDialog) {
        OkUtil.post()
                .url(Api.resetPassword)
                .addParam("username", userName)
                .addParam("password", userPwd)
                .addParam("phone", phone)
                .setProgressDialog(loadingDialog)
                .execute(new ResultCallback<Result<UserInfo>>() {

                    @Override
                    public void onSuccess(Result<UserInfo> response) {
                        String code = response.getCode();
                        switch (code) {
                            case ResultConstant.CODE_SUCCESS:
                                mUserInfo.postValue(response.getData());
                                break;
                            case "00104":
                                mTipMessage.postValue(TipMessage.resId(R.string.toast_reset_pwd_user));
                                break;
                            default:
                                mTipMessage.postValue(TipMessage.resId(R.string.toast_reset_pwd_error));
                                break;
                        }
                    }

                    @Override
                    public void onError(okhttp3.Call call, Exception e) {
                        mTipMessage.postValue(TipMessage.resId(R.string.toast_reset_pwd_error));

                    }
                });
    }

    /**
     * 获取用户信息
     */
    public void doUserInfo() {
        this.doUserInfo(null);
    }

    /**
     * 获取用户信息
     * @param userId 用户id
     */
    public void doUserInfo(String userId) {
        OkUtil.post()
                .url(Api.userInfo)
                .addParam("id", userId)
                .execute(new ResultCallback<Result<UserInfo>>() {

                    @Override
                    public void onSuccess(Result<UserInfo> response) {
                        String code = response.getCode();
                        if (ResultConstant.CODE_SUCCESS.equals(code)) {
                            mUserInfo.postValue(response.getData());
                        } else {
                            mUserInfo.postValue(null);
                        }
                    }

                    @Override
                    public void onError(okhttp3.Call call, Exception e) {
                        mUserInfo.postValue(null);

                    }
                });
    }


}
