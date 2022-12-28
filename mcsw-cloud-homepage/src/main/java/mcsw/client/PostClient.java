package mcsw.client;

import mscw.common.api.CommonResult;
import mscw.common.domain.dto.RequestPage;
import mscw.common.domain.vo.PostVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("mcsw-cloud-post")
public interface PostClient {

    /**
     *  查询主页帖子。以下要素按照从高到低排序
     *  评论数
     *  点赞数
     *  发布时间
     *  三种类别的帖子均匀取
     * @apiNote 在未引入ElasticSearch之前，暂且将此任务交给Mysql。所以这里需要涉及到跨服务调用
     */
    @PostMapping("/homepage")
    CommonResult<List<PostVo>> getHomePosts(@RequestBody RequestPage requestPage);
}
