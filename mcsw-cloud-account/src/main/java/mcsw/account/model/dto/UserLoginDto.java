package mcsw.account.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class UserLoginDto {
    @NotNull
    @Schema(name = "id", description = "账号")
    private String id;
    @NotNull
    @Schema(name = "passwd", description = "密码")
    private String passwd;
}
