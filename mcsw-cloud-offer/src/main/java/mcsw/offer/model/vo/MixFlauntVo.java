package mcsw.offer.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mcsw.offer.service.extend.startegy.flaunt.FlauntStrategy;

/**
 * @apiNote 混合三种类型的晒出记录。其中type的值参考{@link FlauntStrategy.Category}
 * @author wu nan
 * @since  2022/12/29
 **/
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ApiModel("综合排行榜")
public class MixFlauntVo {

    private UserOfFlauntVo userOfFlauntVo;

    @Schema(description = "代表帖子归属哪一类。OFFER(0), MASTER(1), CIVIL_SERVANT(2)")
    private Integer type;

    @Schema(description = "标题。offer为公司，master为学校，cs为岗位名称")
    private String title;

    @Schema(description = "副标题。offer为岗位名称，master为专业名称，cs为空")
    private String subtitle;

    @Schema(description = "内容。offer为薪资，master为空，cs为薪资")
    private String salary;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "备注")
    private String remarks;

    private String createDay;

}
