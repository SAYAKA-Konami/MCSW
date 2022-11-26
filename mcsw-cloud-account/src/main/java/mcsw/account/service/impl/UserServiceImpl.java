package mcsw.account.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
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
    CrawlerUtil crawlerUtil;

    @Resource
    GetRSAPasswdUtil rsaPasswdUtil;

    @Resource
    ThreadPoolTaskExecutor crawlerTaskExecutor;

    @Autowired
    ValueFromNacos value;


    public CommonResult<String> register(UserDto userDto){
        CompletableFuture<Map<String, Object>> getParams = CompletableFuture.supplyAsync(() -> crawlerUtil.getHtml(), crawlerTaskExecutor);
        CompletableFuture<Boolean> login = new CompletableFuture<>();
        CompletableFuture.supplyAsync(() -> {
                    String rsaPasswd = rsaPasswdUtil.RSAPasswd(userDto.getPasswd());
                    Map<String, Object> map = new HashMap<>();
                    map.put("username", userDto.getId());
                    map.put("password", rsaPasswd);
                    return map;
                }, crawlerTaskExecutor)
                // 第一个参数是当前执行获得的结果。第二个是被合并的CF执行完成的结果
                .thenCombineAsync(getParams, (userNameAndPasswd, paramsFromHtml) -> {
                    paramsFromHtml.putAll(userNameAndPasswd);
                    return paramsFromHtml;
                    // 发送请求。
                }).whenComplete((body, r) -> {
                    if (r != null) {
                        login.completeExceptionally(r);
                    } else {
                        boolean result = login(body);
                        login.complete(result);
                    }
                });
        // 等待登录执行结果
        Boolean isLogin = login.join();
        if (!isLogin) {
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
                .setPasswd(userDto.getPasswd());
        return this.save(user);
    }

}

