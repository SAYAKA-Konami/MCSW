package mcsw.offer.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
@Builder
@ApiModel("Po出offer或上岸信息")
public class RequestShowOffDto {

    @Schema(description = "0-offer, 1-master, 2-cs", required = true)
    @NotNull
    @Range(min = 0, max = 2)
    private Integer category;
    /**
     * 备注
     */
    @Schema(description = "备注", required = true)
    private String remarks;

    private RequestOfferDto offerDto;

    private RequestMasterDto masterDto;

    private RequestCsDto    csDto;
    /**
     *  用户相关信息由网关负责解析获得即可
     */
    private Integer userId;

    private Integer college;

    private String userMajor;


}
