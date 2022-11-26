package mcsw.account.service.impl;

import cn.hutool.http.HttpRequest;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import mcsw.account.config.Constant;
import mcsw.account.config.ValueFromNacos;
import mcsw.account.dao.UserDao;
import mcsw.account.entity.User;
import mcsw.account.model.dto.UserDto;
import mcsw.account.service.UserService;
import mcsw.account.util.CrawlerUtil;
import mcsw.account.util.GetRSAPasswdUtil;
import mcsw.account.util.filter.HandleRegister;
import mscw.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static mcsw.account.config.Constant.URL_LOGIN;

/**
 * (User)表服务实现类
 *
 * @author Nan
 * @since 2022-11-26 16:00:54
 */
@Service("userService")
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
    @Resource
    private CrawlerUtil crawlerUtil;

    @Resource
    private GetRSAPasswdUtil rsaPasswdUtil;

    @Autowired
    private ValueFromNacos value;

    @Resource(name = "registerChain")
    private HandleRegister handleRegister;


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

