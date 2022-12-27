package mcsw.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import mcsw.model.dto.SyncPostDto;
import mcsw.service.HomepageService;
import mscw.common.domain.vo.PostVo;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @apiNote 暂时先从数据库查询
 * @author wu nan
 * @since  2022/12/27
 **/
@Service("homepageServiceByMysql")
@Slf4j
public class HomepageServiceImplByMysql implements HomepageService {


    @Override
    public IPage<PostVo> getHomePagePosts() {
        return HomepageService.super.getHomePagePosts();
    }

    @Override
    public IPage<PostVo> searchPost(String content) {
        return HomepageService.super.searchPost(content);
    }

    @Override
    public IPage<PostVo> homePageByCategory(int category) {
        return HomepageService.super.homePageByCategory(category);
    }

    @Override
    public void synchronizeData(List<SyncPostDto> syncPostDtos) {
        HomepageService.super.synchronizeData(syncPostDtos);
    }
}
