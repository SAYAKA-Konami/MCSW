package mcsw.offer.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("晒offer")
public class RequestOfferDto {

    @NotNull
    @Schema(description = "城市", required = true)
    private String city;
    /**
     *  只有offer是必填项
     */
    @Schema(description = "岗位名称")
    @NotNull
    private String positionName;

    /**
     *  只有offer是必填项
     */
    @Schema(description = "薪酬")
    @NotNull
    private String salary;

    /**
     * 公司。只有offer是必填项
     */
    @Schema(description = "公司")
    @NotNull
    private String company;

    /**
     * 0-校招，1-实习.
     * 只有offer,master是必填项
     */
    @Schema(description = "offer:0-校招，1-实习")
    @NotNull
    @Range(min = 0, max = 1, message = "参数错误")
    private Integer type;

    /**
     * 学历
     */
    @Schema(description = "学历")
    @NotNull
    private String education;
    /**
     * 0-IT|互联网|通信 1-金融 2-采购|贸易|交通|物流 3-生产|制造
     */
    @Schema(description = "0-IT|互联网|通信 1-金融 2-采购|贸易|交通|物流 3-生产|制造")
    @NotNull
    private Integer industry;

}
