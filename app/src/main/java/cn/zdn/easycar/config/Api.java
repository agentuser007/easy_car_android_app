package cn.zdn.easycar.config;


public class Api {


    public static final String X_APP_TOKEN = "X-App-Token";
    /**
     * url
     */
    private static String baseUrl = "http://10.0.2.2:8080";



    /**
     * 用户注册
     */
    public static String userRegister = baseUrl + "/user/register";
    /**
     * 用户登录
     */
    public static String userLogin = baseUrl + "/user/login";
    /**
     * 重置密码
     */
    public static String resetPassword = baseUrl + "/user/reset";
    /**
     * 更新用户信息
     */
    public static String updateUser = baseUrl + "/user/update";
    /**
     * 获取用户信息
     */
    public static String userInfo = baseUrl + "/user/info";

    /**
     * 查询用户违章信息
     */
    public static String queryVio = baseUrl + "/vio/info";
    /**
     * 违章信息已读
     */
    public static String readVio = baseUrl + "/vio/isread";

    /**
     * 未读违章条数
     */
    public static String unread = baseUrl + "/vio/unread";


    /**
     * 获取用户车辆信息
     */
    public static String userCar = baseUrl + "/car/info";
    /**
     * 设置用户车辆信息
     */
    public static String updateCar = baseUrl + "/car/update";
    /**
     * 动态列表
     */
    public static String pageFeed = baseUrl + "/feed/page";
    /**
     * 发布动态
     */
    public static String saveFeed = baseUrl + "/feed/save";
    /**
     * 查看动态
     */
    public static String viewFeed = baseUrl + "/feed/view";

    /**
     * 用户动态
     */
    public static String getUserFeed = baseUrl + "/feed/user";
    /**
     * 新增动态操作点赞
     */
    public static String saveAction = baseUrl + "/feed/action/save";
    /**
     * 移除动态操作,如取消赞
     */
    public static String removeAction = baseUrl + "/feed/action/remove";
    /**
     * 动态评论列表
     */
    public static String pageComment = baseUrl + "/feed/comment/page";
    /**
     * 新增动态评论
     */
    public static String saveComment = baseUrl + "/feed/comment/save";

    /**
     * 更新未读为已读
     */
    public static String updateUnread = baseUrl + "/feed/comment/unread/update";

    /**
     * 动态删除
     */
    public static String removeFeed = baseUrl + "/feed/remove";
    /**
     * 加载服务器图片地址
     */
    public static String rssUrl = baseUrl;

}
