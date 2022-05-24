package cn.zdn.easycar.pojo;

import java.util.Date;

public class FeedInfo {
    private String feed_title;
    private String feed_data;
    private String feed_author;
    private int feed_num_msg;
    private int feed_id;
    private String feed_content;

    public FeedInfo(){}



    public int getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(int feed_id) {
        this.feed_id = feed_id;
    }

    public String getFeed_title() {
        return feed_title;
    }

    public void setFeed_title(String feed_title) {
        this.feed_title = feed_title;
    }

    public String getFeed_data() {
        return feed_data;
    }

    public void setFeed_data(String feed_data) {
        this.feed_data = feed_data;
    }

    public String getFeed_author() {
        return feed_author;
    }

    public void setFeed_author(String feed_author) {
        this.feed_author = feed_author;
    }

    public int getFeed_num_msg() {
        return feed_num_msg;
    }

    public void setFeed_num_msg(int feed_num_msg) {
        this.feed_num_msg = feed_num_msg;
    }

    public String getFeed_content() {
        return feed_content;
    }

    public void setFeed_content(String feed_content) {
        this.feed_content = feed_content;
    }
}
