package cn.zdn.easycar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.io.Serializable;
import java.util.Date;
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
import cn.zdn.easycar.ui.home.FeedActivity;
import cn.zdn.easycar.ui.notifications.UserFeedActivity;
import cn.zdn.easycar.util.PagerPosition;
import cn.zdn.easycar.util.SPUtil;
import okhttp3.Call;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

@SuppressWarnings("ALL")
public class CarViolationActivity extends AppCompatActivity {

    String saveCarId = "";
    String isLook = "已读";
    String unLook = "未读";
    Vio vio = new Vio();


    private QuickAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_violation);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("违章查询");

        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        TextView textView = findViewById(R.id.licence_show);

        try {
            saveCarId = SPUtil.build().getString(Constants.SP_USER_CAR_LICENCE);
        }catch (Exception e){
            textView.setText("请先设置车辆信息");
            Toast.makeText(CarViolationActivity.this, "请先设置车辆信息" , Toast.LENGTH_SHORT).show();
        }
        if(saveCarId.isEmpty()){
            textView.setText("请先设置车辆信息");
        }else {
            mAdapter = new QuickAdapter();
            recyclerView.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(mAdapter);
            mAdapter.openLoadAnimation();
            mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Toast.makeText(CarViolationActivity.this, "onItemClick" + position, Toast.LENGTH_SHORT).show();
                    //readDialog(msgs.get(position));
                    Log.i("click", adapter.getItem(position).toString());
                    dialog((Vio) adapter.getItem(position));

                }
            });

            textView.setText(saveCarId);
            queryVio();
        }

    }

    public void dialog(Vio vio) {

        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(this);
        alertdialogbuilder.setMessage("确定已读？");
        alertdialogbuilder.setPositiveButton("确定",  new DialogInterface.OnClickListener() {
            //添加"Yes"按钮
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setIsReadHttp(vio.getVioId());
            }
        });
        alertdialogbuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        final AlertDialog alertdialog = alertdialogbuilder.create();
        alertdialog.show();
    }



    public class QuickAdapter extends BaseQuickAdapter<Vio, BaseViewHolder> {
            public QuickAdapter() {
                super(R.layout.item_violation_item);
            }

            @Override
            protected void convert(BaseViewHolder viewHolder, Vio item) {
                try{
                    viewHolder.setText(R.id.vio_info, item.getAction())
                            .setText(R.id.vio_isHandle, item.getIsLook())
                            .setText(R.id.vio_location, item.getLocation())
                            .setText(R.id.vio_punish, item.getPunish())
                            .setText(R.id.vio_time, item.getCreateTime());

                }catch (Exception e){
                    Log.i("BaseQuickAdapter convert",e.toString());
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



    //api
    public void setData(List<Vio> vioList){
        try {
            mAdapter.replaceData(vioList);
            if (mAdapter.getData().isEmpty()) {
                View emptyView = getLayoutInflater().inflate(R.layout.empty, null);
                mAdapter.setEmptyView(emptyView);
            }
        } catch (Exception e) {
            View emptyView = getLayoutInflater().inflate(R.layout.empty, null);
            mAdapter.setEmptyView(emptyView);
        }

    }

    public void queryVio(){
        OkUtil.post()
                .url(Api.queryVio)
                .addParam("licence", saveCarId)
                .execute(new ResultCallback<Result<List<Vio>>>(){
                    @Override
                    public void onSuccess(Result<List<Vio>> response) {
                        String code = response.getCode();
                        if (ResultConstant.CODE_SUCCESS.equals(code)) {
                            setData(response.getData());
                        } else {
                            Toast.makeText(CarViolationActivity.this, "获取消息失败" , Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(CarViolationActivity.this, "获取消息失败" , Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void setIsReadHttp(String vioId){
        OkUtil.post()
                .url(Api.readVio)
                .addParam("vioId", vioId)
                .execute(new ResultCallback<Result<Vio>>(){
                    @Override
                    public void onSuccess(Result<Vio> response) {
                        String code = response.getCode();
                        if (ResultConstant.CODE_SUCCESS.equals(code)) {
                            PagerPosition p = new PagerPosition();
                            p.setUnReadNum(p.getUnReadNum()-1);

                            Toast.makeText(CarViolationActivity.this, "成功" , Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(CarViolationActivity.this, "获取消息失败" , Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(CarViolationActivity.this, "获取消息失败" , Toast.LENGTH_SHORT).show();
                    }
                });
    }


}