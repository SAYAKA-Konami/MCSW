package mscw.common.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("分页参数")
public class RequestPage {


    @NotNull
    @Schema(description = "当前页，从1开始。不需要分页时可以传入负数")
    private Long current;

    @NotNull
    @Schema(description = "每页大小")
    @Min(1)
    private Long size;

}
