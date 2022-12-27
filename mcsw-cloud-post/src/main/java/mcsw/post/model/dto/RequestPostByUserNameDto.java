package mcsw.post.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mscw.common.domain.dto.RequestPage;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("获取指定用户的帖子")
public class RequestPostByUserNameDto {

    @Schema(name = "name", description = "用户名称")
    @NotNull
    private String name;

    @NotNull
    private RequestPage requestPage;
}
