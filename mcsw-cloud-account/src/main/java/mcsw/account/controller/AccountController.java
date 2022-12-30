package mcsw.account.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import mcsw.account.model.dto.UserDto;
import mcsw.account.model.dto.UserLoginDto;
import mscw.common.domain.vo.AuthVO;
import mcsw.account.service.UserService;
import mscw.common.api.CommonResult;
import mscw.common.domain.vo.UserVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {

   private final UserService userService;


    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "注册", httpMethod = "POST")
    @PostMapping("/register")
    public CommonResult<String> register(@RequestBody @Valid UserDto userDto){
        return userService.register(userDto);
    }

    @ApiOperation(value = "登录", httpMethod = "POST")
    @PostMapping("/login")
    public CommonResult<AuthVO> login(@RequestBody @Valid UserLoginDto userLoginDto){
        return userService.login(userLoginDto.getId(), userLoginDto.getPasswd());
    }

    @ApiOperation(value = "修改", httpMethod = "POST")
    @PostMapping("/update")
    public CommonResult<String> update(@RequestBody @Valid UserDto userDto){
        return userService.update(userDto);
    }

    @ApiOperation(value = "获取用户信息")
    @GetMapping("/getUserInfo")
    public CommonResult<UserVO> getUserInfo(@RequestParam("name") String name){
        return userService.getUserInfoByName(name);
    }

    @ApiOperation(value = "获取用户信息")
    @PostMapping("/getUsersByIds")
    public CommonResult<List<UserVO>> getUsersByIds(@RequestBody @Valid Collection<Integer> ids){
        return userService.getUsersByIds(ids);
    }

    @GetMapping("/test")
    public CommonResult<String> test(){
      log.info("接受到请求");
      return CommonResult.success("hello! This is Account model");
    }

    @GetMapping("/testFilter")
    public CommonResult<String> testFilter(@RequestHeader Map<String, String> headers){

        log.info("接受到的用户实体类为：{}", headers);
        log.info("控制层接受到请求");
        userService.test(headers);
        return CommonResult.success("hello! This is Account model");
    }

}
