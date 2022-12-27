package mscw.common.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("帖子内容")
public class PostWithReply {
    private PostVo post;

    private List<ReplyVo> replyVoList;

}
