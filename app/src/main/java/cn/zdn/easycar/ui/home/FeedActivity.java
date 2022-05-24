package cn.zdn.easycar.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


import java.util.List;

import cn.zdn.easycar.MsgActivity;
import cn.zdn.easycar.R;
import cn.zdn.easycar.pojo.Comment;
import cn.zdn.easycar.pojo.Feed;
import cn.zdn.easycar.viewmodel.FeedViewModel;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

public class FeedActivity extends AppCompatActivity implements View.OnClickListener{

    private AppCompatEditText mEditTuCao;
    private Button mBtnPublish;
    private View mEditMask;
    private RecyclerView mRecyclerView;
    TextView mFeedCommentNum;
    ImageView feedLikeIcon;
    LinearLayout feedLikeLayout;


    private FeedViewModel mFeedViewModel;
    private TextView feedLikeNum;
    private QuickAdapter mAdapter;

    private InputMethodManager imm;

    private int MSG_MODE;
    private final int MSG_EVALUATE = 0;
    private final int MSG_REPLY = 1;

    private String mFeedId;
    private String mCommentId;

    private Feed feed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("帖子详情");

        init();
    }
    private void init() {
        mEditTuCao = findViewById(R.id.edit_tu_cao);
        mBtnPublish = findViewById(R.id.btn_publish);
        mEditMask = findViewById(R.id.edit_mask);
        mRecyclerView = findViewById(R.id.recyclerView);
 //       LinearLayout feedCommentLayout = findViewById(R.id.feed_comment_layout);
        feedLikeIcon = findViewById(R.id.feed_like_icon);
        feedLikeLayout = findViewById(R.id.feed_like_layout);

        // 点击事件
        mEditTuCao.setOnClickListener(this);
        mBtnPublish.setOnClickListener(this);
       // mEditMask.setOnClickListener(this);
        feedLikeLayout.setOnClickListener(this);
  //      feedCommentLayout.setOnClickListener(this);

        initView();
        initViewModel();

    }
    private void initView() {

        TextView userName = findViewById(R.id.user_name);
        TextView feedTitle = findViewById(R.id.feed_title);
        TextView feedTime = findViewById(R.id.feed_time);
        AppCompatTextView feedInfo = findViewById(R.id.feed_info);
        ImageView feedPic = findViewById(R.id.feed_pic);

  //      mFeedCommentNum = findViewById(R.id.feed_comment_num);
        feedLikeNum = findViewById(R.id.feed_like_num);


        Bundle bundle = this.getIntent().getExtras();
        if (bundle == null) return;
        feed = (Feed) bundle.getSerializable("feed");
        if (feed == null) return;

        mFeedId = feed.getFeedId();


        try{
            userName.setText(feed.getUser().getUsername() == null ? "出错了":"作者："+feed.getUser().getUsername());
            feedTime.setText(feed.getCreateTime()== null ? "出错了":feed.getCreateTime());
            feedTitle.setText(feed.getFeedTitle()== null ? "出错了":feed.getFeedTitle());
            feedInfo.setText(feed.getFeedInfo()== null ? "出错了":feed.getFeedInfo());
            // 查看评论点赞数
            //mFeedCommentNum.setText(String.valueOf(feed.getCommentNum())== null ? "出错了":String.valueOf(feed.getCommentNum()));
            feedLikeNum.setText(String.valueOf(feed.getLikeNum())== null ? "出错了":feed.getLikeNum()+"");
            // 是否已经点赞
            if(feed.isLike()){
                feedLikeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_like_fill_red));
                //feedLikeIcon.setColorFilter(Color.RED);
                feedLikeIcon.setFocusable(false);

            }
        }catch (Exception e){
            Log.e("setText出错 feed",e.toString());
        }
        // 图片url
        try{
            String url = feed.getUrl();
            Glide.with(FeedActivity.this)
                    .load(url)
                    .fitCenter()
                    .into(feedPic);

        }catch (Exception e){

        }

    }

    private void initViewModel() {
        mFeedViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        mFeedViewModel.doPageComment(1, 20, mFeedId);

        mFeedViewModel.getTipMessage().observe(this, tipMessage -> {
            if (tipMessage.isRes()) {
                Toast.makeText(FeedActivity.this, tipMessage.getMsgId(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FeedActivity.this, tipMessage.getMsgStr(), Toast.LENGTH_SHORT).show();
            }
        });

        mFeedViewModel.getFeed().observe(this, feed -> {

        });
        mFeedViewModel.getFeedComment().observe(this, i -> {
            if (i == MSG_EVALUATE) {
             //   mFeedCommentNum.setText(String.valueOf(Integer.parseInt(mFeedCommentNum.getText().toString()) + 1));
            }
            i = MSG_REPLY;
            mFeedViewModel.doPageComment(1, 20, mFeedId);
        });
        mFeedViewModel.getCommentPage().observe(this, commentPageInfo -> {
            setData(commentPageInfo.getList());
        });
    }

    public void setData(List<Comment> data) {
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new QuickAdapter();

        recyclerView.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        try{
            mAdapter.replaceData(data);
            if (mAdapter.getData().isEmpty()) {
                View emptyView = getLayoutInflater().inflate(R.layout.empty, null);
                mAdapter.setEmptyView(emptyView);
            }

        }catch (Exception e){
            View emptyView = getLayoutInflater().inflate(R.layout.empty, null);
            mAdapter.setEmptyView(emptyView);
        }

    }


    private void publishComment() {
        Toast.makeText(FeedActivity.this, "评论中", Toast.LENGTH_SHORT).show();
        String msg = mEditTuCao.getText().toString().trim();
        mFeedViewModel.addEvaluate(mFeedId, msg);
        mEditTuCao.setText(null);
        hideSoftInput(mEditTuCao);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_publish:
                hideSoftInput(mEditTuCao);
                publishComment();
                break;
        }

    }

    /**
     * 调用输入法
     */
    public void openSofInput(EditText edit) {
        edit.setText(null);
        edit.requestFocus();
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏输入法
     */
    public void hideSoftInput(EditText edit) {
        //imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void imageOnClick(View view) {
        mFeedViewModel.doLike(feed);
        feedLikeIcon.setColorFilter(Color.RED);
        feedLikeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_like_fill_red));
        feedLikeIcon.setFocusable(false);
    }


    public static class QuickAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {
        public QuickAdapter() {
            super(R.layout.item_feed_item);
        }

        @Override
        protected void convert(BaseViewHolder viewHolder, Comment item) {
            try {
                viewHolder.setText(R.id.lmi_title, item.getCommentInfo())
                        .setText(R.id.lmi_describe, "用户："+item.getUser().getUsername())
                        .setText(R.id.lmi_author, item.getCreateTime());
            }catch (Exception e){
                Log.e("QuickAdapter: ",e.toString());

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

}