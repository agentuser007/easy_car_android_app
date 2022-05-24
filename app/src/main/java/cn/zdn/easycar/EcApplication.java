package cn.zdn.easycar;

import android.app.Application;

import java.util.HashMap;
import java.util.Map;

import cn.zdn.easycar.config.Api;
import cn.zdn.easycar.okhttp.OkUtil;
import cn.zdn.easycar.util.SPUtil;

public class EcApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 注册
        SPUtil.newInstance().init(this);
        initOkUtil();
    }


    /**
     * 初始化OkUtil
     */
    private void initOkUtil() {
        // 公共请求头
        Map<String, String> headers = new HashMap<>(1);
        String token = SPUtil.build().getString(Api.X_APP_TOKEN);
        headers.put(Api.X_APP_TOKEN, token);
        // 注册添加公共请求头
        OkUtil.newInstance().init(this)
                .addCommonHeaders(headers);
    }

}
