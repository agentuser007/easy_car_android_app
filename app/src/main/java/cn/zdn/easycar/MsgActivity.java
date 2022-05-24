package cn.zdn.easycar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;


import cn.zdn.easycar.config.Api;
import cn.zdn.easycar.config.Constants;
import cn.zdn.easycar.okhttp.OkUtil;
import cn.zdn.easycar.okhttp.ResultCallback;
import cn.zdn.easycar.pojo.Feed;
import cn.zdn.easycar.pojo.PageInfo;
import cn.zdn.easycar.pojo.Vio;
import cn.zdn.easycar.result.Result;
import cn.zdn.easycar.result.ResultConstant;
import cn.zdn.easycar.ui.home.HomeFragment;
import cn.zdn.easycar.util.PagerPosition;
import cn.zdn.easycar.util.SPUtil;
import okhttp3.Call;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;


public class MsgActivity extends AppCompatActivity {

    private QuickAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("我的消息");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);

        mAdapter = new QuickAdapter();

        recyclerView.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);


        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mAdapter.getItemCount() < 2) {
                            //List<Msg> msgs = new Gson().fromJson(JSON_msg, new TypeToken<ArrayList<Msg>>() {}.getType());
                            try{
                                unreadHttp();
                            }catch (Exception e){
                            }
                        }
                        refreshLayout.finishRefresh();
                    }
                },2000);
            }
        });



        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(MsgActivity.this, "onItemClick" + position, Toast.LENGTH_SHORT).show();
                readDialog((Vio)adapter.getItem(position));
            }
        });
        unreadHttp();

    }

    public void readDialog(Vio msg){
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(this);
        alertdialogbuilder.setMessage( msg.toString());
        alertdialogbuilder.setPositiveButton("确认已读",  new DialogInterface.OnClickListener() {
            //添加"Yes"按钮
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                readHttp(msg);
            }
        });

        final AlertDialog alertdialog = alertdialogbuilder.create();
        alertdialog.show();

    }

    public class QuickAdapter extends BaseQuickAdapter<Vio, BaseViewHolder> {
        public QuickAdapter() {
            super(R.layout.item_feed_item);
        }

        @Override
        protected void convert(BaseViewHolder viewHolder, Vio item) {
            try {
                viewHolder.setText(R.id.lmi_title, item.getPunish())
                        .setText(R.id.lmi_describe, item.getCreateTime())
                .setText(R.id.lmi_author, item.getLocation());

            }catch (Exception e){

            }
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

    public void setData(List<Vio> feedPage){
        try{
            mAdapter.replaceData(feedPage);

            if (mAdapter.getData().isEmpty()) {
                View emptyView = getLayoutInflater().inflate(R.layout.empty, null);
                mAdapter.setEmptyView(emptyView);
            }
        }catch (Exception e){
            View emptyView = getLayoutInflater().inflate(R.layout.empty, null);
            mAdapter.setEmptyView(emptyView);
        }
    }



    //发送请求未读数据
    public void unreadHttp(){
        OkUtil.post()
                .url(Api.unread)
                .addParam("licence", SPUtil.build().getString(Constants.SP_USER_CAR_LICENCE))
                .execute(new ResultCallback<Result<List<Vio>>>(){
                    @Override
                    public void onSuccess(Result<List<Vio>> response) {
                        String code = response.getCode();
                        if (ResultConstant.CODE_SUCCESS.equals(code)) {
                            setData(response.getData());
                        } else {
                            Toast.makeText(MsgActivity.this, "获取消息失败" , Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(MsgActivity.this, "获取消息失败" , Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //发送已读数据
    public void readHttp(Vio vio){
        OkUtil.post()
                .url(Api.readVio)
                .addParam("vioId", vio.getVioId())
                .execute(new ResultCallback<Result<Vio>>() {
                    @Override
                    public void onSuccess(Result<Vio> response) {
                        String code = response.getCode();
                        PagerPosition p = new PagerPosition();
                        p.setUnReadNum(p.getUnReadNum()-1);
                        if (ResultConstant.CODE_SUCCESS.equals(code)) {

                        } else {
                            Log.i("idVio",response.getMsg());

                            Toast.makeText(MsgActivity.this, "更新已读状态失败" , Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(MsgActivity.this, "网络错误" , Toast.LENGTH_SHORT).show();
                    }
                });

    }


    }