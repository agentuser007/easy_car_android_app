package cn.zdn.easycar.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.io.Serializable;
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
import cn.zdn.easycar.util.SPUtil;
import okhttp3.Call;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;


public class PageRulesFragment extends Fragment {

    private QuickAdapter mAdapter;
    private int pageNum = 1;
    private int totalFeed = 0;


    public PageRulesFragment() {
        // required empty public constructor.
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page_rules, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        RefreshLayout refreshLayout = view.findViewById(R.id.refreshLayout);

        mAdapter = new QuickAdapter();

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
        mAdapter.openLoadAnimation();
        getFeed(1, 20);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFeed(1, 20);

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
                //readDialog(msgs.get(position));
                Intent intent = new Intent(getActivity(), FeedActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("feed", (Serializable) adapter.getItem(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }


    public static class QuickAdapter extends BaseQuickAdapter<Feed, BaseViewHolder> {
        public QuickAdapter() {
            super(R.layout.item_feed_item);
        }

        @Override
        protected void convert(BaseViewHolder viewHolder, Feed item) {

                viewHolder.setText(R.id.lmi_title, item.getFeedTitle())
                        .setText(R.id.lmi_describe, item.getFeedInfo());

        }
    }

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
                .addParam("state", 0)
                .addParam("pageNum", pageNum)
                .addParam("pageSize", pageSize)

                .execute(new ResultCallback<Result<PageInfo<Feed>>>(){
                    @Override
                    public void onSuccess(Result<PageInfo<Feed>> response) {
                        String code = response.getCode();
                        if (ResultConstant.CODE_SUCCESS.equals(code)) {
                            setData(response.getData());
                        } else {
                            Toast.makeText(getContext(), "获取消息失败" , Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(getContext(), "获取消息失败" , Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
