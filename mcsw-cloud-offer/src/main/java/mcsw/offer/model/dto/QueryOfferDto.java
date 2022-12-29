package mcsw.offer.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("查询offer")
public class QueryOfferDto {
    @Schema(description = "帖子id")
    @NotNull
    private Integer id;


    @NotNull
    @Range(min = 0, max = 2, message = "参数错误")
    @Schema(description = "帖子种类")
    private Integer category;
}
