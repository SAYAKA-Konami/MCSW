package mcsw.account.controller;

import lombok.extern.slf4j.Slf4j;
import mscw.common.api.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {

    @GetMapping("/test")
    public CommonResult<String> test(){
      log.info("接受到请求");
      return CommonResult.success("hello! This is Account model");
    }

}
