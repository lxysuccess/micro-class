package yinzhi.micro_client.network.constants;

/**
 * Created by LIXUYU on 16/2/19.
 */
public interface INetworkConstants {

    public final static String LOGON_TOKEN = "X-YZMC-LogonToken";
    
    public final static String CHARSET = "UTF-8";
    
    //============接口相关==============
    public final static String YZMC_SERVER = "http://10.189.234.155:8080";
    //首页
    public final static String API_COURSE_SLIDELIST = YZMC_SERVER + "/api/v1/course/slide/list";
    public final static String API_COURSE_HOTRECOMMEND = YZMC_SERVER + "/api/v1/course/hotRecommend ";
    public final static String API_COURSE_FREERECOMMEND = YZMC_SERVER + "/api/v1/course/freeRecommend ";
    public final static String API_COURSE_FREERANKINGLIST = YZMC_SERVER + "/api/v1/course/freeRankingList";
    public final static String API_COURSE_HOTRANKINGLIST = YZMC_SERVER + "/api/v1/course/hotRankingList";
    public final static String API_COURSE_SEARCH = YZMC_SERVER + "/api/v1/course/search";
    public final static String API_COURSE_DETAIL = YZMC_SERVER + "/api/v1/course/detail";
    public final static String API_COURSE_CATALOG = YZMC_SERVER + "/api/v1/course/catalog";
    public final static String API_COURSE_TIPS = YZMC_SERVER + "/api/v1/course/tips";
    public final static String API_COURSE_SUBSCRIBE = YZMC_SERVER + "/api/v1/course/subscribe";
    
    //练习
    public final static String API_EXERCISE = YZMC_SERVER + "/api/v1/exercise";
    public final static String API_EXERCISE_RECORD = YZMC_SERVER + "/api/v1/exercise/record";

    //视频播放
    public final static String API_VIDEO = YZMC_SERVER + "/api/v1/video";

    //评论
    public final static String API_COMMENT_PUBLISH = YZMC_SERVER + "/api/v1/comment/publish";
    public final static String API_COMMENT_LIST = YZMC_SERVER + "/api/v1/comment/list";
    //TODO 赞的接口
    
    //分类
    public final static String API_CLASSIFY_LIST = YZMC_SERVER + "/api/v1/classify/list";
    public final static String API_CLASSIFY_LISTBYCLASSIFY = YZMC_SERVER + " /api/v1/classify/listByClassify";
    

    //用户
    public final static String API_USER_LOGIN = YZMC_SERVER + "/api/v1/user/login";
    public final static String API_USER_REGISTER = YZMC_SERVER + "/api/v1/user/register";
    public final static String API_USER_VERIFYUSERNAME = YZMC_SERVER + "/api/v1/user/verifyUsername";
    public final static String API_USER_MODIFYNICKNAME = YZMC_SERVER + "/api/v1/user/modifyNickname";
    public final static String API_USER_COURSELIST = YZMC_SERVER + "/api/v1/user/courseList";
    public final static String API_USER_SCHEDULE = YZMC_SERVER + "/api/v1/user/schedule";

}
