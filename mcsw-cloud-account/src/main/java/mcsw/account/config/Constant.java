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


}
