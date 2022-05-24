package cn.zdn.easycar.ui.notifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import cn.zdn.easycar.MsgActivity;
import cn.zdn.easycar.R;
import cn.zdn.easycar.config.Api;
import cn.zdn.easycar.config.Constants;
import cn.zdn.easycar.okhttp.OkUtil;
import cn.zdn.easycar.okhttp.ResultCallback;
import cn.zdn.easycar.pojo.Feed;
import cn.zdn.easycar.pojo.PageInfo;
import cn.zdn.easycar.result.Result;
import cn.zdn.easycar.result.ResultConstant;
import cn.zdn.easycar.ui.home.FeedActivity;
import cn.zdn.easycar.util.PagerPosition;
import cn.zdn.easycar.util.SPUtil;
import okhttp3.Call;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

public class UserFeedActivity extends AppCompatActivity {


    private QuickAdapter mAdapter;
    private int pageNum = 1;
    private int totalFeed = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("我的帖子");

        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);

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
                            try{
                                //mAdapter.replaceData(getFeed());
                                getFeed(1, 20);
                            }catch (Exception e){
                            }
                        }
                        refreshLayout.finishRefresh();
                    }
                },2000);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mAdapter.getItemCount() < totalFeed) {
                            try{
                                pageNum += 1;
                                //mAdapter.replaceData(getFeed());
                                getFeed(pageNum, 20);
                            }catch (Exception e){
                            }
                        }
                        refreshLayout.finishLoadMore();
                    }
                },2000);
            }
        });


        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //Toast.makeText(UserFeedActivity.this, "onItemClick" + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserFeedActivity.this, FeedActivity.class);
                Bundle bundle = new Bundle();
                Feed feed = (Feed) adapter.getItem(position);
                bundle.putSerializable("feed", feed);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        getFeed(1,20);

    }

    public class QuickAdapter extends BaseQuickAdapter<Feed, BaseViewHolder> {
        public QuickAdapter() {
            super(R.layout.item_feed_item);
        }

        @Override
        protected void convert(BaseViewHolder viewHolder, Feed item) {
            try {
                viewHolder.setText(R.id.lmi_title, item.getFeedTitle())
                        .setText(R.id.lmi_author, "作者："+item.getUser().getUsername())
                        .setText(R.id.lmi_describe, item.getCreateTime());
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


    //api
    public void setData(PageInfo<Feed> feedPage){
        totalFeed = feedPage.getTotal();
        List<Feed> feedList = feedPage.getList();
        try{
            mAdapter.replaceData(feedList);

            if (mAdapter.getData().isEmpty()) {
                View emptyView = getLayoutInflater().inflate(R.layout.empty, null);
                mAdapter.setEmptyView(emptyView);
            }
        }catch (Exception e){
            View emptyView = getLayoutInflater().inflate(R.layout.empty, null);
            mAdapter.setEmptyView(emptyView);
        }
    }

    public void getFeed(int pageNum, int pageSize){
        OkUtil.post()
                .url(Api.pageFeed)
                .addParam("state", 1)
                .addParam("pageNum", pageNum)
                .addParam("pageSize", pageSize)
                .addParam("searchUserId", SPUtil.build().getString(Constants.SP_USER_ID))
                .execute(new ResultCallback<Result<PageInfo<Feed>>>(){
                    @Override
                    public void onSuccess(Result<PageInfo<Feed>> response) {
                        String code = response.getCode();
                        if (ResultConstant.CODE_SUCCESS.equals(code)) {
                            setData(response.getData());
                        } else {
                            Toast.makeText(UserFeedActivity.this, "获取消息失败" , Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(UserFeedActivity.this, "获取消息失败" , Toast.LENGTH_SHORT).show();
                    }
                });
    }


}