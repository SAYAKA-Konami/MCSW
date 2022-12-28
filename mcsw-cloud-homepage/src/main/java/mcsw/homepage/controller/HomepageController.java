package mcsw.homepage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import mcsw.homepage.service.HomepageService;
import mscw.common.api.CommonResult;
import mscw.common.domain.dto.QueryPosts;
import mscw.common.domain.vo.PostVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/homepage")
@Slf4j
public class HomepageController {

    @Resource(name = "homepageServiceByMysql")
    private HomepageService homepageService;


    @PostMapping("/home")
    @ApiOperation("查询首页帖子。可根据不同的板块更改相应的参数。")
    public CommonResult<IPage<PostVo>> getHomePage(@RequestBody QueryPosts queryPosts){
        return homepageService.getHomePagePosts(queryPosts);
    }
}
