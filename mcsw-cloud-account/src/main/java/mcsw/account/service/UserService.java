package mcsw.account.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.http.HttpRequest;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import mcsw.account.config.Constant;
import mcsw.account.config.ValueFromNacos;
import mcsw.account.dao.UserDao;
import mcsw.account.entity.User;
import mcsw.account.model.dto.UserDto;
import mcsw.account.model.vo.AuthVO;
import mcsw.account.model.vo.UserVO;
import mcsw.account.util.CrawlerUtil;
import mcsw.account.util.GetRSAPasswdUtil;
import mcsw.account.util.JWTUtil;
import mcsw.account.util.UserUtil;
import mcsw.account.util.filter.HandleRegister;
import mscw.common.api.CommonResult;
import mscw.common.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static mcsw.account.config.Constant.*;

/**
 * (User)表服务实现类
 *
 * @author Nan
 * @since 2022-11-26 16:00:54
 */
@Service
@Slf4j
public class UserService extends ServiceImpl<UserDao, User>{
    @Resource
    private CrawlerUtil crawlerUtil;
    @Resource
    private GetRSAPasswdUtil rsaPasswdUtil;
    @Resource(name = "registerChain")
    private HandleRegister handleRegister;
    @Autowired
    private ValueFromNacos value;
    @Autowired
    private UserDao userDao;


    /**
     *  注册
     * @param userDto 包含信息门户的账号密码用于确认身份。
     */
    public CommonResult<String> register(UserDto userDto){
        CommonResult<String> handle = handleRegister.handle(userDto);
        if (handle != null) return handle;
        String rsaPasswd = rsaPasswdUtil.RSAPasswd(userDto.getPasswd());
        Map<String, Object> body = new HashMap<>();
        body.put("username", userDto.getId());
        body.put("password", rsaPasswd);
        Map<String, Object> html = crawlerUtil.getHtml();
        body.putAll(html);
        boolean login = login(body);
        if (!login){
            return CommonResult.failed(Constant.LOGIN_FAIL);
        }else{
            registerIntoDatabase(userDto);
            return CommonResult.success(Constant.LOGIN_SUCCESS);
        }
    }

    /**
     *  用户登录
     */
    public CommonResult<AuthVO> login(String account, String password) {
        try{
            User user = userDao.selectByAccountUser(account);
            if (null == user) {
                return CommonResult.failed(USER_NOT_FOUND);
            }

            // 校验用户密码，这里暂不考虑对密码做加密处理
/*            String encryptPwd = UserUtil.getUserEncryptPassword(account, password);
            if (!user.getUserPwd().equals(encryptPwd)) {
                return ResultUtil.verificationFailed().buildMessage("登录失败，密码输入错误！");
            }*/
            if (!StringUtils.equals(password, user.getPasswd())){
                return CommonResult.failed(WRONG_ACCOUNT_OR_PASSWD);
            }
            // 登录成功，返回用户信息
            AuthVO vo = new AuthVO();
            UserVO userVo = new UserVO();
            BeanUtils.copyProperties(user, userVo);
            vo.setToken(JWTUtil.buildJwt(this.getLoginExpre(), userVo));
            vo.setUser(userVo);
            return CommonResult.success(vo);
        } catch (Exception ex) {
            log.error("登录失败了！{}; account:{}", ex, account);
            return CommonResult.failed(BAD_THING_HAPPENED);
        }
    }

    public CommonResult<String> update(UserDto userDto){
        String account = userDto.getId();
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setAccount(account);
        userDao.updateOne(user);
        return CommonResult.success(UPDATE_SUCCESS);
    }


    /**
     *  模拟登录信息门户
     */
    private boolean login(Map<String, Object> body){
        int httpCode = HttpRequest.post(URL_LOGIN)
                .header("Connection", "keep-alive")//头信息，多个头信息多次调用此方法即可
                .header("Accept", "*/*")
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("X-Requested-With", "XMLHttpRequest")
                .header("user-agent",value.getUserAgent())
                .form(body)//表单内容
                .execute().getStatus();
        // 账号密码正确时会跳转页面。错误时会重新刷新页面。得到的验证码是200
        return httpCode == 302;
    }

    /**
     * 获取登陆过期时间。 默认设置为7天
     */
    private Date getLoginExpre(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, EXPIRE_DAY);
        return calendar.getTime();
    }

    private boolean registerIntoDatabase(UserDto userDto){
        User user = new User().setAccount(userDto.getId())
                .setGender(userDto.getGender())
                .setName(userDto.getName())
                .setEmail(userDto.getEmail())
                .setCollege(userDto.getCollege())
                .setPasswd(userDto.getPasswd())
                .setDegree(userDto.getDegree())
                .setMajor(userDto.getMajor());
        return this.save(user);
    }

}

