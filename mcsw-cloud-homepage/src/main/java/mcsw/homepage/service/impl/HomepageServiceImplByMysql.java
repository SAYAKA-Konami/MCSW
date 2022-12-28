package mcsw.homepage.service.impl;

import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import mcsw.homepage.client.PostClient;
import mcsw.homepage.model.dto.SyncPostDto;
import mcsw.homepage.service.HomepageService;
import mscw.common.api.CommonResult;
import mscw.common.domain.dto.QueryPosts;
import mscw.common.domain.vo.PostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static mscw.common.config.Constants.SERVER_ERROR;

/**
 * @apiNote 暂时先从数据库查询
 * @author wu nan
 * @since  2022/12/27
 **/
@Service("homepageServiceByMysql")
@Slf4j
public class HomepageServiceImplByMysql implements HomepageService {

    private PostClient postClient;


    @Override
    public CommonResult<IPage<PostVo>> getHomePagePosts(QueryPosts queryPosts) {
        CommonResult<List<PostVo>> homePosts = postClient.getHomePosts(queryPosts);
        if (homePosts.getCode() == HttpStatus.HTTP_OK) {
            List<PostVo> data = homePosts.getData();
            IPage<PostVo> page = Page.of(queryPosts.getRequestPage().getCurrent(), queryPosts.getRequestPage().getSize());
            page.setRecords(data);
            return CommonResult.success(page);
        }else{
            return CommonResult.failed(SERVER_ERROR);
        }
    }

    @Override
    public IPage<PostVo> searchPost(String content) {
        return HomepageService.super.searchPost(content);
    }


    @Autowired
    public void setPostClient(PostClient postClient) {
        this.postClient = postClient;
    }
}
