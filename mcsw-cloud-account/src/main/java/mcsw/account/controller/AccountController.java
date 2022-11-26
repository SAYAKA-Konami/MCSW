package mcsw.account.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import mcsw.account.model.dto.UserDto;
import mcsw.account.service.UserService;
import mscw.common.api.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {

   private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test")
    public CommonResult<String> test(){
      log.info("接受到请求");
      return CommonResult.success("hello! This is Account model");
    }

    @ApiOperation(value = "注册", httpMethod = "POST")
    @PostMapping("/register")
    public CommonResult<String> register(UserDto userDto){
        return userService.register(userDto);
    }

}
