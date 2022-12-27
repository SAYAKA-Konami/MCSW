package mcsw.post.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("包装回复信息")
public class RequestReplyDto {
    private ReplyNestedDto replyNestedDto;

    private ReplyPostDto replyPostDto;
}
