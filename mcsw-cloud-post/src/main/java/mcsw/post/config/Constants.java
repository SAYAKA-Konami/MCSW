package mcsw.post.config;

public interface Constants {

    String USER_NOT_FOUND = "查无此昵称的用户";

    String HAS_LIKED = "已经点赞过了";

    String POST_NOT_FOUND = "帖子不存在";

    String POST_SUCCESS = "发帖成功";

    String POST_FAILED = "发帖失败";

    String LIKE_SUCCESS = "点赞成功!";

    String SERVER_ERROR = "服务器被吃了T ^ T";

    String REPLY_SUCCESS = "评论成功！";

    String WRONG_PARAM = "参数错误";

    /**
     *  评论点赞数前缀
     */
    String REPLY_LIKE_KEY_PREFIX = "reply_like_";

    /**
     *  帖子点赞数前缀
     */
    String POST_LIKE_KEY_PREFIX = "post_like_";
    /**
     *  点赞评论的用户列表的ID
     */
    String REPLY_LIKE_USER_LIST_KEY_PREFIX = "reply_like_list_";

    /**
     *  点赞帖子的用户列表的ID
     */
    String POST_LIKE_USER_LIST_KEY_PREFIX = "post_like_list_";


}
