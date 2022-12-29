package mcsw.offer.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @apiNote offer页面浏览时的卡片信息。不携带发帖的用户信息
 * @author wu nan
 * @since  2022/12/29
 **/
@Data
@Builder
public class MixBrowserVo {
    @Schema(description = "帖子ID")
    private Integer id;

    @Schema(description = "代表帖子归属哪一类。OFFER(0), MASTER(1), CIVIL_SERVANT(2)")
    private Integer type;

    @Schema(description = "offer：公司名称, master:学校，cs：城市")
    private String title;

    @Schema(description = "offer:薪资， master：无， cs:薪资")
    private String rightTitle;

    @Schema(description = "offer:岗位，master：专业名称，cs：岗位名称")
    private String subtitle;

    private String createTime;

    @Schema(description = "查看人数")
    private Integer watched;

    @Schema(description = "备注。只展示8个字")
    private String remarks;

    @Schema(description = "学院")
    private String college;

    @Schema(description = "学位")
    private String degree;

    @Schema(hidden = true)
    private Integer userId;
}
