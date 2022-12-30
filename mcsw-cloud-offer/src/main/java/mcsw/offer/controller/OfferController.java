package mcsw.offer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import mcsw.offer.manage.ManageService;
import mcsw.offer.model.dto.QueryOfferDto;
import mcsw.offer.model.dto.RequestCommentDto;
import mcsw.offer.model.dto.RequestShowOffDto;
import mcsw.offer.model.vo.MixBrowserVo;
import mcsw.offer.model.vo.MixFlauntVo;
import mcsw.offer.service.CommentService;
import mscw.common.api.CommonResult;
import mscw.common.domain.dto.RequestPage;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/offer")
public class OfferController {
    @Resource
    private  ManageService manageService;
    @Resource
    private CommentService commentService;


    @PostMapping("/showOff")
    @ApiOperation("晒offer或上岸记录")
    public CommonResult<String> flaunt(@RequestHeader Map<String, String> header, @RequestBody @Valid RequestShowOffDto requestShowOffDto){
        return manageService.flaunt(header, requestShowOffDto);
    }

    @PostMapping("/getInfo")
    @ApiOperation("获取offer或上岸记录的详情")
    public CommonResult<MixFlauntVo> getInfo(@RequestBody @Valid QueryOfferDto dto){
        return manageService.getInfoOfOne(dto);
    }

    @PostMapping("/browser")
    @ApiOperation("浏览首页。按热度排行从高到低显示")
    public CommonResult<IPage<MixBrowserVo>> browser(@RequestBody @Valid RequestPage requestPage){
        return manageService.getMixData(requestPage);
    }

    @PostMapping("/comment")
    @ApiOperation("评论")
    public CommonResult<String> comment(@RequestHeader Map<String, String> header, @RequestBody @Valid RequestCommentDto dto){
        return commentService.comment(header, dto);
    }

}
