package mcsw.offer.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@ApiModel("考公上岸")
public class RequestCsDto {
    /**
     *  只有offer是必填项
     */
    @Schema(description = "岗位名称")
    private String positionName;

    /**
     *  只有offer是必填项
     */
    @Schema(description = "薪酬")
    private String salary;

    @NotNull
    @Schema(description = "城市", required = true)
    private String city;
}
