package cn.zdn.easycar.pojo;

import java.io.Serializable;
import java.util.List;

// 动态
public class Feed implements Serializable {

    private String feedId;
    // 用户信息
    private User user;
    private String feedInfo;
    private String feedTitle;

    // 评论数
    private Integer commentNum;
    // 当前用户是否点赞
    private boolean like;
    // 点赞数
    private Integer likeNum;
    // 相册
    private String photoList;
    private String createTime;
    private String updateTime;
    private String url;
    // 索引
    private int position;

    public Feed(){}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getFeedTitle() {
        return feedTitle;
    }

    public void setFeedTitle(String feedTitle) {
        this.feedTitle = feedTitle;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFeedInfo() {
        return feedInfo;
    }

    public void setFeedInfo(String feedInfo) {
        this.feedInfo = feedInfo;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public String getPhotoList() {
        return photoList;
    }

    public void setPhotoList(String photoList) {
        this.photoList = photoList;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Feed{" +
                ", user=" + user +
                ", feedInfo='" + feedInfo + '\'' +
                ", commentNum=" + commentNum +
                ", like=" + like +
                ", likeNum=" + likeNum +
                ", photoList=" + photoList +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", position=" + position +
                '}';
    }
}
