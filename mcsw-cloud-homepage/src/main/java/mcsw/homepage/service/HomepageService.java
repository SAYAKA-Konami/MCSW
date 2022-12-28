package mcsw.homepage.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import mcsw.homepage.model.dto.SyncPostDto;
import mscw.common.api.CommonResult;
import mscw.common.domain.dto.QueryPosts;
import mscw.common.domain.vo.PostVo;

import java.util.List;

public interface HomepageService {
    /**
     *  获取主页的帖子
     */
    default CommonResult<IPage<PostVo>> getHomePagePosts(QueryPosts queryPosts){
        return null;
    }

    /**
     * 根据关键字或内容查找
     * @param content 内容
     */
    default IPage<PostVo> searchPost(String content){
        return null;
    }



    /**
     *  写入到Es中。如果后续有引入ElasticSearch的话...
     */
    default void synchronizeData(List<SyncPostDto> syncPostDtos){}
}
