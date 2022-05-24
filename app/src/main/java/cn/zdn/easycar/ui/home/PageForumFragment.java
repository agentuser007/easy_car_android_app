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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.zdn.easycar.R;
import cn.zdn.easycar.config.Api;
import cn.zdn.easycar.okhttp.OkUtil;
import cn.zdn.easycar.okhttp.ResultCallback;
import cn.zdn.easycar.pojo.Feed;
import cn.zdn.easycar.pojo.PageInfo;
import cn.zdn.easycar.result.Result;
import cn.zdn.easycar.result.ResultConstant;
import cn.zdn.easycar.viewmodel.FeedViewModel;
import okhttp3.Call;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

public class PageForumFragment extends Fragment {

    private QuickAdapter mAdapter;
    private int mPageNum = 1;
    private int totalFeed = 0;
    private FeedViewModel mFeedViewModel;
    RefreshLayout refreshLayout;
    private static final int PAGE_SIZE = 20;


    public PageForumFragment() {
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
        refreshLayout = view.findViewById(R.id.refreshLayout);

        mAdapter = new QuickAdapter();

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
        mAdapter.openLoadAnimation();


        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mAdapter.getItemCount() < 2) {
                            try{
                                mPageNum = 1;
                                //mAdapter.replaceData(getFeed());
                                mFeedViewModel.doPageFeed(mPageNum, PAGE_SIZE,1);                            }catch (Exception e){
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
                                //mAdapter.replaceData(getFeed());
                                mFeedViewModel.doPageFeed(mPageNum, PAGE_SIZE,1);                            }catch (Exception e){
                            }
                        }
                        refreshLayout.finishLoadMore();
                    }
                },1000);
            }
        });
        
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
              //  Toast.makeText(getContext(), "onItemClick" + position, Toast.LENGTH_SHORT).show();
                //readDialog(msgs.get(position));
                Feed feed = (Feed) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), FeedActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("feed", feed);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        if (mAdapter != null) {
            recyclerView.setAdapter(mAdapter);
        }
        initViewModel();
        getFeed(1,20);

    }

    private void initViewModel() {
        mFeedViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        mFeedViewModel.getTipMessage().observe(requireActivity(), tipMessage -> {
            if (refreshLayout.isRefreshing()) {
                refreshLayout.finishRefresh();
            }
            if (tipMessage.isRes()) {

            } else {
                Toast.makeText(getContext(), tipMessage.getMsgStr() , Toast.LENGTH_SHORT).show();

            }
            mAdapter.loadMoreEnd();
        });
        mFeedViewModel.getFeedPage().observe(requireActivity(), feedPageInfo -> {
            if (refreshLayout.isRefreshing()) {
                refreshLayout.finishRefresh();
            }
            Integer pageNum = feedPageInfo.getPageNum();
            mPageNum = pageNum + 1;
            List<Feed> list = feedPageInfo.getList();
            if (pageNum == 1) {
                setData(list);
            } else {
                mAdapter.addData(list);
                //updateData(list);
            }
            mAdapter.loadMoreEnd();
        });
    }


    public static class QuickAdapter extends BaseQuickAdapter<Feed, BaseViewHolder> {
        public QuickAdapter() {
            super(R.layout.item_feed_item);
        }

        @Override
        protected void convert(BaseViewHolder viewHolder, Feed item) {

                viewHolder.setText(R.id.lmi_title, item.getFeedTitle())
                        .setText(R.id.lmi_describe, item.getFeedInfo())
                        .setText(R.id.lmi_author, "作者："+item.getUser().getUsername())
                ;

        }
    }
    
    public void setData(List<Feed> feedPage){
        List<Feed> feedList = feedPage;
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

                .execute(new ResultCallback<Result<PageInfo<Feed>>>(){
                    @Override
                    public void onSuccess(Result<PageInfo<Feed>> response) {
                        String code = response.getCode();
                        if (ResultConstant.CODE_SUCCESS.equals(code)) {
                            setData(response.getData().getList());
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
