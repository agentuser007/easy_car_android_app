package cn.zdn.easycar.util;

import cn.zdn.easycar.pojo.Feed;

public class PagerPosition {
    public static int pagePosition = 0;
    public static int itemPosition = 0;
    public static int banner_position;
    public static Feed feedItem;
    public static int unReadNum = 0;

    public  int getUnReadNum() {
        return unReadNum;
    }

    public  void setUnReadNum(int unReadNum) {
        PagerPosition.unReadNum = unReadNum;
    }

    public Feed getFeedItem() {
        return feedItem;
    }

    public void setFeedItem(Feed feedItem) {
        PagerPosition.feedItem = feedItem;
    }

    public static String banner_url_part_1 = "https://m.findlaw.cn/zhishi/a1694055.html";
    public static String banner_url_part_2 = "http://law.mot.gov.cn/index.action";

    public static String inspection_advice_1 = "http://jtgl.beijing.gov.cn/jgj/93950/check_car/zcfg74/624996/index.html";
    public static String inspection_advice_2 = "http://jtgl.beijing.gov.cn/jgj/93950/check_car/zcfg74/625003/index.html";
    static public String inspection_advice_3 = "https://map.beijing.gov.cn/jgjmap/tag?tagId=5eb65cebbd15badca5f65d4f";
    public static String inspection_advice_a = "http://jtgl.beijing.gov.cn/jgj/93950/check_car/index.html";



    public int getBanner_position() {
        return banner_position;
    }

    public void setBanner_position(int banner_position) {
        PagerPosition.banner_position = banner_position;
    }

    public String getBanner_url() {
        if(banner_position == 1) return banner_url_part_1;
        else return banner_url_part_2;

    }

    public void setBanner_url_part_1(String banner_url_part) {
        PagerPosition.banner_url_part_1 = banner_url_part;
    }

    public int getPagePosition() {
        return pagePosition;
    }

    public void setPagePosition(int pagePosition) {
        PagerPosition.pagePosition = pagePosition;
    }

    public int getItemPosition() {
        return itemPosition;
    }

    public void setItemPosition(int itemPosition) {
        PagerPosition.itemPosition = itemPosition;
    }

}
