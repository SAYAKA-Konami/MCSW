package mcsw.account.config;

public interface Constant {

    String URL_LOGIN = "https://cas.scau.edu.cn/lyuapServer/login";

    String LOGIN_FAIL = "用户名或密码错误";

    String LOGIN_SUCCESS = "身份验证成功！您的账号和密码默认与信息门户的保持一致！";

    String REPEAT_NAME = "该昵称已经被占用。😖 重新取一个吧！";

    String REPEAT_REGISTER = "该学号已经注册了呀";

    /**
     *  Token过期时间
     */
    Integer EXPIRE_DAY = 7;

    /**
     *  JWT密钥
     */
    String SECRET_KEY = "SAYAKA";

    String USER_NOT_FOUND = "登录失败，用户不存在！";

    String WRONG_ACCOUNT_OR_PASSWD = "登录失败，用户名或密码错误!";

    String BAD_THING_HAPPENED = "登录失败，服务器被吃了＝(#>д<)ﾉ ！请重试。";

    String UPDATE_SUCCESS = "修改成功";


}
