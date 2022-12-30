package mcsw.offer.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mcsw.offer.service.extend.startegy.flaunt.FlauntStrategy;

import java.util.List;

/**
 * @apiNote 混合三种类型的晒出记录。其中type的值参考{@link FlauntStrategy.Category}
 * @author wu nan
 * @since  2022/12/29
 **/
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ApiModel("offer详情")
public class MixFlauntVo {

    private UserOfFlauntVo userOfFlauntVo;

    @Schema(description = "代表帖子归属哪一类。OFFER(0), MASTER(1), CIVIL_SERVANT(2)")
    private Integer category;

    @Schema(description = "标题。offer为薪水，master为学校，cs为岗位名称")
    private String title;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "发布时间")
    private String createDay;

    @Schema(description = "回复")
    private List<CommentVo> commentVoList;

    /**
     *  冗余。以便查询发帖用户信息
     */
    @Schema(hidden = true)
    private Integer userId;
    /**
     *  以下为offer独有的字段
     */
    @Schema(description = "岗位名称")
    private String positionName;

    @Schema(description = "公司")
    private String company;

    @Schema(description = "0-IT|互联网|通信 1-金融 2-采购|贸易|交通|物流 3-生产|制造")
    private String industry;

    /**
     * 以下为master独有的字段
     */
    @Schema(description = "目标专业")
    private String masterMajor;

    @Schema(description = "考研专业课代码")
    private Integer code;

    @Schema(description = "考研总分")
    private Integer score;

    @Schema(description = "考研-分数/保研")
    private String masterType;
    /**
     *  以下为CS独有字段
     */
    @Schema(description = "工资")
    private String csSalary;
}
