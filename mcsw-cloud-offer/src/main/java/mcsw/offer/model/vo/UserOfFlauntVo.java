package mcsw.offer.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@ApiModel("记录所需的用户信息")
public class UserOfFlauntVo {
    /**
     * 登录名
     */
    private String name;

    private String genderCz;

    private String email;

    @Schema(description = "学院")
    private String collegeCz;

    @Schema(description = "学位")
    private String degreeCz;

    /**
     *  专业名称
     */
    @Schema(description = "专业")
    private String major;
}
