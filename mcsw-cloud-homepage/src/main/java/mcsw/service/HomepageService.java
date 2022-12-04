package mcsw.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import mcsw.model.SyncPostDto;
import mscw.common.domain.vo.PostVo;

import java.util.List;

public interface HomepageService {
    /**
     *  获取主页的帖子
     */
    default IPage<PostVo> getHomePagePosts(){
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
     *  按照类被查询
     * @param category 0- work 1-cs 2 - master
     */
    default IPage<PostVo> homePageByCategory(int category){
        return null;
    }

    /**
     *  写入到Es中。如果后续有引入ElasticSearch的话...
     */
    default void synchronizeData(List<SyncPostDto> syncPostDtos){}
}
