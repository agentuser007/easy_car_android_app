package cn.zdn.easycar.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.zdn.easycar.LoginActivity;
import cn.zdn.easycar.MainActivity;
import cn.zdn.easycar.MsgActivity;
import cn.zdn.easycar.R;
import cn.zdn.easycar.SettingsActivity;
import cn.zdn.easycar.config.Constants;
import cn.zdn.easycar.util.PagerPosition;
import cn.zdn.easycar.util.SPUtil;

public class NotificationsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView unReadNum = view.findViewById(R.id.unReadNum);
        TextView username = view.findViewById(R.id.username);
        username.setText(SPUtil.build().getString(Constants.SP_USER_NAME));

        //我的消息
        LinearLayout linearLayout_alertMsg = view.findViewById(R.id.alertMsg);
        linearLayout_alertMsg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                unReadNum.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "我的消息", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), MsgActivity.class);
                startActivity(intent);

            }
        });
        //消息数
        int num = new PagerPosition().getUnReadNum();
        if(num>0){
            unReadNum.setText("" + num);
            unReadNum.setVisibility(View.VISIBLE);
        }

        //信息设置
        LinearLayout linearLayout_car_info = view.findViewById(R.id.carInfo);
        linearLayout_car_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), SettingsActivity.class);
                startActivity(intent);

            }
        });

        //我的帖子
        LinearLayout linearLayout_mf = view.findViewById(R.id.myFeed);
        linearLayout_mf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), UserFeedActivity.class);
                startActivity(intent);

            }
        });


        //我的账号
        LinearLayout linearLayout_mc = view.findViewById(R.id.myAccount);
        linearLayout_mc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), PersonalInfoActivity.class);
                startActivity(intent);

            }
        });


    }



}