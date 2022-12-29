package mcsw.offer.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@ApiModel("研究生上岸")
public class RequestMasterDto {
    /**
     * 目标学校
     */
    @Schema(description = "目标院校")
    @NotNull
    private String university;
    /**
     * 目标专业
     */
    @Schema(description = "目标专业")
    @NotNull
    private String masterMajor;

    @Schema(description = "考研专业课代码")
    private Integer code;
    /**
     *  考研总分
     */
    @Schema(description = "考研总分")
    private Integer score;

    /**
     * 0-校招，1-实习.
     * 只有offer,master是必填项
     */
    @Schema(description = "offer: 0-考研，1-保研")
    @NotNull
    private Integer type;
}
