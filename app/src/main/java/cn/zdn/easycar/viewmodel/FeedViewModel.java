package cn.zdn.easycar.viewmodel;


import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import java.io.File;
import java.util.List;

import cn.zdn.easycar.CarInspectionActivity;
import cn.zdn.easycar.R;
import cn.zdn.easycar.config.Api;
import cn.zdn.easycar.config.Constants;
import cn.zdn.easycar.okhttp.OkUtil;
import cn.zdn.easycar.okhttp.ResultCallback;
import cn.zdn.easycar.pojo.Comment;
import cn.zdn.easycar.pojo.Feed;
import cn.zdn.easycar.pojo.PageInfo;
import cn.zdn.easycar.pojo.TipMessage;
import cn.zdn.easycar.result.Result;
import cn.zdn.easycar.result.ResultConstant;
import cn.zdn.easycar.ui.home.FeedActivity;
import cn.zdn.easycar.util.ImageUtil;
import cn.zdn.easycar.util.SPUtil;
import okhttp3.Call;


public class FeedViewModel extends ViewModel {

    private final MutableLiveData<TipMessage> mTipMessage;
    private final MutableLiveData<PageInfo<Feed>> mFeedPage;
    private final MutableLiveData<Feed> mFeed;
    private final MutableLiveData<Integer> mFeedComment;
    private final MutableLiveData<PageInfo<Comment>> mCommentPage;

    public FeedViewModel() {
        mTipMessage = new MutableLiveData<>();
        mFeedPage = new MutableLiveData<>();
        mFeed = new MutableLiveData<>();
        mFeedComment = new MutableLiveData<>();
        mCommentPage = new MutableLiveData<>();
    }
    String userId = SPUtil.build().getString(Constants.SP_USER_ID);

    public LiveData<TipMessage> getTipMessage() {
        return mTipMessage;
    }

    public LiveData<PageInfo<Feed>> getFeedPage() {
        return mFeedPage;
    }

    public LiveData<Feed> getFeed() {
        return mFeed;
    }

    public LiveData<Integer> getFeedComment() {
        return mFeedComment;
    }

    public LiveData<PageInfo<Comment>> getCommentPage() {
        return mCommentPage;
    }



    /**
     * 获取动态
     * @param pageNum 页码
     * @param pageSize 页容量
     * @param state 动态类型
     */
    public void doPageFeed(int pageNum, int pageSize, int state) {
        OkUtil.post()
                .url(Api.pageFeed)
                .addParam("state", state)
                .addParam("pageNum", pageNum)
                .addParam("pageSize", pageSize)
                .execute(new ResultCallback<Result<PageInfo<Feed>>>() {
                    @Override
                    public void onSuccess(Result<PageInfo<Feed>> response) {
                        String code = response.getCode();
                        if (ResultConstant.CODE_SUCCESS.equals(code)) {
                            mFeedPage.postValue(response.getData());
                        } else {
                            mTipMessage.postValue(TipMessage.resId(R.string.toast_get_feed_error));
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        mTipMessage.postValue(TipMessage.resId(R.string.toast_get_feed_error));
                    }
                });
    }

    /**
     * 保存动态
     * @param feedInfo 动态信息
     * @param photos 动态图片
     */
    public void saveFeed(String feedTitle, String feedInfo, List<String> photos) {
        int state = SPUtil.build().getInt(Constants.SP_USER_STATE);

        OkUtil.post()
                .url(Api.saveFeed)
                //测试
                .addParam("userId", userId)
                .addParam("feedTitle", feedTitle)
                .addParam("feedInfo", feedInfo)
                .addParam("state", state)
                .addFiles("file", ImageUtil.pathToImageFile(photos))
                .execute(new ResultCallback<Result<Feed>>() {
                    @Override
                    public void onSuccess(Result<Feed> response) {
                        String code = response.getCode();
                        if (ResultConstant.CODE_SUCCESS.equals(code)) {
                            mFeed.postValue(response.getData());
                        } else {
                            mTipMessage.postValue(TipMessage.str("发布失败"));
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        mTipMessage.postValue(TipMessage.str("发布失败"));
                    }
                });
    }

    /**
     * 点赞
     * @param feed 动态
     */
    public void doLike(Feed feed) {
        OkUtil.post()
                .url(Api.saveAction)
                .addParam("userId", userId)
                //测试
                .addParam("feedId", feed.getFeedId())
                .execute(new ResultCallback<Result<Object>>() {
                    @Override
                    public void onSuccess(Result<Object> response) {
                        Log.i("点赞",response.getMsg());
                        String code = response.getCode();
                        if (ResultConstant.CODE_SUCCESS.equals(code)) {
                            mFeed.postValue(feed);
                            mTipMessage.postValue(TipMessage.str("赞"));

                        } else {
                            mTipMessage.postValue(TipMessage.str("已经赞过啦"));

                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        mTipMessage.postValue(TipMessage.str("点赞失败"));
                    }
                });
    }

    public void viewFeed(String feedId) {
        OkUtil.post()
                .url(Api.viewFeed)
                .addParam("id", feedId)
                .execute();
    }

    /**
     * 添加评论
     */
    public void addEvaluate(String feedId,  String comment) {
        OkUtil.post()
                .url(Api.saveComment)
                .addParam("feedId", feedId)
                .addParam("commentInfo", comment)
                .addParam("userId", userId)
                //测试

                .execute(new ResultCallback<Result<Object>>() {
                    @Override
                    public void onSuccess(Result<Object> response) {
                        String code = response.getCode();
                        if (ResultConstant.CODE_SUCCESS.equals(code)) {
                            mFeedComment.postValue(0);
                        } else {
                            mTipMessage.postValue(TipMessage.str("评论失败"));
                        }

                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        mTipMessage.postValue(TipMessage.str("评论失败"));
                    }
                });
    }

    /**
     * 获取评论数据
     */
    public void doPageComment(int pageNum, int pageSize, String feedId) {
        OkUtil.post()
                .url(Api.pageComment)
                .addParam("feedId", feedId)
                .addParam("pageNum", pageNum)
                .addParam("pageSize", pageSize)
                .execute(new ResultCallback<Result<PageInfo<Comment>>>() {
                    @Override
                    public void onSuccess(Result<PageInfo<Comment>> response) {
                        String code = response.getCode();
                        if (ResultConstant.CODE_SUCCESS.equals(code)) {
                            mCommentPage.postValue(response.getData());
                        } else {
                            mTipMessage.postValue(TipMessage.resId(R.string.toast_get_feed_error));
                        }

                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        mTipMessage.postValue(TipMessage.resId(R.string.toast_get_feed_error));
                    }
                });
    }


    /**
     * 更新未读条数
     */
    public void updateUnread() {
        OkUtil.post()
                .url(Api.updateUnread)
                .execute(new ResultCallback<Result<Integer>>() {
                    @Override
                    public void onSuccess(Result<Integer> response) {
                        Constants.isRead = true;
                    }

                    @Override
                    public void onError(Call call, Exception e) {

                    }
                });
    }
}
