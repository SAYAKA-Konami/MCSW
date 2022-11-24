package mcsw.gateway.controller;

import lombok.extern.slf4j.Slf4j;
import mscw.common.api.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/gate")
public class GateWayController {
    @GetMapping("/test")
    public CommonResult<String> test(){
        log.info("网关接受到测试请求");
        return CommonResult.success("hello！！I'm GateWay!");
    }
}
